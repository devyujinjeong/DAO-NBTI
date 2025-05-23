<script setup>

import {computed} from "vue";

const props = defineProps({
  modelValue: String,
  label: String,
  placeholder: String,
  type: {
    type: String,
    default: 'text'
  },
  insertable:{
    type:Boolean,
    default:true
  }
})

const emit = defineEmits(['update:modelValue'])

const inputValue = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
</script>

<template>
  <div class="container">
    <div class="label">
      <div class="text">{{label}}</div>
    </div>
    <input
        class="input"
        v-model="inputValue"
        :type="type"
        :placeholder="placeholder"
        :disabled="!insertable"
    />
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

.input {
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

.input:disabled {
  background-color: #f0f0f0; /* 연한 회색 배경 */
  color: #a0a0a0;            /* 텍스트도 회색 */
  cursor: not-allowed;
}

</style>