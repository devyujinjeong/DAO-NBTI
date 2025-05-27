<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import api from "@/api/axios.js";
import { useRouter, useRoute } from "vue-router";
import SmallModal from "@/components/common/SmallModal.vue";
import {useToast} from "vue-toastification";

const router = useRouter()
const route = useRoute()
const toast = useToast();

const isEditMode = ref(false)
const parentCategories = ref([]);
const categories = ref([]);
const problem = ref({})
const showDeleteModal = ref(false)
const showCancelEditModal = ref(false)

const selectedFile = ref(null)  // 새로 선택된 이미지 파일 보관용

const fetchCategories = async () => {
  const response = await api.get('/admin/categories')
  parentCategories.value = response.data.data.parentCategories;
  categories.value = response.data.data.childCategories;
}

const findParentCategoryId = (categoryId) => {
  const child = categories.value.find((c) => c.categoryId === categoryId)
  return child?.parentCategoryId || ''
}

const filteredCategories = computed(() => {
  if (!problem.value.parentCategory) return []
  return categories.value.filter(
      (child) => child.parentCategoryId === problem.value.parentCategory
  );
});

const fetchProblem = async () => {
  try {
    const problemId = route.params.problemId;
    const res = await api.get(`/admin/problems/${problemId}`)
    const data = res.data.data.problem
    problem.value = {
      ...data,
      parentCategory: findParentCategoryId(data.categoryId)
    }
    selectedFile.value = null;  // 초기화
  } catch (e) {
    if (e.status === 404) {
      toast.error('존재하지 않는 문제입니다.');
    } else {
      toast.error('문제 정보를 불러오지 못했습니다.');
    }
    router.push('/admin/problems');
  }
}

onMounted(async () => {
  await fetchCategories();
  await fetchProblem();
});

watch(
    () => problem.value.parentCategory,
    (newVal, oldVal) => {
      if (oldVal !== undefined && newVal !== oldVal) {
        problem.value.categoryId = '';
      }
    },
    { immediate: false }
);

function startEdit() {
  isEditMode.value = true
}

function onDeleteClick() {
  showDeleteModal.value = true
}

const onConfirmDelete = async () => {
  showDeleteModal.value = false
  try {
    await api.delete(`/admin/problems/${problem.value.problemId}`)
    toast.success('삭제가 완료되었습니다.')
    router.replace('/admin/problems')
  } catch (error) {
    toast.error('삭제 중 오류가 발생했습니다.')
  }
}

const onCancelDelete = () => {
  showDeleteModal.value = false
}

const goToList = () => {
  router.push('/admin/problems');
}

const cancelEdit = () => {
  showCancelEditModal.value = true;
}

const onCancelEdit = () => {
  showCancelEditModal.value = false
  isEditMode.value = false;
  fetchProblem();
}

// 수정 완료 (multipart/form-data 로 전송)
const completeEdit = async () => {
  if (!validateRequest()) return;

  try {
    const formData = new FormData();

    // JSON 부분 Blob으로 추가
    const problemUpdateRequest = {
      categoryId: problem.value.categoryId,
      level: problem.value.level,
      answerTypeId: problem.value.answerTypeId,
      correctAnswer: problem.value.correctAnswer,
      contentImageUrl: problem.value.contentImageUrl // 기존 URL 유지
    };
    const jsonBlob = new Blob([JSON.stringify(problemUpdateRequest)], { type: "application/json" });
    formData.append("problemUpdateRequest", jsonBlob);

    // 새로 선택한 이미지가 있으면 첨부
    if (selectedFile.value) {
      formData.append("imageFile", selectedFile.value);
    }

    await api.put(`/admin/problems/${problem.value.problemId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    toast.success('수정이 완료되었습니다.');
    isEditMode.value = false;
    selectedFile.value = null;
    await fetchProblem();
  } catch (e) {
    toast.error('수정 실패했습니다.');
    // console.error(e);
  }
}

const validateRequest = () => {
  if (!problem.value.categoryId) {
    toast.error('분야를 선택하세요.');
    return false;
  }
  if (!problem.value.level) {
    toast.error('난이도를 선택하세요.');
    return false;
  }
  if (!problem.value.answerTypeId) {
    toast.error('답안 유형을 선택하세요.');
    return false;
  }
  if (!problem.value.correctAnswer) {
    toast.error('정답을 입력하세요.');
    return false;
  }
  if (!problem.value.contentImageUrl) {
    toast.error('본문 사진을 등록하세요.');
    return false;
  }
  return true;
};

// 이미지 선택 시 새 파일 저장 및 미리보기 처리
const uploadImage = (event) => {
  const file = event.target.files[0];
  if (!file) return;

  selectedFile.value = file;
  problem.value.contentImageUrl = URL.createObjectURL(file); // 미리보기용 임시 URL
};
</script>

<template>
  <main class="content">
    <section class="section">
      <template v-if="!isEditMode">
        <h2>문제 상세 조회</h2>
      </template>
      <template v-else>
        <h2>문제 상세 조회 - 수정</h2>
      </template>

      <div class="card">
        <div class="flex">
          <div class="top-btn-group">
            <template v-if="!isEditMode">
              <button class="btn" @click="startEdit">수정하기</button>
              <button class="btn" @click="onDeleteClick">삭제</button>
              <button class="btn" @click="goToList">목록으로</button>
            </template>
            <template v-else>
              <button class="btn" @click="completeEdit">수정 완료</button>
              <button class="btn" @click="onDeleteClick">삭제</button>
              <button class="btn" @click="cancelEdit">취소</button>
            </template>
          </div>
        </div>

        <form>
          <div class="form-group" style="margin-bottom:1.5rem;">
            <label for="problem-id">문제 번호</label>
            <input type="text" id="problem-id" :value="problem.problemId" readonly :class="{ 'readonly-look': !isEditMode }" />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="category-parent">상위 분야</label>
              <select id="category-parent" :disabled="!isEditMode" v-model="problem.parentCategory" :class="{ 'readonly-look': !isEditMode }">
                <option value="">선택</option>
                <option v-for="parent in parentCategories" :key="parent.categoryId" :value="parent.categoryId">{{ parent.name }}</option>
              </select>
            </div>
            <div class="form-group">
              <label for="category-child">하위 분야</label>
              <select id="category-child" :disabled="!isEditMode" v-model="problem.categoryId" :class="{ 'readonly-look': !isEditMode }">
                <option value="">선택</option>
                <option v-for="child in filteredCategories" :key="child.categoryId" :value="child.categoryId">{{ child.name }}</option>
              </select>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="difficulty">난이도</label>
              <select id="difficulty" :disabled="!isEditMode" v-model="problem.level" :class="{ 'readonly-look': !isEditMode }">
                <option value="">선택</option>
                <option value="1">레벨 1</option>
                <option value="2">레벨 2</option>
                <option value="3">레벨 3</option>
              </select>
            </div>
            <div class="form-group">
              <label for="answer-type">답안 유형</label>
              <select id="answer-type" :disabled="!isEditMode" v-model="problem.answerTypeId" :class="{ 'readonly-look': !isEditMode }">
                <option value="">선택</option>
                <option value="1">선다형</option>
                <option value="2">단답형</option>
                <option value="3">다답형</option>
              </select>
            </div>
          </div>

          <div class="form-group" style="margin-bottom:1.5rem;">
            <label for="image-upload">본문 이미지</label>
            <input
                type="file"
                id="image-upload"
                accept="image/*"
                :disabled="!isEditMode"
                :class="{ 'readonly-look': !isEditMode }"
                @change="uploadImage"
            />
          </div>

          <div class="preview-box">
            <img :src="problem.contentImageUrl" alt="이미지 없음" />
          </div>

          <div class="form-group">
            <label for="correct-answer">정답</label>
            <input type="text" id="correct-answer" v-model="problem.correctAnswer" :readonly="!isEditMode" :class="{ 'readonly-look': !isEditMode }" />
          </div>
        </form>
      </div>
    </section>

    <SmallModal
        :visible="showDeleteModal"
        :confirmVisible="true"
        confirmText="예"
        cancelText="아니오"
        @confirm="onConfirmDelete"
        @cancel="onCancelDelete"
        title="삭제 확인"
    >
      <p>정말 삭제하시겠습니까?</p>
    </SmallModal>
    <SmallModal
        :visible="showCancelEditModal"
        :confirmVisible="true"
        confirmText="예"
        cancelText="아니오"
        @confirm="onCancelEdit"
        @cancel="() => (showCancelEditModal = false)"
        title="수정 취소 확인"
    >
      <p>수정을 취소하시겠습니까? 입력한 내용이 저장되지 않습니다.</p>
    </SmallModal>
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
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
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

.readonly-look {
  background-color: #f5f5f5;
  color: #777;
  cursor: not-allowed;
  pointer-events: none;
}

.preview-box img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.readonly-look {
  background-color: #eee;
  cursor: default;
}
.preview-box img {
  max-width: 100%;
  max-height: 300px;
  display: block;
  margin-top: 1rem;
}
</style>
