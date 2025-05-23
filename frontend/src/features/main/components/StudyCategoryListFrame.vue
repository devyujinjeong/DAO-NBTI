<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import StudyCard from './StudyCard.vue'
import DifficultyModal from "@/components/common/DifficultyModal.vue";
import LoginRequiredModal from "@/components/common/LoginRequiredModal.vue";

const router = useRouter()
const authStore = useAuthStore()

const isModalOpen = ref(false)
const isLoginModalOpen = ref(false)
const selectedCategory = ref(null)

const items = [
  { title: '언어 이해', description: '언어를 해석하고\n의미를 파악하는\n인지 능력' },
  { title: '시사 상식', description: '사회, 경제, 문화 등\n전반적인 분야의\n기초 지식' },
  { title: '지각 추론', description: '주어진 정보를 바탕으로\n논리적으로 판단하고\n결론을 도출하는 능력' },
  { title: '처리 속도', description: '정보를 빠르고 정확하게\n이해하고 판단하여\n처리하는 능력' },
  { title: '작업 기억', description: '작업 중 필요한 정보를\n일시적으로 기억하고\n활용하는 능력' },
  { title: '공간 지각력', description: '공간 속 사물의 위치나\n관계를 인식하고\n이해하는 능력' }
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
  router.push({
    path: '/study',
    query: {
      category: selectedCategory.value,
      level
    }
  })
  isModalOpen.value = false
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
</script>

<template>
  <div class="frame">
    <div class="heading">학습</div>
    <div class="card-container">
      <StudyCard
          v-for="item in items"
          :key="item.title"
          :title="item.title"
          :description="item.description"
          @click="onCardClick(item)"
      />
    </div>
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
</template>

<style scoped>
.frame {
  width: fit-content;
  padding: 20px;
  margin: 0 auto;
}

.heading {
  font-weight: bold;
  font-size: 19.2px;
  margin-bottom: 16px;
}

.card-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}
</style>
