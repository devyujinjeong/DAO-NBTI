<script setup>
import { computed } from 'vue'
import { format } from 'date-fns'
import { ko } from 'date-fns/locale'

const props = defineProps({
  testResultId: { type: Number, required: true },
  createdAt: { type: String, required: true },
  highestCategory: { type: String, required: true },
  lowestCategory: { type: String, required: true },
  totalScore: { type: Number, required: true }
})

const formattedDate = computed(() =>
    format(new Date(props.createdAt), 'yyyy년 M월 d일', { locale: ko })
)
</script>

<template>
  <article class="result-row">
    <div class="info-left">
      <span class="badge badge-strong">강점: {{ highestCategory }}</span>
      <span class="badge badge-weak">약점: {{ lowestCategory }}</span>
    </div>

    <div class="score">
      {{ totalScore }}점
    </div>

    <div class="date">
      {{ formattedDate }}
    </div>

    <router-link
        :to="`/mypage/test/${testResultId}`"
        class="btn-detail"
        :aria-label="`${formattedDate} 검사 상세 보기`"
    >
      상세 보기
    </router-link>
  </article>
</template>

<style scoped>
.result-row {
  display: grid;
  grid-template-columns: 1fr 100px 140px 90px;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem 1.5rem;
  border-bottom: 1px solid #e5e7eb;
  background: #ffffff;
  font-family: 'Pretendard', sans-serif;
}

.info-left {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem;
}

.badge {
  padding: 0.35rem 0.9rem;
  font-size: 0.85rem;
  font-weight: 500;
  border-radius: 9999px;
  white-space: nowrap;
}

.badge-strong {
  background-color: #dbeafe;
  color: #1e3a8a;
}

.badge-weak {
  background-color: #fee2e2;
  color: #991b1b;
}

.score {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1e293b;
  text-align: right;
}

.date {
  font-size: 0.9rem;
  color: #6b7280;
  text-align: right;
}

.btn-detail {
  background: #3b82f6;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  text-decoration: none;
  transition: background 0.2s;
}
.btn-detail:hover {
  background: #1e40af;
}
.loading,
.empty {
  text-align: center;
  color: #666;
  padding: 2rem 0;
}
</style>
