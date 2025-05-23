<script setup>
import { computed } from 'vue'

const props = defineProps({
  gender: String
})

const emit = defineEmits(['update:gender'])

const genderModel = computed({
  get: () => props.gender,
  set: value => emit('update:gender', value)
})
</script>

<template>
  <div class="container">
    <div class="label">
      <div class="text">성별</div>
    </div>
    <select class="gender-form" v-model="genderModel">
      <option value="M">남</option>
      <option value="F">여</option>
    </select>
  </div>
</template>


<style scoped>
.container {
  align-items: flex-start;
  align-self: stretch;
  display: flex;
  flex: 0 0 auto;
  flex-direction: column;
  gap: 8px;
  position: relative;
  width: 100%;
}

.label {
  align-items: flex-start;
  align-self: stretch;
  display: flex;
  flex: 0 0 auto;
  flex-direction: column;
  position: relative;
  width: 100%;
}
.gender-form {
  background-color: #ffffff;
  border: 1px solid #cccccc;
  border-radius: 8px;
  padding: 12px 13px 11px;
  width: 100%;
  box-sizing: border-box;
  display: block; /* 또는 생략 */
}
.text {
  align-self: stretch;
  color: #757575;
  margin-top: -1.00px;
  position: relative;
}
</style>