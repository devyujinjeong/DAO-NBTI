<script setup>
import { ref, watch, nextTick } from 'vue'

const props = defineProps({
  visible: Boolean,
})
const emit = defineEmits(['confirm', 'cancel'])

const selectedLevel = ref('random')

watch(() => props.visible, (val) => {
  if (val) {
    selectedLevel.value = 'random'
    nextTick(() => {
      document.querySelector('.modal')?.focus()
    })
  }
})

function confirm() {
  emit('confirm', selectedLevel.value)
}
function cancel() {
  emit('cancel')
}
</script>

<template>
  <div
      v-if="visible"
      class="modal"
      tabindex="0"
      @keydown.enter="confirm"
      @keydown.esc="cancel"
  >
    <div class="modal-content">
      <p>난이도를 선택하세요</p>
      <select v-model="selectedLevel" class="level-select">
        <option value="random">무작위</option>
        <option value="1">1단계</option>
        <option value="2">2단계</option>
        <option value="3">3단계</option>
      </select>
      <div class="modal-buttons">
        <button class="confirm-btn" @click="confirm">시작</button>
        <button class="cancel-btn" @click="cancel">닫기</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal {
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  outline: none;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 12px;
  width: 500px;
  text-align: center;
}

.modal-content p {
  font-size: 16px;
  margin-bottom: 20px;
  text-align: center;
  white-space: pre-line;
  word-break: keep-all;
}

.level-select {
  font-size: 16px;
  padding: 8px;
  margin-bottom: 20px;
  width: 60%;
}

.modal-buttons {
  display: flex;
  justify-content: space-evenly;
}

.modal-buttons button {
  padding: 6px 16px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn,
.confirm-btn {
  border: none;
  background-color: #3B82F6;
  color: #fff;
}
</style>
