<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from "@/api/axios.js"
import {useToast} from "vue-toastification";

const router = useRouter()
const toast = useToast();

const parentCategories = ref([]);
const categories = ref([]);
const problem = ref({
  problemId: '',
  parentCategory: '',
  categoryId: '',
  level: '',
  answerTypeId: '',
  correctAnswer: '',
  // contentImageUrl: ''  // 이 필드는 이제 안 씀
  imageFile: null  // 이미지 파일 저장용
});

const fetchCategories = async () => {
  const response = await api.get('/admin/categories')
  parentCategories.value = response.data.data.parentCategories;
  categories.value = response.data.data.childCategories;
}

const filteredCategories = computed(() => {
  if (!problem.value.parentCategory) return []
  return categories.value.filter(
      (child) => child.parentCategoryId === problem.value.parentCategory
  );
})

onMounted(() => {
  fetchCategories()
})

const cancelCreate = () => {
  router.push('/admin/problems')
}

const createProblem = async () => {
  if (!validateRequest()) return;

  // 문제 생성 요청 데이터 (이미지 파일 제외한 필드만)
  const problemCreateRequest = {
    categoryId: problem.value.categoryId,
    level: problem.value.level,
    answerTypeId: problem.value.answerTypeId,
    correctAnswer: problem.value.correctAnswer
  }

  if (!problem.value.imageFile) {
    toast.error('본문 사진을 등록하세요.');
    return;
  }

  const formData = new FormData();
  formData.append('problemCreateRequest', new Blob([JSON.stringify(problemCreateRequest)], { type: 'application/json' }));
  formData.append('imageFile', problem.value.imageFile);

  try {
    const response = await api.post('/admin/problems', formData, {
      headers: { "Content-Type": "multipart/form-data" }
    });
    const problemId = response.data.data.problem.problemId;
    toast.success(`문제가 성공적으로 등록되었습니다.`);
    router.push(`/admin/problems/${problemId}`);
  } catch (e) {
    toast.error('문제 등록에 실패했습니다.');
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
  if (!problem.value.imageFile) {
    toast.error('본문 사진을 등록하세요.');
    return false;
  }
  return true;
};

const uploadImage = (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // 이미지 URL을 서버에 업로드하지 않고 바로 저장하지 않고,
  // 파일 객체 자체를 저장
  problem.value.imageFile = file;

  // 미리보기용으로 URL 생성
  problem.value.contentImageUrl = URL.createObjectURL(file);
};
</script>

<template>
  <main class="content">
    <section class="section">
      <h2>문제 등록</h2>

      <div class="card">
        <!-- 상단 버튼 -->
        <div class="flex">
          <div class="top-btn-group">
            <button class="btn" @click="createProblem">등록</button>
            <button class="btn" @click="cancelCreate">취소</button>
          </div>
        </div>

        <!-- 폼 -->
        <form>
          <div class="form-group">
            <label for="problem-id">문제 번호</label>
            <input type="text" id="problem-id" v-model="problem.problemId" disabled/>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="category-parent">상위 분야</label>
              <select id="category-parent" v-model="problem.parentCategory">
                <option value="">선택</option>
                <option
                    v-for="parent in parentCategories"
                    :key="parent.categoryId"
                    :value="parent.categoryId"
                >
                  {{ parent.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label for="category-child">하위 분야</label>
              <select id="category-child" v-model="problem.categoryId">
                <option value="">선택</option>
                <option
                    v-for="child in filteredCategories"
                    :key="child.categoryId"
                    :value="child.categoryId"
                >
                  {{ child.name }}
                </option>
              </select>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="difficulty">난이도</label>
              <select id="difficulty" v-model="problem.level">
                <option value="">선택</option>
                <option value="1">레벨 1</option>
                <option value="2">레벨 2</option>
                <option value="3">레벨 3</option>
              </select>
            </div>
            <div class="form-group">
              <label for="answer-type">답안 유형</label>
              <select id="answer-type" v-model="problem.answerTypeId">
                <option value="">선택</option>
                <option value="1">선다형</option>
                <option value="2">단답형</option>
                <option value="3">다답형</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label for="image-upload">본문 이미지</label>
            <input type="file" id="image-upload" accept="image/*" @change="uploadImage"/>
          </div>

          <div class="preview-box">
            <img :src="problem.contentImageUrl" alt="이미지 없음" />
          </div>

          <div class="form-group">
            <label for="correct-answer">정답</label>
            <input type="text" id="correct-answer" v-model="problem.correctAnswer" />
          </div>
        </form>
      </div>
    </section>
  </main>
</template>

<style scoped>
/* 동일한 스타일 유지 */
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

.preview-box img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}
</style>
