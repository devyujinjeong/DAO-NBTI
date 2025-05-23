<template>
  <div class="button-wrapper">
    <button class="main-button" @click="$emit('go-home')">메인으로</button>
  </div>
</template>

<script setup>
defineEmits(['go-home']);
</script>

<style scoped>
.button-wrapper {
  display: flex;
  justify-content: center;
  padding-top: 32px;
}
.main-button {
  background-color: #3b82f6;
  color: white;
  font-size: 18px;
  padding: 18px 32px;
  border-radius: 14px;
  font-family: "Inter", sans-serif;
  border: none;
  cursor: pointer;
}
</style>
