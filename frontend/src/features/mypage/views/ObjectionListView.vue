<script setup>
import BigModal from '@/components/common/BigModal.vue'
import Pagination from '@/features/mypage/components/Pagination.vue'
import { ref, onMounted } from 'vue'
import { fetchObjections, fetchObjectionDetail, fetchStudyCategories } from '@/features/mypage/api.js'

const objectionList = ref([])
const selectedObjection = ref(null)
const modalVisible = ref(false)

const filter = ref('')
const parentCategoryId = ref('')
const categoryOptions = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)

const statusLabel = {
  PENDING: '접수됨',
  ACCEPTED: '수용됨',
  REJECTED: '반려됨'
}

async function fetchList() {
  try {
    const { data } = await fetchObjections({
      status: filter.value || undefined,
      parentCategoryId: parentCategoryId.value || undefined,
      page: currentPage.value - 1,
      size: pageSize.value
    })
    objectionList.value = data.data?.content || []
    totalItems.value = data.data?.pagination?.totalItems || 0
  } catch (e) {
    console.error('이의제기 목록 조회 실패', e.response || e)
  }
}

async function openDetailModal(id) {
  try {
    const { data } = await fetchObjectionDetail(id)
    selectedObjection.value = {
      ...data.data,
      parentCategoryName: objectionList.value.find(o => o.objectionId === id)?.parentCategoryName || '-'
    }
    modalVisible.value = true
  } catch (e) {
    console.error('이의제기 상세 조회 실패', e.response || e)
  }
}

async function fetchCategories() {
  try {
    const { data } = await fetchStudyCategories()
    categoryOptions.value = data.data.categories || []
  } catch (e) {
    console.error('분야 목록 조회 실패', e.response || e)
  }
}

function onSearch() {
  currentPage.value = 1
  fetchList()
}

onMounted(() => {
  fetchCategories()
  fetchList()
})
</script>

<template>
  <main>
    <section class="section">
      <h2 class="section-title">이의제기 내역 조회</h2>

      <div class="filter-bar">
        <label for="status">상태</label>
        <select v-model="filter" id="status">
          <option value="">전체</option>
          <option value="PENDING">접수됨</option>
          <option value="ACCEPTED">수용됨</option>
          <option value="REJECTED">반려됨</option>
        </select>

        <label for="category">분야</label>
        <select v-model="parentCategoryId" id="category">
          <option value="">전체</option>
          <option v-for="cat in categoryOptions" :key="cat.categoryId" :value="cat.categoryId">
            {{ cat.name }}
          </option>
        </select>

        <button @click="onSearch">검색</button>
      </div>

      <table class="objection-table">
        <thead>
        <tr>
          <th>분야</th>
          <th>사유</th>
          <th>상태</th>
          <th>제출 일시</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in objectionList" :key="item.objectionId">
          <td>{{ item.parentCategoryName || '-' }}</td>
          <td>{{ item.reason || '-' }}</td>
          <td>
              <span
                  v-if="item.status"
                  class="status"
                  :class="item.status.toLowerCase()"
              >
                {{ statusLabel[item.status] || '알 수 없음' }}
              </span>
            <span v-else class="status unknown">알 수 없음</span>
          </td>
          <td>{{ item.createdAt?.replace('T', ' ').slice(0, 16) || '-' }}</td>
          <td>
            <button class="detail-button" @click="openDetailModal(item.objectionId)">
              상세 보기
            </button>
          </td>
        </tr>
        </tbody>
      </table>

      <Pagination
          :currentPage="currentPage"
          :pageSize="pageSize"
          :totalItems="totalItems"
          @update:currentPage="(p) => { currentPage.value = p; fetchList() }"
      />
    </section>

    <BigModal :visible="modalVisible" @cancel="modalVisible = false">
      <template #default>
        <div class="modal-detail">
          <div class="modal-header">
            <h3>제출한 이의제기 내용</h3>
            <span class="status" :class="selectedObjection?.status?.toLowerCase() || 'unknown'">
              {{ statusLabel[selectedObjection?.status] || '알 수 없음' }}
            </span>
          </div>
          <div class="modal-meta">
            <div><strong>분야</strong> {{ selectedObjection?.parentCategoryName || '-' }}</div>
            <div><strong>제출일시</strong>
              {{ selectedObjection?.createdAt?.replace('T', ' ').slice(0, 16) || '-' }}
            </div>
          </div>
          <div class="modal-reason">
            <p class="label">이의제기 사유</p>
            <p class="value">{{ selectedObjection?.reason || '-' }}</p>
          </div>
        </div>
      </template>
    </BigModal>
  </main>
</template>


<style scoped>
.section {
  margin-bottom: 80px;
}
.section-title {
  font-size: 1.4rem;
  margin-bottom: 1rem;
  font-weight: bold;
  border-bottom: 2px solid #ddd;
  padding-bottom: 0.5rem;
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
.objection-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}
.objection-table th {
  background: #f3f4f6;
  text-align: left;
  padding: 0.75rem;
  font-size: 0.95rem;
  color: #374151;
}
.objection-table td {
  padding: 0.75rem;
  border-top: 1px solid #eee;
  font-size: 0.95rem;
}
.status {
  padding: 0.35rem 0.75rem;
  border-radius: 999px;
  font-size: 0.82rem;
  font-weight: 500;
  display: inline-block;
}
.status.pending {
  background: #e0f3ff;
  color: var(--color-main, #3b82f6);
}
.status.accepted {
  background: #e8f5e9;
  color: var(--color-success, #2e7d32);
}
.status.rejected {
  background: #fdecea;
  color: var(--color-error, #d32f2f);
}
.detail-button {
  background: #3b82f6;
  color: #fff;
  padding: 0.4rem 0.8rem;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
}
.detail-button:hover {
  background: #1e40af;
}
.modal-detail {
  text-align: left;
  font-size: 0.95rem;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  margin-bottom: 20px;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.modal-meta {
  display: flex;
  justify-content: space-between;
  font-weight: 500;
  background: #f5f7fa;
  padding: 0.75rem 1rem;
  border-radius: 8px;
}
.modal-reason {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 1rem;
}
.modal-reason .label {
  font-weight: bold;
  margin-bottom: 0.5rem;
}
.modal-reason .value {
  line-height: 1.6;
  white-space: pre-wrap;
}
</style>
