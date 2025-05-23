<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {fetchStudyResult, fetchTestResultList} from "@/features/admin/api.js"
import PagingBar from "@/features/admin/components/PagingBar.vue";

const studyResult = ref([])
const totalItems = ref(0)
const totalPages = ref(0)
const filter = ref({
  userId: '',
  parentCategoryId: null,
  startDate:null,
  endDate: null,
  page: 1,
  size:10
})

const loadStudyResult = async () => {
  const queryParams = new URLSearchParams()
  try {
    if (filter.value.userId) queryParams.append('accountId', filter.value.userId)
    if (filter.value.parentCategoryId !== null) queryParams.append('parentCategoryId', filter.value.parentCategoryId)
    if (filter.value.startDate !== null) queryParams.append('startDate', filter.value.startDate)
    if (filter.value.endDate !== null) queryParams.append('endDate', filter.value.endDate)
    if (filter.value.page !== undefined) queryParams.append('page', filter.value.page)
    if (filter.value.size !== undefined) queryParams.append('size', filter.value.size)
    console.log(queryParams.toString())
    const response = await fetchStudyResult(queryParams.toString())
    const data = response.data.data
    console.log(data)
    studyResult.value = data.studies
    totalPages.value = data.pagination.totalPage
    totalItems.value = data.pagination.totalItems
    filter.value.page = data.pagination.currentPage // Spring의 page는 0-based
  } catch (e) {
    console.error(e)
  }
}

onMounted(async () => {
  await loadStudyResult()
})

const onSearch = () => {
  filter.value.page = 1
  loadStudyResult()
}

const changePage = (page) => {
  filter.value.page = page
  loadStudyResult()
}
</script>




<template>
  <main class="content">
    <section class="section">
      <h2>학습 목록 조회</h2>

      <!-- 필터 바 -->
      <div class="card">
        <div class="filter-bar">
          <label for="filter-member">회원 ID</label>
          <input
              id="filter-member"
              type="text"
              v-model="filter.accountId"
              placeholder="회원 ID 검색"
          />

          <label for="filter-year">연도</label>
          <input
              id="filter-year"
              type="number"
              v-model="filter.year"
              placeholder="예: 2025"
              min="2000"
              max="2100"
          />

          <label for="filter-month">월</label>
          <select id="filter-month" v-model="filter.month">
            <option :value="null">전체</option>
            <option v-for="m in 12" :key="m" :value="m">{{ m }}월</option>
          </select>

          <button class="btn" @click="onSearch">검색</button>
        </div>


        <!-- 사용자 목록 테이블 -->
        <table class="table">
          <thead>
          <tr>
            <th>회원 ID</th>
            <th>생성일</th>
            <th>학습 분야</th>
            <th>맞은 개수</th>
            <th>상세보기</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="studyResult in studyResult" :key="studyResult.studyId">
            <td>{{ studyResult.userId }}</td>
            <td>{{ studyResult.createdAt }}</td>
            <td>{{ studyResult.parentCategoryName }}</td>
            <td>{{ studyResult.correctCount }}</td>
            <td><RouterLink :to="`/admin/study/${studyResult.studyId}`">
              상세보기
            </RouterLink></td>

          </tr>

          <tr v-if="studyResult.length === 0">
            <td colspan="6" style="color: #999;">조회된 학습이 없습니다.</td>
          </tr>
          </tbody>
        </table>

        <PagingBar
            :current-page="filter.page"
            :total-pages="totalPages"
            :total-items="totalItems"
            @page-changed="changePage"
        />
      </div>
    </section>
  </main>
</template>



<style scoped>
.card { background: #fff; border-radius: 12px; padding: 1.5rem; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 1rem; }
.filter-bar { display: flex; align-items: center; gap: 1rem; margin-bottom: 1rem; }
.filter-bar select, .filter-bar input { padding: 0.5rem; border: 1px solid #ddd; border-radius: 8px; }
.filter-bar button { padding: 0.5rem 1rem; border: none; background: #007bff; color: #fff; border-radius: 8px; cursor: pointer; }

.btn { padding: 0.5rem 1rem; border: none; background: #007bff; color: #fff; border-radius: 8px; cursor: pointer; font-size: 0.9rem; }

.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  table-layout: fixed; /* 고정된 열 너비 */
}

.table th,
.table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: center;
  width: calc(100% / 5); /* 6개의 열이라면 각 열은 16.66% */
  word-wrap: break-word; /* 긴 내용 줄바꿈 */
}

.form-group label { margin-top: 0.5rem; font-weight: 500; }
.form-group input[type="text"],
.form-group select, .form-group input[type="file"] {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.pagination { display: flex; justify-content: center; align-items: center; gap: 0.5rem; margin-top: 1rem; }
.pagination button { padding: 0.4rem 0.8rem; border: 1px solid #ddd; background: #fff; border-radius: 4px; cursor: pointer; }
.pagination button.active { background: #007bff; color: #fff; border-color: #007bff; }
</style>