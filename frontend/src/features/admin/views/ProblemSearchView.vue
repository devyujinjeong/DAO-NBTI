<script setup>
import {ref, watch, onMounted, computed} from 'vue'
import api from "@/api/axios.js";
import {useRouter} from "vue-router";

const router = useRouter();
// 상태
const parentCategories = ref([]);
const categories = ref([]);
const problems = ref([]);
const filter = ref({
  parentCategory: '',
  category: '',
  level: '',
  page: 1,
  size: 10
});

const totalPages = ref(10);

const filteredCategories = computed(() => {
  if (!filter.value.parentCategory || !categories.value.length) return [];
  return categories.value.filter(
      (child) => child.parentCategoryId === filter.value.parentCategory
  );
});

watch(() => filter.value.parentCategory, () => {
  filter.value.category = ''
});

// 분야 불러오기
const fetchCategories = async () => {
  const response = await api.get('/admin/categories')
  parentCategories.value = response.data.data.parentCategories;
  categories.value = response.data.data.childCategories;
}

// 문제 목록 불러오기
const fetchProblems = async () => {
  const response = await api.get('/admin/problems/list', {
    params: {
      parentCategoryId: filter.value.parentCategory,
      childCategoryId: filter.value.category,
      level: filter.value.level,
      page: filter.value.page,
      size: filter.value.size,
    }
  })
  problems.value = response.data.data.problems;
  totalPages.value = response.data.data.pagination.totalPage;
}

// 최초 로딩 시 상위 분야 + 문제 목록 조회
onMounted(async () => {
  await fetchCategories()
  fetchProblems()
})

// 검색 버튼 클릭
const onSearch = () => {
  filter.value.page = 1  // 검색 시 1페이지로 초기화
  fetchProblems()
}

// 문제 등록 버튼 클릭
const onCreate = () => {
  router.push('/admin/problems/new');
}

// 문제 상세 페이지 이동
const goToDetail = (problemId) => {
  router.push({name: 'problem-details', params: {problemId}})
}

// 페이지 변경 처리
const changePage = (pageNum) => {
  if (pageNum < 1 || pageNum > totalPages.value) return;
  filter.value.page = pageNum;
  fetchProblems();
}

const paginationRange = computed(() => {
  const total = totalPages.value;
  const current = filter.value.page;

  // 10페이지 이하이면 전부 보여줌
  if (total <= 10) {
    return Array.from({length: total}, (_, i) => i + 1);
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
</script>


<template>
  <main class="content">
    <section class="section">
      <h2>문제 관리</h2>

      <!-- 필터 바 -->
      <div class="card">
        <div class="filter-bar">
          <label for="filter-parent-category">상위 분야</label>
          <select id="filter-parent-category" v-model="filter.parentCategory">
            <option value="">전체</option>
            <option v-for="item in parentCategories" :key="item.categoryId" :value="item.categoryId">{{
                item.name
              }}
            </option>
          </select>

          <label for="filter-category">하위 분야</label>
          <select id="filter-category" v-model="filter.category">
            <option value="">전체</option>
            <option v-for="item in filteredCategories" :key="item.categoryId" :value="item.categoryId">{{
                item.name
              }}
            </option>
          </select>

          <label for="filter-level">난이도</label>
          <select id="filter-level" v-model="filter.level">
            <option value="">전체</option>
            <option value="1">레벨 1</option>
            <option value="2">레벨 2</option>
            <option value="3">레벨 3</option>
          </select>

          <button class="btn" @click="onSearch">검색</button>
          <button class="btn" @click="onCreate">신규 문제 등록</button>
        </div>

        <table class="table">
          <thead>
          <tr>
            <th>문제 ID</th>
            <th>상위 분야</th>
            <th>하위 분야</th>
            <th>난이도</th>
            <th>답안 유형</th>
            <th>상세보기</th>
          </tr>
          </thead>
          <tbody>
          <!-- 문제 목록 렌더링 -->
          <tr v-for="problem in problems" :key="problem.problemId">
            <td>{{ problem.problemId }}</td>
            <td>{{ problem.parentCategoryName }}</td>
            <td>{{ problem.childCategoryName }}</td>
            <td>레벨 {{ problem.level }}</td>
            <td>{{ problem.answerTypeDescription }}</td>
            <td>
              <button class="btn" @click="goToDetail(problem.problemId)">상세보기</button>
            </td>
          </tr>

          <!-- 데이터가 없을 때 -->
          <tr v-if="problems.length === 0">
            <td colspan="6" style="color: #999;">조회된 문제가 없습니다.</td>
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
.card {
  background: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 1rem;
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.filter-bar select, .filter-bar input {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.filter-bar button {
  padding: 0.5rem 1rem;
  border: none;
  background: #007bff;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  background: #007bff;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.table th, .table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: center;
}

.form-group {
  display: flex; /* row가 기본 방향 */
  align-items: flex-start; /* 세로 중앙 정렬 */
  gap: 0.75rem; /* 요소 간 간격 */
  margin-bottom: 1.5rem;
}

.form-group label {
  margin-top: 0.5rem;
  font-weight: 500;
}

.form-group input[type="text"],
.form-group select, .form-group input[type="file"] {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  margin-top: 1rem;
}

.pagination button {
  padding: 0.4rem 0.8rem;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button.active {
  background: #007bff;
  color: #fff;
  border-color: #007bff;
}
</style>