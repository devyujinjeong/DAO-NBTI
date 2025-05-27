<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import StudyCard from './StudyCard.vue'
import DifficultyModal from "@/components/common/DifficultyModal.vue"
import LoginRequiredModal from "@/components/common/LoginRequiredModal.vue"
import TestGuideModal from "@/components/common/TestGuideModal.vue"

const router = useRouter()
const authStore = useAuthStore()

// 모달 상태
const isModalOpen = ref(false)        // 난이도 선택 모달
const isLoginModalOpen = ref(false)   // 로그인 필요 안내 모달
const showGuideModal = ref(false)     // 문제 답안 작성 안내 모달

const selectedCategory = ref(null)
const selectedLevel = ref(0)

const items = [
  { title: '언어 이해', description: '언어를 해석하고 의미를 파악하는\n인지 능력' },
  { title: '시사 상식', description: '사회, 경제, 문화 등\n전반적인 분야의 기초 지식' },
  { title: '지각 추론', description: '주어진 정보를 바탕으로\n논리적으로 판단하고 결론을 도출하는 능력' },
  { title: '처리 속도', description: '정보를 빠르고 정확하게 이해하고 판단하여\n처리하는 능력' },
  { title: '작업 기억', description: '작업 중 필요한 정보를\n일시적으로 기억하고 활용하는 능력' },
  { title: '공간 지각력', description: '공간 속 사물의 위치나 관계를 인식하고\n이해하는 능력' }
]

function onCardClick(item) {
  if (!authStore.isAuthenticated) {
    isLoginModalOpen.value = true
    return
  }

  selectedCategory.value = item.title
  isModalOpen.value = true
}

function onModalConfirm(level) {
  selectedLevel.value = level
  isModalOpen.value = false
  showGuideModal.value = true
}

function onModalCancel() {
  isModalOpen.value = false
}

function onLoginConfirm() {
  isLoginModalOpen.value = false
  router.push('/login')
}

function onLoginCancel() {
  isLoginModalOpen.value = false
}

function onGuideCancel() {
  showGuideModal.value = false
}

function startStudy() {
  showGuideModal.value = false
  router.push({
    path: '/study',
    query: {
      category: selectedCategory.value,
      level: selectedLevel.value
    }
  })
}
</script>

<template>
  <div class="study-wrapper">
    <div class="study-heading">
      <h2 class="title">학습 영역 선택</h2>
      <p class="subtitle">강화하고 싶은 인지 영역을 선택하여 난이도를 설정하고 학습을 시작하세요.</p>
    </div>

    <div class="study-grid">
      <StudyCard
          v-for="item in items"
          :key="item.title"
          :title="item.title"
          :description="item.description"
          @click="onCardClick(item)"
      />
    </div>

    <DifficultyModal
        :visible="isModalOpen"
        @confirm="onModalConfirm"
        @cancel="onModalCancel"
    />

    <LoginRequiredModal
        :visible="isLoginModalOpen"
        @confirm="onLoginConfirm"
        @cancel="onLoginCancel"
    />

    <TestGuideModal
        :visible="showGuideModal"
        @confirm="startStudy"
        @cancel="onGuideCancel"
    />
  </div>
</template>

<style scoped>
.study-wrapper {
  max-width: 1024px;
  margin: 0 auto;
  padding: 40px 20px;
}

.study-heading {
  text-align: center;
  margin-bottom: 32px;
}

.title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #1e3a8a;
  margin-bottom: 0.5rem;
}

.subtitle {
  font-size: 1rem;
  color: #4b5563;
  line-height: 1.6;
}

.study-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(400px, 1fr));
  gap: 24px;
}
</style>
