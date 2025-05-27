<script setup>

</script>

<template>
  <div class="loading-container">
    <div class="spinner" />
    <p>결과를 불러오는 중입니다...</p>
  </div>
</template>

<style scoped>
.loading-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: #fff;
  text-align: center;
  padding: 3rem 4rem;
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
</style>