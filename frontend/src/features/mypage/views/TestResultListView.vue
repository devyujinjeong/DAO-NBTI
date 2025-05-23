<script setup>
import { ref, onMounted } from 'vue'
import {
  fetchTestResultList,
  fetchTestDetail
} from '@/features/mypage/api.js'
import ResultCard from '@/features/mypage/components/ResultCard.vue'
import RadarChart  from '@/features/mypage/components/RadarChart.vue'
import LineChart   from '@/features/mypage/components/LineChart.vue'
import Pagination  from '@/features/mypage/components/Pagination.vue'

// --- 필터/페이징 상태 ---
const year        = ref('')
const month       = ref('')
const currentPage = ref(1)
const pageSize    = ref(5)
const totalPage   = ref(1)
const totalItems  = ref(0)

// --- 로딩 상태 ---
const loadingList   = ref(false)
const loadingDetail = ref(false)

// --- 목록 데이터 ---
const resultList = ref([])

// --- 차트/AI 데이터 ---
const radarLabels = ref([])
const radarScores = ref([])
const aiText      = ref('')
const detailDate  = ref('')

// --- 라인 차트(총점 이력) 데이터 ---
const lineLabels = ref([])
const lineScores = ref([])

// 카테고리별 아이콘 맵
const categoryIcons = {
  '언어 이해':   new URL('@/assets/images/language_comprehension.png', import.meta.url).href,
  '시사 상식':   new URL('@/assets/images/common_sense.png', import.meta.url).href,
  '지각 추론':   new URL('@/assets/images/perceptual_reasoning.png', import.meta.url).href,
  '공간 지각력': new URL('@/assets/images/spatial_perception.png', import.meta.url).href,
  '작업 기억':   new URL('@/assets/images/work_memory.png', import.meta.url).href,
  '처리 속도':   new URL('@/assets/images/processing_speed.png', import.meta.url).href
}

/**
 * 최신 검사 결과 1건 로드 (필터 무시)
 */
async function loadLatestResult() {
  loadingDetail.value = true
  try {
    // 전체에서 최신 1개만 조회
    const res = await fetchTestResultList({ page: 0, size: 1 })
    const latest = res.data.data.content[0]
    if (latest) {
      await loadDetail(latest.testResultId)
      await loadScoreHistory()
    }
  } catch (err) {
    console.error('최신 결과 로드 실패', err)
  } finally {
    loadingDetail.value = false
  }
}

/**
 * 검사 결과 목록 로드 (연·월 필터 + 페이징)
 */
async function loadList() {
  loadingList.value = true
  try {
    const res = await fetchTestResultList({
      year:  year.value || undefined,
      month: month.value || undefined,
      page:  currentPage.value - 1,
      size:  pageSize.value
    })
    const { content, pagination } = res.data.data

    resultList.value  = content
    currentPage.value = pagination.currentPage
    totalPage.value   = pagination.totalPage
    totalItems.value  = pagination.totalItems
  } catch (err) {
    console.error('목록 로드 중 오류', err)
  } finally {
    loadingList.value = false
  }
}

/**
 * 상세(Radar + AI) 로드
 */
async function loadDetail(testResultId) {
  try {
    const res = await fetchTestDetail(testResultId)
    const { scores, aiText: text, createdAt } = res.data.data

    radarLabels.value = scores.map(s => s.categoryName)
    radarScores.value = scores.map(s => s.score)
    aiText.value      = text
    detailDate.value  = createdAt
  } catch (err) {
    console.error('상세 로드 중 오류', err)
  }
}

/**
 * 총점 이력(최신 10개) 로드 (전체 필터 무시)
 */
async function loadScoreHistory() {
  try {
    const res = await fetchTestResultList({ page: 0, size: 10 })
    let { content } = res.data.data

    // 내림차순 정렬 → 오름차순으로 뒤집어 차트에 표시
    content.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    const recent = content.reverse()

    lineLabels.value = recent.map(item =>
        new Date(item.createdAt)
            .toLocaleDateString('ko-KR', { month: '2-digit', day: '2-digit' })
    )
    lineScores.value = recent.map(item => item.totalScore)
  } catch (err) {
    console.error('차트용 총점 이력 로드 실패', err)
  }
}

// --- 이벤트 핸들러 ---
function onSearch() {
  currentPage.value = 1
  loadList()
}
function onPageChange(page) {
  currentPage.value = page
  loadList()
}

// --- 마운트 시 순서대로 로드 ---
onMounted(async () => {
  await loadLatestResult()
  await loadList()
})
</script>


<template>
  <main>
    <!-- 1) 최근 검사 결과 (Radar + AI) -->
    <section class="section">
      <h2 class="section-title">최근 검사 결과</h2>

      <div v-if="loadingDetail" class="loading">불러오는 중...</div>
      <div v-else class="card">
        <div class="chart-row">
          <RadarChart :labels="radarLabels" :data="radarScores" />
          <LineChart  :labels="lineLabels"  :data="lineScores"  />
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

        <div class="ai-summary-card">
          <p><strong>AI 분석 결과 ({{ detailDate.slice(0,10) }}):</strong></p>
          <p>{{ aiText }}</p>
        </div>
      </div>
    </section>

    <!-- 2) 검사 결과 목록 -->
    <section class="section">
      <h2 class="section-title">검사 결과 목록</h2>

      <div class="filter-bar">
        <label>년도
          <select v-model="year">
            <option value="">전체</option>
            <option value="2025">2025년</option>
            <option value="2024">2024년</option>
          </select>
        </label>
        <label>월
          <select v-model="month">
            <option value="">전체</option>
            <option v-for="m in 12" :key="m" :value="m">{{ m }}월</option>
          </select>
        </label>
        <button @click="onSearch">검색</button>
      </div>

      <div v-if="loadingList" class="loading">불러오는 중...</div>
      <div v-else-if="!resultList.length">검사 결과가 없습니다.</div>
      <div v-else>
        <ResultCard
            v-for="item in resultList"
            :key="item.testResultId"
            :testResultId="item.testResultId"
            :createdAt="item.createdAt"
            :highestCategory="item.highestCategory"
            :lowestCategory="item.lowestCategory"
            :totalScore="item.totalScore"
            @click="loadDetail(item.testResultId)"
        />
      </div>

      <Pagination
          :currentPage="currentPage"
          :pageSize="pageSize"
          :totalItems="totalItems"
          @update:currentPage="onPageChange"
      />
    </section>
  </main>
</template>

<style scoped>
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
  margin-bottom: 3rem;
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
.ai-summary-card {
  background: #f0f4ff;
  padding: 1.8rem;
  border-radius: 16px;
  border: 1px solid #dbeafe;
  font-size: 0.95rem;
  line-height: 1.6;
}
.filter-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin-bottom: 2rem;
  align-items: center;
}
.filter-bar label {
  font-size: 0.95rem;
}
.filter-bar select {
  padding: 0.4rem 0.6rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  background: #fff;
}
.filter-bar button {
  padding: 0.45rem 1.2rem;
  background: #3b82f6;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.loading {
  text-align: center;
  color: #666;
}
</style>
