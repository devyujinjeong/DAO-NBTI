<script setup>
import { ref, onMounted, watchEffect, computed, onBeforeUnmount } from 'vue'
import { useRoute } from 'vue-router'
import { getProblems, submitAnswers } from "@/features/study/api.js"
import router from "@/router/index.js"

const categoryMap = {
  '언어 이해': 1,
  '시사 상식': 2,
  '지각 추론': 3,
  '처리 속도': 4,
  '작업 기억': 5,
  '공간 지각력': 6
}

const route = useRoute()
const categoryName = ref(route.query.category)
const categoryId = ref(categoryMap[categoryName.value] ?? 0)
const level = ref(route.query.level === 'random' ? 0 : Number(route.query.level))

const problems = ref([])
const currentProblemIndex = ref(0)
const totalProblems = computed(() => problems.value.length)

const percentage = ref(0)
const time = ref(0)
const timerDisplay = ref('0')
let interval

const userAnswer = ref('')
const userAnswers = ref([])

const isImageModalOpen = ref(false)
const isConfirmModalOpen = ref(false)

function openImageModal() {
  isImageModalOpen.value = true
}
function closeImageModal() {
  isImageModalOpen.value = false
}
function closeConfirmModal() {
  isConfirmModalOpen.value = false
}
function confirmAndContinue() {
  closeConfirmModal()
  submitAnswerAndContinue()
}

// ESC로 닫기
function onEsc(e) {
  if (e.key === 'Escape') closeImageModal()
}
onMounted(() => {
  fetchProblems()
  window.addEventListener('keydown', onEsc)
})
onBeforeUnmount(() => {
  clearInterval(interval)
  window.removeEventListener('keydown', onEsc)
})

// 문제 가져오기
async function fetchProblems() {
  try {
    const res = await getProblems(categoryId.value, level.value)
    problems.value = res.data.data.slice(0, 3)
    currentProblemIndex.value = 0
    startTimer()
  } catch (e) {
    console.error('문제 불러오기 실패:', e)
  }
}

// 타이머 시작
function startTimer() {
  if (interval) clearInterval(interval)
  const current = problems.value[currentProblemIndex.value]
  time.value = current?.timeLimit ?? 60
  timerDisplay.value = time.value.toString()

  interval = setInterval(() => {
    if (time.value <= 0) {
      clearInterval(interval)
      timerDisplay.value = '0'
      submitAnswerAndContinue()
    } else {
      time.value--
      timerDisplay.value = time.value.toString()
    }
  }, 1000)
}

async function submitAllAnswers() {
  try {
    const response = await submitAnswers({ answers: userAnswers.value })
    const studyId = response.data.data.studyId
    router.push({ path: '/study/result', query: { studyId } })
  } catch (e) {
    console.error('제출 실패:', e)
  }
}

function goToNextProblem() {
  // 시간이 남았고, 답안이 비어 있다면 모달 띄움
  if (time.value > 0 && userAnswer.value.trim() === '') {
    isConfirmModalOpen.value = true
    return
  }

  // 시간이 다 됐거나 답안이 입력되어 있으면 바로 다음으로
  submitAnswerAndContinue()
}

function submitAnswerAndContinue() {
  const current = problems.value[currentProblemIndex.value]
  userAnswers.value.push({
    problemId: current.problemId,
    userAnswer: userAnswer.value
  })

  if (currentProblemIndex.value < totalProblems.value - 1) {
    currentProblemIndex.value++
    userAnswer.value = ''
    startTimer()
  } else {
    clearInterval(interval)
    submitAllAnswers()
  }
}

watchEffect(() => {
  percentage.value =
      totalProblems.value > 0
          ? ((currentProblemIndex.value + 1) / totalProblems.value) * 100
          : 0
})
</script>

<template>
  <div class="timer">{{ timerDisplay }}</div>

  <div class="container" v-if="problems.length > 0">
    <div class="progress-container">
      <div class="progress-bar" :style="{ width: percentage + '%' }"></div>
    </div>

    <div class="image-area">
      <img
          class="question-image"
          :src="problems[currentProblemIndex].contentImageUrl"
          alt="문제 이미지"
          @click="openImageModal"
      />
    </div>

    <div class="answer-input">
      정답 :
      <input type="text" v-model="userAnswer" />
    </div>

    <div class="button-group">
      <button class="btn" @click="goToNextProblem">다음</button>
    </div>
  </div>

  <!-- 이미지 모달 -->
  <div v-if="isImageModalOpen" class="modal-overlay" @click="closeImageModal">
    <div class="modal-wrapper" @click.stop>
      <div class="modal-image-container">
        <button class="close-button" @click.stop="closeImageModal">×</button>
        <img
            class="modal-image"
            :src="problems[currentProblemIndex].contentImageUrl"
            alt="확대 문제 이미지"
        />
      </div>
    </div>
  </div>

  <!-- 답변 확인 모달 -->
  <div v-if="isConfirmModalOpen" class="modal-overlay" @click="closeConfirmModal">
    <div class="modal-wrapper" @click.stop>
      <div class="confirm-modal">
        <p>답안을 제출하고 다음 문제로 넘어가시겠습니까?</p>
        <div class="modal-actions">
          <button class="btn" @click="confirmAndContinue">확인</button>
          <button class="btn cancel" @click="closeConfirmModal">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 900px;
  margin: 3rem auto;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  padding: 3rem 4rem;
  text-align: center;
  position: relative;
}

.progress-container {
  width: 100%;
  height: 6px;
  background-color: #e5e7eb;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 2rem;
}

.progress-bar {
  height: 100%;
  background-color: #3b82f6;
  transition: width 0.3s ease;
}

.image-area {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.question-image {
  width: 100%;
  max-width: 720px;
  height: auto;
  border-radius: 12px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
  object-fit: contain;
  cursor: pointer;
}

.answer-input {
  margin-top: 1.5rem;
}

.answer-input input {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border-radius: 8px;
  border: 1px solid #ccc;
  width: 100px;
  text-align: center;
}

.button-group {
  margin-top: 2rem;
  display: flex;
  gap: 1.5rem;
  justify-content: center;
}

.btn {
  background: #3b82f6;
  color: #fff;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 12px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s ease;
}

.btn:hover {
  background: #1e3a8a;
}

.cancel {
  background-color: #e5e7eb;
  color: #111;
}

.cancel:hover {
  background-color: #d1d5db;
}

.timer {
  position: absolute;
  margin: 10px;
  right: 2rem;
  background: #eee;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  z-index: 10;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.75);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-image-container {
  position: relative;
  display: inline-block;
}

.modal-image {
  max-width: 90vw;
  max-height: 90vh;
  border-radius: 12px;
  box-shadow: 0 0 12px #000;
  object-fit: contain;
}

.close-button {
  position: absolute;
  top: 8px;
  right: 8px;
  font-size: 24px;
  color: white;
  background-color: rgba(0, 0, 0, 0.6);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1001;
  transition: transform 0.15s ease;
}

.close-button:hover {
  transform: scale(1.2);
}

.confirm-modal {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
  max-width: 400px;
  text-align: center;
}

.modal-actions {
  margin-top: 1.5rem;
  display: flex;
  justify-content: center;
  gap: 1rem;
}
</style>
