<template>
  <div class="options-header">
    <div class="options-row">
      <div
          v-for="(option, index) in options"
          :key="index"
          class="question-option"
          :class="{ selected: index === selectedIndex }"
          @click="$emit('option-click', index)"
      >
        {{ option }}
      </div>
    </div>
    <div class="result-badge">{{ resultText }}</div>
  </div>
</template>

<script setup>
defineProps({
  options: Array,
  selectedIndex: Number,
  resultText: String
});

defineEmits(['option-click']);
</script>

<style scoped>
.options-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.options-row {
  display: flex;
  gap: 20px;
}

.question-option {
  padding: 12px 24px;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  font-family: "Inter", sans-serif;
  background-color: #e0e7ff;
  color: #333;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.question-option:hover {
  background-color: #c7d2fe;
}

.question-option.selected {
  background-color: #3b82f6;
  color: #fff;
}

.result-badge {
  font-size: 18px;
  font-weight: 600;
  color: #10b981;
  font-family: "Inter", sans-serif;
  white-space: nowrap;
}
</style>
