<script setup>
import {ref, watch, onMounted, computed} from 'vue'
import api from "@/api/axios.js";
import {useRouter} from "vue-router";
import ObjectionDetailsView from "@/features/admin/views/ObjectionDetailsView.vue";

const router = useRouter();
// 상태
const objections = ref([])
const filter = ref({
  accountId: '',
  problemId: '',
  status: '',
  page: 1,
  size: 10,
})

const totalPages = ref(10);

// 이의 제기 목록 조회
const fetchObjections = async () => {
  try {
    const response = await api.get('/admin/objections', {
      params: {
        accountId: filter.value.accountId || null,
        problemId: filter.value.problemId,
        status: filter.value.status,
        page: filter.value.page,
        size: filter.value.size,
      },
    })
    objections.value = response.data.data.objections
    totalPages.value = response.data.data.pagination.totalPage
  } catch (e) {
    console.error(e)
  }
}

const goToDetail = (objectionId) => {
  router.push(`/admin/objections/${objectionId}`);
}

onMounted(async () => {
  await fetchObjections()
});

const onSearch = () => {
  filter.value.page = 1
  fetchObjections()
}

// 페이지 변경 처리
const changePage = (pageNum) => {
  if (pageNum < 1 || pageNum > totalPages.value) return;
  filter.value.page = pageNum;
  fetchObjections();
}

const paginationRange = computed(() => {
  const total = totalPages.value;
  const current = filter.value.page;

  // 10페이지 이하이면 전부 보여줌
  if (total <= 10) {
    return Array.from({ length: total }, (_, i) => i + 1);
  }

  // 항상 보여줄 페이지
  const fixedPages = [1, 2, total - 1, total];
  const aroundCurrent = [];

  // 현재 페이지 전후로 2개까지
  for (let i = current - 2; i <= current + 2; i++) {
    if (i > 0 && i <= total) {
      aroundCurrent.push(i);
    }
  }

  // 중복 제거 및 정렬
  const merged = Array.from(new Set([...fixedPages, ...aroundCurrent])).sort((a, b) => a - b);

  // '...' 삽입
  const result = [];
  for (let i = 0; i < merged.length; i++) {
    result.push(merged[i]);
    if (i < merged.length - 1 && merged[i + 1] > merged[i] + 1) {
      result.push('...');
    }
  }

  return result;
});

// datetime에 요일을 붙여주는 형태로 가공
const formatDateTimeWithWeekday = (datetimeStr) => {
  const [datePart, timePart] = datetimeStr.split('T'); // ['2025-05-20', '10:18:00']
  const date = new Date(datePart);
  const weekdays = '일월화수목금토';
  const day = weekdays[date.getDay()];
  return `${datePart} (${day}) ${timePart}`;
};
</script>


<template>
  <main class="content">
    <section class="section">
      <h2>이의제기 관리</h2>

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

          <label for="filter-problem">문제 ID</label>
          <input
              id="filter-problem"
              type="text"
              v-model="filter.problemId"
              placeholder="문제 ID 검색"
          />

          <label for="filter-status">상태</label>
          <select id="filter-status" v-model="filter.status">
            <option value="">전체</option>
            <option value="PENDING">대기</option>
            <option value="ACCEPTED">승인</option>
            <option value="REJECTED">반려</option>
          </select>

          <button class="btn" @click="onSearch">검색</button>
        </div>

        <table class="table">
          <thead>
          <tr>
            <th>회원 ID</th>
            <th>문제 ID</th>
            <th>제출 일시</th>
            <th>상태</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in objections" :key="item.objectionId">
            <td>{{ item.accountId }}</td>
            <td>{{ item.problemId }}</td>
            <td>{{ formatDateTimeWithWeekday(item.createdAt) }}</td>
            <td>
              {{ item.status === 'PENDING' ? '대기' : item.status === 'ACCEPTED' ? '승인' : '반려' }}
            </td>
            <td>
              <button class="btn" @click="goToDetail(item.objectionId)">상세보기</button>
            </td>
          </tr>

          <tr v-if="objections.length === 0">
            <td colspan="5" style="color: #999;">조회된 이의 제기가 없습니다.</td>
          </tr>
          </tbody>
        </table>

        <!-- 페이지네이션 -->
        <div class="pagination">
          <button @click="changePage(filter.page - 1)" :disabled="filter.page <= 1">&laquo;</button>

          <button
              v-for="pageNum in paginationRange"
              :key="pageNum"
              @click="typeof pageNum === 'number' && changePage(pageNum)"
              :class="{ active: filter.page === pageNum }"
              :disabled="pageNum === '...'"
          >
            {{ pageNum }}
          </button>

          <button @click="changePage(filter.page + 1)" :disabled="filter.page >= totalPages">&raquo;</button>
        </div>
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

.table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
.table th, .table td { border: 1px solid #ddd; padding: 0.75rem; text-align: center; }

.form-group {
  display: flex;           /* row가 기본 방향 */
  align-items: flex-start;     /* 세로 중앙 정렬 */
  gap: 0.75rem;            /* 요소 간 간격 */
  margin-bottom: 1.5rem;
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