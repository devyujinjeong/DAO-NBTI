<script setup>
import { ref, onMounted } from 'vue'
import { fetchStudyResults, fetchStudyCategories } from '@/features/mypage/api.js'
import Pagination from '@/features/mypage/components/Pagination.vue'

// --- 필터 상태 ---
const year  = ref('')
const month = ref('')
const parentCategoryId = ref('')

// --- 카테고리 목록 ---
const categories = ref([])

// --- 데이터 & 로딩 ---
const studyList = ref([])
const loading   = ref(false)

// --- 페이지네이션 상태 (서버 기반) ---
const currentPage = ref(1)    // 1-based
const pageSize    = ref(10)
const totalItems  = ref(0)

// --- 카테고리 아이콘 맵 (경로 조정) ---
const categoryIcons = {
  '언어 이해': new URL('@/assets/images/language_comprehension.png', import.meta.url).href,
  '시사 상식': new URL('@/assets/images/common_sense.png', import.meta.url).href,
  '지각 추론': new URL('@/assets/images/perceptual_reasoning.png', import.meta.url).href,
  '공간 지각력': new URL('@/assets/images/spatial_perception.png', import.meta.url).href,
  '작업 기억': new URL('@/assets/images/work_memory.png', import.meta.url).href,
  '처리 속도': new URL('@/assets/images/processing_speed.png', import.meta.url).href
}

// --- API 호출 함수 ---
async function loadCategories() {
  try {
    const res = await fetchStudyCategories()
    categories.value = res.data.data.categories
  } catch (e) {
    console.error('카테고리 로드 실패', e)
  }
}
async function fetchData() {
  console.log('>> fetchData params', {
    year: year.value,
    month: month.value,
    parentCategoryId: parentCategoryId.value
  })
  loading.value = true
  try {
    const res = await fetchStudyResults({
      year:  year.value || undefined,
      month: month.value || undefined,
      parentCategoryId: parentCategoryId.value ? Number(parentCategoryId.value) : undefined,
      page:  currentPage.value - 1,  // 0-based index
      size:  pageSize.value
    })
    const { content, pagination } = res.data.data
    studyList.value   = content
    currentPage.value = pagination.currentPage
    totalItems.value  = pagination.totalItems
  } catch (err) {
    console.error('학습 내역 로드 실패', err)
  } finally {
    loading.value = false
  }
}

// --- 검색 & 페이지 변경 핸들러 ---
function onSearch() {
  currentPage.value = 1
  fetchData()
}
function onPageChange(page) {
  currentPage.value = page
  fetchData()
}

// --- 컴포넌트 초기화 ---
onMounted(async () => {
  await loadCategories()
  await fetchData()
})
</script>

<template>
  <main>
    <section class="section">
      <h2 class="section-title">학습 내역 조회</h2>

      <!-- 필터 바 -->
      <div class="filter-bar" role="search">
        <label for="year">년도</label>
        <select v-model="year" id="year">
          <option value="">전체</option>
          <option value="2025">2025년</option>
          <option value="2024">2024년</option>
        </select>

        <label for="month">월</label>
        <select v-model="month" id="month">
          <option value="">전체</option>
          <option v-for="m in 12" :key="m" :value="m">{{ m }}월</option>
        </select>

        <label for="category">분야</label>
        <select v-model.number="parentCategoryId" id="category">
          <option value="">전체</option>
          <option
              v-for="cat in categories"
              :key="cat.categoryId"
              :value="cat.categoryId"
          >
            {{ cat.name }}
          </option>
        </select>

        <button @click="onSearch">검색</button>
      </div>

      <!-- 리스트 -->
      <div v-if="loading" class="loading">불러오는 중...</div>
      <div v-else-if="!studyList.length" class="empty">학습 내역이 없습니다.</div>
      <div v-else class="study-list">
        <article class="study-item" v-for="item in studyList" :key="item.studyId">
          <div class="study-item-inner">
            <img
                :src="categoryIcons[item.parentCategoryName]"
                alt=""
                class="category-icon"
            />
            <div class="study-content">
              <div class="study-meta">
                <div class="study-title">{{ item.parentCategoryName }}</div>
                <span>
                  정답 {{ item.correctCount }} / {{ item.totalCount }}문항
                </span>
              </div>
              <div class="study-btn">
                <span class="date">{{ item.createdAt.slice(0, 10) }}</span>
                <router-link
                    :to="`/mypage/study/${item.studyId}`"
                    class="btn-detail"
                    :aria-label="`${item.createdAt.slice(0,10)} 검사 상세 보기`"
                >
                  상세 보기
                </router-link>
              </div>
            </div>
          </div>
        </article>
      </div>

      <!-- 페이지네이션 -->
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
.filter-bar select,
.filter-bar button {
  padding: 0.4rem 0.6rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  background: #fff;
}
.filter-bar button {
  background: #3b82f6;
  color: #fff;
  border: none;
  cursor: pointer;
}
.study-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.study-item {
  background: #fff;
  padding: 1.2rem 1.5rem;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}
.study-item-inner {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.study-content {
  display: flex;
  justify-content: space-between;
  flex: 1;
}
.study-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}
.study-meta {
  display: flex;
  flex-direction: column;
  font-size: 0.9rem;
  color: #555;
}
.study-btn {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 0.9rem;
  color: #555;
}
.study-btn .date {
  color: #888;
}
.category-icon {
  width: 60px;
  height: 60px;
  object-fit: contain;
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
