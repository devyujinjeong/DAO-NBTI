<template>
  <div class="content-area">
    <div class="question-image" @click="openModal = true">
      <img class="image" :src="imageUrl" alt="문제 이미지" />
    </div>

    <div class="question-info">
      <p><strong>난이도</strong>: {{ level }}</p>
      <p><strong>문제 정답</strong>: {{ correctAnswer }}</p>
      <p><strong>제출 답안</strong>: {{ submittedAnswer }}</p>
      <p><strong>정답 여부</strong>:
        <span :class="correct ? 'correct' : 'wrong'">
          {{ correct ? 'O' : 'X' }}
        </span>
      </p>
    </div>

    <!-- 이미지 모달 -->
    <div v-if="openModal" class="modal-backdrop" @click="closeModal">
      <div class="modal-wrapper" @click.stop>
        <div class="image-container">
          <button class="close-button" @click.stop="closeModal">×</button>
          <img class="modal-image" :src="imageUrl" alt="확대 이미지" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';

const props = defineProps({
  level: String,
  correctAnswer: String,
  submittedAnswer: String,
  correct: Boolean,
  imageUrl: String
});

const openModal = ref(false);

function closeModal() {
  openModal.value = false;
}

function handleEscape(e) {
  if (e.key === 'Escape') {
    closeModal();
  }
}

onMounted(() => {
  window.addEventListener('keydown', handleEscape);
});

onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleEscape);
});
</script>

<style scoped>
.content-area {
  display: flex;
  gap: 40px;
  justify-content: center;
  width: 100%;
  position: relative;
}

.question-image {
  background-color: #f9fafb;
  padding: 0;
  border-radius: 16px;
  width: 748px;
  height: auto;
  cursor: pointer;
  overflow: hidden;
}

.image {
  width: 100%;
  height: auto;
  border-radius: 16px;
  object-fit: cover;
  display: block;
}

.question-info {
  background-color: #f1f3f5;
  padding: 68px 32px;
  border-radius: 16px;
  width: 292px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  font-family: "Inter", sans-serif;
  font-size: 15px;
  color: #333;
}

.question-info strong {
  font-weight: 700;
}

.correct {
  color: #10b981;
  font-weight: bold;
}

.wrong {
  color: #ef4444;
  font-weight: bold;
}

/* 모달 스타일 */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-container {
  position: relative;
  display: inline-block;
}

.modal-image {
  max-width: 90vw;
  max-height: 90vh;
  border-radius: 12px;
  object-fit: contain;
  box-shadow: 0 0 10px #000;
}

.close-button {
  position: absolute;
  top: 8px;
  right: 8px;
  font-size: 24px;
  color: white;
  background-color: rgba(0, 0, 0, 0.6);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: transform 0.2s ease;
}

.close-button:hover {
  transform: scale(1.2);
}

</style>
