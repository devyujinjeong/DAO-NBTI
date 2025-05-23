<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { fetchUserList } from "@/features/admin/api.js"
import PagingBar from "@/features/admin/components/PagingBar.vue";

const users = ref([])
const totalItems = ref(0)
const totalPages = ref(0)
const filter = ref({
  accountId: '',
  isDeleted: null,
  page: 1,
  size: 10,
})




const fetchUsers = async () => {
  try {
    const queryParams = new URLSearchParams()

    if (filter.value.accountId) queryParams.append('accountId', filter.value.accountId)
    if (filter.value.isDeleted !== null) queryParams.append('isDeleted', filter.value.isDeleted)
    if (filter.value.page !== undefined) queryParams.append('page', filter.value.page-1)
    if (filter.value.size !== undefined) queryParams.append('size', filter.value.size)
    console.log(queryParams.toString())
    const response = await fetchUserList(queryParams.toString())
    const data = response.data.data
    console.log(data)
    users.value = data.page.content
    totalPages.value = data.page.totalPages
    //filter.value.size = data.page.numberOfElements
    totalItems.value = data.page.totalElements
    filter.value.page = data.page.number // Spring의 page는 0-based
  } catch (e) {
    console.error(e)
  }
}

onMounted(async () => {
  await fetchUsers()
})

const onSearch = () => {
  filter.value.page = 1
  fetchUsers()
}

const changePage = (page) => {
  filter.value.page = page
  fetchUsers()
}
</script>




<template>
  <main class="content">
    <section class="section">
      <h2>회원 조회</h2>

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

          <label for="filter-deleted">탈퇴 여부</label>
          <select id="filter-deleted" v-model="filter.isDeleted">
            <option :value="null">전체</option>
            <option :value="'N'">정상</option>
            <option :value="'Y'">탈퇴</option>
          </select>

          <button class="btn" @click="onSearch">검색</button>
        </div>

        <!-- 사용자 목록 테이블 -->
        <table class="table">
          <thead>
          <tr>
            <th>회원 ID</th>
            <th>이름</th>
            <th>성별</th>
            <th>생일</th>
            <th>포인트</th>
            <th>탈퇴 여부</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="user in users" :key="user.accountId">
            <td>{{ user.accountId }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.gender }}</td>
            <td>{{ user.birthdate }}</td>
            <td>{{ user.point ?? 0 }}</td>
            <td>{{ user.isDeleted==="Y" ? '탈퇴' : '정상' }}</td>
          </tr>

          <tr v-if="users.length === 0">
            <td colspan="6" style="color: #999;">조회된 회원이 없습니다.</td>
          </tr>
          </tbody>
        </table>

        <PagingBar
            :current-page="filter.page+1"
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
  width: calc(100% / 6); /* 6개의 열이라면 각 열은 16.66% */
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