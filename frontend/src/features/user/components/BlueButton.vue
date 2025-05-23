<script setup>
const props = defineProps({
  text: {
    type: String
  },
})

const emit = defineEmits(['click']);

function handleClick(){
  emit('click');
}

</script>

<template>
  <button class="button" @click="handleClick">
    {{text}}
  </button>
</template>

<style scoped>
.button {
  all: unset;
  align-items: center;
  align-self: stretch;
  background-color: #3b82f6;
  border-radius: 8px;
  box-sizing: border-box;
  display: flex;
  flex: 0 0 auto;
  justify-content: center;
  padding: 13px 12px 15px;
  position: relative;
  width: 100%;
  color : white;
}

.button:hover {
  background-color: #0056b3; /* 더 어두운 색 */
  cursor: pointer;
}
</style>