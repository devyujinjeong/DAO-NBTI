<script setup>
import {ref, computed, onMounted} from 'vue'
import {useRoute} from 'vue-router'
import BigModal from '@/components/common/BigModal.vue'
import {fetchStudyDetail, submitObjection as apiSubmitObjection } from '@/features/mypage/api.js'
import { useToast } from 'vue-toastification'

const toast = useToast()
const route = useRoute()
const studyId = route.params.id

// 로딩 상태
const loading = ref(true)

// API에서 내려오는 원본 데이터
const detailRaw = ref({
  parentCategoryName: '',
  parentCategoryDescription: '',
  createdAt: '',
  problems: []
})

// 카테고리 아이콘 맵
const categoryIcons = {
  '언어 이해': new URL('@/assets/images/language_comprehension.png', import.meta.url).href,
  '시사 상식': new URL('@/assets/images/common_sense.png', import.meta.url).href,
  '지각 추론': new URL('@/assets/images/perceptual_reasoning.png', import.meta.url).href,
  '공간 지각력': new URL('@/assets/images/spatial_perception.png', import.meta.url).href,
  '작업 기억': new URL('@/assets/images/work_memory.png', import.meta.url).href,
  '처리 속도': new URL('@/assets/images/processing_speed.png', import.meta.url).href
}

// API 호출로 상세 로드
async function loadDetail() {
  loading.value = true
  try {
    const res = await fetchStudyDetail(studyId)
    const payload = res.data.data

    detailRaw.value = {
      parentCategoryName: payload.parentCategoryName,
      parentCategoryDescription: payload.parentCategoryDescription,
      createdAt: payload.createdAt,
      problems: (payload.problems || []).map((p, idx) => ({
        // 문제 ID나 인덱스 할당
        problemId: p.problemId,
        correct: p.correct,
        userAnswer: p.userAnswer,
        correctAnswer: p.correctAnswer,
        level: p.level,
        solvedAt: p.solvedAt,
        // T 제거한 값을 미리 계산해 두기
        solvedAtDisplay: p.solvedAt ? p.solvedAt.replace('T', ' ') : '',
        imageUrl: p.imageUrl || p.contentImageUrl
      }))
    }
  } catch (err) {
    console.error('학습 상세 로드 실패', err)
  } finally {
    loading.value = false
  }
}

// 정답 개수, 전체 개수
const totalCount = computed(() => detailRaw.value.problems.length)
const correctCount = computed(() =>
    detailRaw.value.problems.filter(p => p.correct).length
)

// 이의제기 모달
const objectionVisible = ref(false)
const selectedProblem = ref(null)
const objectionReason = ref('')

function openObjection(problem) {
  selectedProblem.value = problem
  objectionReason.value = ''
  objectionVisible.value = true
}

async function submitObjection() {
  const reason = objectionReason.value.trim()
  if (!reason) {
    toast.error('이의 사유를 입력해주세요.')
    return
  }

  try {
    // problem.problemId 는 이제 실제 DB PK 값이라고 가정
    const payload = {
      problemId: selectedProblem.value.problemId,
      reason
    }
    const res = await apiSubmitObjection(payload)

    // 성공 시 서버 메시지를 표시하고 모달 닫기
    toast.success('이의제기가 성공적으로 등록되었습니다.')
    objectionVisible.value = false

  } catch (err) {
    // validation 에러(400)나 기타 서버 메시지를 그대로 보여줍니다
    const serverMsg = err.response?.data?.message
    console.error('이의 제기 실패', err)
    toast.error('이의제기 등록에 실패했습니다. 다시 시도해주세요.')
  }
}
onMounted(loadDetail)
</script>

<template>
  <main>
    <section class="section">
      <h2 class="section-title">학습 상세 내역</h2>

      <div class="top-bar">
        <router-link to="/mypage/study" class="back-button">
          ← 학습 내역 목록으로
        </router-link>
        <div class="field-title">
          학습일: {{ detailRaw.createdAt.slice(0, 10) }}
        </div>
      </div>

      <div v-if="loading" class="loading">불러오는 중...</div>
      <div v-else class="card">
        <div class="summary-box">
          <div class="category">
            <img
                :src="categoryIcons[detailRaw.parentCategoryName]"
                alt=""
                class="category-icon"
            />
            <strong>분야:</strong> {{ detailRaw.parentCategoryName }}
          </div>
          <div>
            <strong>정답:</strong> {{ correctCount }} / {{ totalCount }}문항
          </div>
        </div>

        <ul class="problem-list">
          <li
              v-for="(problem, idx) in detailRaw.problems"
              :key="idx"
              class="problem-item"
          >
            <div class="problem-header">
              <div class="problem-title">문제 {{ idx + 1 }}</div>
              <button
                  class="objection-button"
                  @click="openObjection(problem)"
              >
                <img
                    src="@/assets/images/objection.svg"
                    alt="이의제기 아이콘"
                    class="objection-icon"
                />
              </button>
            </div>
            <div class="problem-content">
              <img
                  :src="problem.imageUrl"
                  alt="문제 이미지"
                  class="question-image"
              />
              <div class="problem-meta">
                <p
                    v-if="problem.content"
                    class="problem-text"
                >{{ problem.content }}</p>
                <div class="answer-content">
                  <div class="answer-meta">
                    <span>
                      <strong>난이도 : </strong> Lv. {{ problem.level }}
                    </span>
                  </div>
                  <div class="answer-meta">
                  <span>
                    <strong>제출 : </strong> {{ problem.userAnswer }}
                  </span>
                    <span>
                    <strong>정답 : </strong> {{ problem.correctAnswer }}
                  </span>
                  </div>
                </div>
                <div
                    :class="['answer-result', problem.correct ? 'correct' : 'wrong']"
                >
                  {{ problem.correct ? '정답입니다' : '오답입니다' }}
                </div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </section>

    <BigModal
        :visible="objectionVisible"
        confirm-visible
        confirm-text="제출"
        @cancel="objectionVisible = false"
        @confirm="submitObjection"
    >
      <template #default>
        <div class="modal-body">
          <h3>문제 이의제기</h3>
          <img
              :src="selectedProblem?.imageUrl"
              alt="문제 이미지"
              class="modal-image"
          />
          <textarea
              v-model="objectionReason"
              rows="5"
              placeholder="이 문제에 대해 이의가 있다면 사유를 작성해주세요."
          ></textarea>
        </div>
      </template>
    </BigModal>
  </main>
</template>

<style scoped>
.section {
  margin-bottom: 80px;
}

.section-title {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  font-weight: bold;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #ddd;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.back-button {
  background: #3b82f6;
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.9rem;
  text-decoration: none;
  transition: background 0.2s ease;
}

.back-button:hover {
  background: #1e3a8a;
}

.field-title {
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}

.loading {
  text-align: center;
  color: #666;
  padding: 2rem 0;
}

.card {
  background: #fff;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}

.summary-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 2rem;
  font-size: 1rem;
  font-weight: 500;
}

.problem-list {
  list-style: none;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.problem-item {
  border: 1px solid #e5e7eb;
  padding: 1.5rem;
  border-radius: 12px;
  background: #f9fafb;
}

.problem-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.problem-title {
  font-weight: 600;
  font-size: 1.1rem;
  color: #1e293b;
}

.problem-content {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
}

.question-image {
  width: 700px;
  height: auto;
  border: 1px solid #ddd;
  border-radius: 6px;
}

.problem-meta {
  flex: 1;
  font-size: 0.95rem;
  color: #333;
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
  align-items: flex-start;
  justify-content: space-evenly;
}

.problem-text {
  font-weight: 500;
}

.answer-content {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}


.answer-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  font-size: 0.9rem;
  color: #444;
}

.answer-result {
  font-weight: 600;
  margin-top: 0.5rem;
}

.answer-result.correct {
  color: #10b981;
}

.answer-result.wrong {
  color: #ef4444;
}

.objection-button {
  background: none;
  border: none;
  color: #888;
  cursor: pointer;
  font-size: 0.95rem;
}

.objection-button:hover {
  color: #1e3a8a;
  text-decoration: underline;
}

.category {
  display: flex;
  align-items: center;
  gap: 10px;
}

.category-icon {
  width: 60px;
  height: 60px;
  object-fit: contain;
}

/* 모달 내부 */
.modal-body {
  display: flex;
  flex-direction: column;
  text-align: left;
  gap : 1em;
  margin-bottom: 3em;
}

.modal-body h3 {
  font-size: 1.1rem;
  margin-bottom: 1rem;
}

.modal-image {
  width: 500px;
  margin-bottom: 1rem;
}

.modal-body textarea {
  resize: none;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
