<script setup>
defineProps({
  title: String,
  description: String
})

const emit = defineEmits(['click'])
</script>

<template>
  <div class="card" @click="emit('click')">
    <div class="strong">{{ title }}</div>
    <div class="text-wrapper">{{ description }}</div>
  </div>
</template>

<style scoped>
.card {
  cursor: pointer; /* 마우스 커서를 손가락으로 */
  background-color: #f1f3f5;
  border-radius: 12px;
  box-shadow: 0px 2px 6px #0000000d;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px;
  width: 156px;
  flex: 1 1 auto;
  transition: transform 0.2s ease, background-color 0.2s ease, box-shadow 0.2s ease;
}


.strong {
  font-weight: bold;
  color: #111;
  font-size: 16px;
}

.text-wrapper {
  color: #555;
  font-size: 14.4px;
}
</style>
