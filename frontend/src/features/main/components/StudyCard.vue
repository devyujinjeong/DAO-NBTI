<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: String,
  description: String
})

const emit = defineEmits(['click'])

// 카테고리별 아이콘 맵
const iconMap = {
  '언어 이해': new URL('@/assets/images/language_comprehension.png', import.meta.url).href,
  '시사 상식': new URL('@/assets/images/common_sense.png', import.meta.url).href,
  '지각 추론': new URL('@/assets/images/perceptual_reasoning.png', import.meta.url).href,
  '처리 속도': new URL('@/assets/images/processing_speed.png', import.meta.url).href,
  '작업 기억': new URL('@/assets/images/work_memory.png', import.meta.url).href,
  '공간 지각력': new URL('@/assets/images/spatial_perception.png', import.meta.url).href
}

const imageSrc = computed(() => iconMap[props.title] || '')
const formattedDescription = computed(() =>
    (props.description || '').replace(/\n/g, '<br />')
)
</script>

<template>
  <div class="study-card" @click="emit('click')">
    <img :src="imageSrc" alt="" class="study-icon" />
    <div class="text-block">
      <div class="study-title">{{ title }}</div>
      <div class="study-description" v-html="formattedDescription" />
    </div>
  </div>
</template>

<style scoped>
.study-card {
  background-color: #ffffff;
  border-radius: 20px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.06);
  border: 1px solid #e5e7eb;
  padding: 20px;
  display: flex;
  gap: 16px;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.study-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.08);
}
.study-icon {
  width: 64px;
  height: 64px;
  object-fit: contain;
  border-radius: 12px;
  background-color: #f1f5ff;
  padding: 6px;
}
.text-block {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.study-title {
  font-size: 1.15rem;
  font-weight: 700;
  color: #1e3a8a;
  margin-bottom: 8px;
}
.study-description {
  font-size: 0.95rem;
  color: #4b5563;
  line-height: 1.45;
}
</style>
