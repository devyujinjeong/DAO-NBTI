<script setup>
import {ref, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import api from '@/api/axios.js'
import {useToast} from "vue-toastification";

const route = useRoute()
const router = useRouter()
const toast= useToast()

const objectionId = route.params.objectionId
const objection = ref(null)
const isLoading = ref(true)
const isEditing = ref(false)

const fetchObjection = async () => {
  try {
    const response = await api.get(`/admin/objections/${objectionId}`)
    objection.value = response.data.data.objectionDetails;
  } catch (e) {
    toast.error('이의 제기 정보를 불러오지 못했습니다.')
    // console.error(e)
    router.push('/admin/objections')
  } finally {
    isLoading.value = false;
  }
}

const startEditing = () => {
  if (objection.value.status !== 'PENDING') {
    toast.error('이미 처리 완료된 이의 제기입니다.');
    return;
  }
  isEditing.value = true
}

const updateObjection = async () => {
  if (!validateRequest()) return;
  try {
    await api.put(`/admin/objections/${objectionId}`, {
      status: objection.value.status,
      information: objection.value.information,
    })
    toast.success('처리가 완료되었습니다.')
    isEditing.value = false
    router.push('/admin/objections')
  } catch (e) {
    toast.error('처리 중 오류가 발생했습니다.')
    // console.error(e)
  }
}

const validateRequest = () => {
  if (objection.value.status === 'PENDING') {
    toast.error('변경할 상태는 승인 또는 반려여야 합니다.');
    return false;
  }
  if (objection.value.status === 'REJECTED' && !objection.value.information) {
    toast.error('이의 제기 반려 시 처리 내용을 필수로 입력해야 합니다.');
    return false;
  }
  return true;
}

const cancelEditing = () => {
  isEditing.value = false
  fetchObjection()  // 원래 데이터 다시 불러오기
}

const goToProblem = () => {
  // 새 창에서 열기
  const url = `/admin/problems/${objection.value.problemId}`
  window.open(url, '_blank', 'noopener,noreferrer');
}

const goToList = () => {
  router.push(`/admin/objections`);
}

const formatDateTimeWithWeekday = (datetimeStr) => {
  const [datePart, timePart] = datetimeStr.split('T');
  const date = new Date(datePart);
  const weekdays = '일월화수목금토';
  const day = weekdays[date.getDay()];
  return `${datePart} (${day}) ${timePart}`;
}

onMounted(async () => {
  await fetchObjection()
});

</script>

<template>
  <main class="content">
    <section class="section">
      <template v-if="!isEditing">
        <h2>이의 제기 상세</h2>
      </template>
      <template v-else>
        <h2>이의 제기 상세 - 답변</h2>
      </template>

      <div class="card" v-if="!isLoading && objection">
        <div class="form-group">
          <label>이의 제기 ID</label>
          <input type="text" :value="objection.objectionId" disabled/>
        </div>

        <div class="form-group">
          <label>회원 ID</label>
          <input type="text" :value="objection.accountId" disabled/>
        </div>

        <div class="form-group">
          <label>문제 ID</label>
          <div class="form-row">
            <input type="text" :value="objection.problemId" disabled/>
            <button class="btn" @click="goToProblem">문제 확인</button>
          </div>
        </div>

        <div class="form-group">
          <label>제출 일시</label>
          <input type="text" :value="formatDateTimeWithWeekday(objection.createdAt)" disabled/>
        </div>

        <div class="form-group">
          <label>상세 내용</label>
          <textarea :value="objection.reason" disabled> </textarea>
        </div>

        <div class="form-group">
          <label>처리 상태</label>
          <select v-model="objection.status" :disabled="!isEditing">
            <option value="PENDING">대기</option>
            <option value="ACCEPTED">승인</option>
            <option value="REJECTED">반려</option>
          </select>
          <button class="btn" v-if="!isEditing" @click="startEditing">변경</button>
        </div>

        <div class="form-group">
          <label>처리 정보</label>
          <textarea v-model="objection.information" :disabled="!isEditing" placeholder="답변 내용을 입력하세요"> </textarea>
        </div>

        <div class="top-btn-group" v-if="isEditing">
          <button class="btn" @click="updateObjection">처리 완료</button>
          <button class="btn" @click="cancelEditing">취소</button>
        </div>
        <div class="top-btn-group" v-else>
          <button class="btn" @click="goToList">목록으로</button>
        </div>
      </div>
      <div v-else-if="isLoading" class="card">
        <div class="loading-overlay">
          <div class="spinner"/>
          <p>결과를 불러오는 중입니다...</p>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
.top-btn-group {
  display: flex;
  gap: 12px;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.flex {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-bottom: 1.5rem;
  margin-left: 20rem;
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

.form-row {
  display: flex;
  gap: 1rem;
}

.form-group {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.form-group label {
  font-weight: 500;
}

.form-group input[type="text"],
.form-group select,
.form-group input[type="file"] {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.form-group textarea {
  min-width: 20rem;
  min-height: 5rem;
}

.preview-box {
  width: 200px;
  height: 180px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f0f0f0;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #999;
  margin-bottom: 1rem;
}

/* readonly지만 disabled처럼 스타일 부여 */
.readonly-look {
  background-color: #f5f5f5; /* 회색 배경 */
  color: #777; /* 텍스트 색상 */
  cursor: not-allowed; /* 마우스 커서 */
  pointer-events: none; /* 클릭 막기 */
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #3b82f6;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-overlay {
  /* position: absolute; */
  top: 0;
  left: 0;
  z-index: 50;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
