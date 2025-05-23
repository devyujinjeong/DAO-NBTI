<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchTestDetail, fetchTestResultList } from '@/features/mypage/api.js'
import RadarChart from '@/features/mypage/components/RadarChart.vue'
import {fetchTestResultDetail} from "@/features/admin/api.js";

// 라우팅
const route         = useRoute()
const router        = useRouter()
const testResultId  = route.params.id

// 로딩 & 원본 데이터
const loading       = ref(true)
const detail        = ref({
  scores:       [],
  aiText:       '',
  createdAt:    ''
})

// 차트 데이터 바인딩
const radarLabels  = ref([])
const radarScores  = ref([])
const lineLabels   = ref([])
const lineScores   = ref([])

// 카테고리별 아이콘 맵
const categoryIcons = {
  '언어 이해':   new URL('@/assets/images/language_comprehension.png', import.meta.url).href,
  '시사 상식':   new URL('@/assets/images/common_sense.png', import.meta.url).href,
  '지각 추론':   new URL('@/assets/images/perceptual_reasoning.png', import.meta.url).href,
  '공간 지각력': new URL('@/assets/images/spatial_perception.png', import.meta.url).href,
  '작업 기억':   new URL('@/assets/images/work_memory.png', import.meta.url).href,
  '처리 속도':   new URL('@/assets/images/processing_speed.png', import.meta.url).href
}

// 데이터 로드
async function loadDetail() {
  loading.value = true
  try {

    // 1) 상세 정보
    const resDetail = await fetchTestResultDetail(testResultId)
    const payload   = resDetail.data.data
    detail.value    = payload

    // Radar 차트
    const rawScores = payload.scores || []
    radarLabels.value = rawScores.map(s => s.categoryName)
    radarScores.value = rawScores.map(s => s.score)

    // AI 텍스트, 날짜는 template에서 detail.aiText / detail.createdAt 사용

    // 2) 총점 이력(Line 차트) — 목록 API 호출
    const resList = await fetchTestResultList({ page: 0, size: 10 })
    const items   = resList.data.data.content || []
    items.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    const recent  = items.reverse()
    lineLabels.value = recent.map(i =>
        new Date(i.createdAt)
            .toLocaleDateString('ko-KR', { month:'2-digit', day:'2-digit' })
    )
    lineScores.value = recent.map(i => i.totalScore)

  } catch (err) {
    console.error('상세 정보 로드 실패', err)
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
}

// 마운트 시 호출
onMounted(loadDetail)
</script>


<template>
  <main>
    <section class="section">
      <h2 class="section-title">검사 상세 내역</h2>

      <div class="top-bar">
        <button @click="goBack" class="back-button">← 목록으로</button>
        <div class="field-title">검사일: {{ detail?.createdAt?.slice(0, 10) }}</div>
      </div>

      <div v-if="loading">불러오는 중...</div>
      <div v-else class="card">
        <!-- 차트 -->
        <div class="chart-row">
          <RadarChart :labels="radarLabels" :data="radarScores" :aspectRatio="1.5" />
        </div>

        <!-- 점수 그리드 -->
        <div class="grid">
          <div v-for="(label, index) in radarLabels" :key="label" class="grid-item">
            <img
                :src="categoryIcons[label]"
                alt=""
                class="category-icon"
            />
            <div class="score-content">
              <div class="score-header">
                <div class="category-title">{{ label }}</div>
                <div class="score-num">{{ radarScores[index] }}점</div>
              </div>
              <div class="score-bar">
                <div class="bar" :style="{ width: (radarScores[index] / 6 * 100) + '%' }"></div>
              </div>
              <div class="category-content">{{ label }} 설명 내용</div>
            </div>
          </div>
        </div>

        <!-- AI 분석 -->
        <div class="ai-summary-card">
          <p><strong>AI 분석 결과:</strong></p>
          <p>{{ detail.aiText    || '요약 정보 없음' }}</p>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
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
.section {
  margin-bottom: 80px;
}
.section-title {
  font-size: 1.4rem;
  margin-bottom: 1.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #ddd;
}
.card {
  background: #fff;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}
.chart-row {
  display: flex;
  gap: 2rem;
  margin-bottom: 2.5rem;
  justify-content: center;
}
.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(280px, 1fr));
  gap: 1.8rem;
  margin-bottom: 2rem;
}
.grid-item {
  background: #f9fafb;
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  gap: 15px;
}
.score-content {
  display: flex;
  flex-direction: column;
  width: 100%;
}
.score-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.75rem;
}
.category-title {
  font-weight: 600;
  color: #1e3a8a;
}
.score-num {
  color: #3b82f6;
  font-weight: 500;
}
.score-bar {
  height: 10px;
  background: #f1f5f9;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 0.75rem;
}
.score-bar .bar {
  height: 100%;
  background: linear-gradient(to right, #3b82f6, #93c5fd);
}
.category-content {
  font-size: 0.92rem;
  color: #555;
}
.ai-summary-card {
  background: #f0f4ff;
  padding: 1.8rem;
  border-radius: 16px;
  border: 1px solid #dbeafe;
  font-size: 0.95rem;
  line-height: 1.6;
}
.category-title {
  display: flex;
  align-items: center;
  font-size: 1.2em;
  font-weight: 600;
  color: #1e3a8a;
  gap: 0.4rem;
}
.category-icon {
  width: 100px;
  height: 100px;
  object-fit: contain;
}

</style>
