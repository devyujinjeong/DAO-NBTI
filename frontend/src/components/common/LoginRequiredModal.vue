<script setup>
import { watch, nextTick } from 'vue'
const props = defineProps({
  visible: Boolean
})
const emit = defineEmits(['confirm', 'cancel'])

watch(() => props.visible, (val) => {
  if (val) {
    nextTick(() => {
      document.querySelector('.modal')?.focus()
    })
  }
})

function confirm() {
  emit('confirm')
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
      <p>로그인이 필요한 서비스입니다.<br>로그인하시겠습니까?</p>
      <div class="modal-buttons">
        <button class="confirm-btn" @click="confirm">확인</button>
        <button class="cancel-btn" @click="cancel">취소</button>
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
  width: 400px;
  text-align: center;
}

.modal-buttons {
  display: flex;
  justify-content: space-evenly;
  margin-top: 20px;
}

.modal-buttons button {
  padding: 6px 16px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
  border: none;
  background-color: #3B82F6;
  color: #fff;
}
</style>
