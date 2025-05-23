<script setup>

import UserInput from "@/features/user/components/UserInput.vue";
import {computed, reactive} from "vue";
import BlueButton from "@/features/user/components/BlueButton.vue";

const emit = defineEmits(['login','update:loginId', 'update:name'])

const props = defineProps({
  loginId: String,
  name: String
})

const loginIdModel = computed({
  get: () => props.loginId,
  set: val => emit('update:loginId', val)
})

const nameModel = computed({
  get: () => props.name,
  set: val => emit('update:name', val)
})

function check(){
  emit('check');
}

</script>

<template>
  <div class="section">
    <div class="heading">
      비밀번호 찾기
    </div>
    <div class="form">
      <UserInput
          v-model="loginIdModel"
          label="로그인 ID"
          placeholder="로그인 ID를 입력해주세요"/>
      <UserInput
          v-model="nameModel"
          label="이름"
          placeholder="이름을 입력해주세요"
      />

      <BlueButton
          text="비밀번호 재설정"
          @click="check"/>
    </div>
  </div>
</template>

<style scoped>
.section {
  align-items: center;
  background-color: #ffffff;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  max-width: 464px;
  padding: 51.91px 32px 47.99px;
  position: relative;
  width: 464px;
}

.section .form {
  align-items: center;
  align-self: stretch;
  display: flex;
  flex: 0 0 auto;
  flex-direction: column;
  gap: 19.2px;
  position: relative;
  width: 100%;
}

.section .heading {
  align-items: center;
  align-self: stretch;
  display: flex;
  flex: 0 0 auto;
  flex-direction: column;
  position: relative;
  width: 100%;
  font-size: x-large;
  font-weight : 700;
  color : #3b82f6;
}
</style>