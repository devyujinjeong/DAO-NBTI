<script setup>

import UserInput from "@/features/user/components/UserInput.vue";
import GenderForm from "@/features/user/components/GenderForm.vue";
import BlueButton from "@/features/user/components/BlueButton.vue";
import {computed, ref} from "vue";


const props = defineProps({
  form:Object,
  passwordCheck:String,
  insertable:Boolean
})

const emit = defineEmits([
    'update:accountId',
  'update:password',
  'update:name',
  'update:gender',
  'update:birthdate',
    'update:passwordCheck',
    'idCheck',
    'signup'
])

const models = {
  accountId: useModel('accountId'),
  password: useModel('password'),
  name: useModel('name'),
  gender: useModel('gender'),
  birthdate: useModel('birthdate'),
}
const passwordCheckModel =computed({
  get: () => props.passwordCheck,
  set: val => emit('update:passwordCheck', val)
})

function useModel(key) {
  return computed({
    get: () => props.form[key],
    set: val => emit(`update:${key}`, val)
  })
}

const idCheck = async () => {
  console.log("idCheck")
  if (props.form.accountId?.trim()) {
    emit("idCheck")
  }
}

const signup = async  () =>{
  emit('signup');
}

</script>

<template>
<div class = "section">
  <div class="heading">
    회원가입
  </div>
  <div class = "id-form">
    <UserInput
        v-model="props.form.accountId"
        label="로그인 ID"
        :insertable="insertable"
        placeholder="로그인 ID를 입력해주세요"/>
    <input class = "id-check-button" type="button" value = "중복 확인" @click="idCheck">
  </div>

  <UserInput
      v-model="props.form.password"
      label="비밀번호"
      placeholder="비밀번호를 입력해주세요"
      type = "password"/>
  <UserInput
      v-model="passwordCheckModel"
      label="비밀번호 확인"
      placeholder="비밀번호를 입력해주세요"
      type = "password"/>
  <UserInput
      v-model="props.form.name"
      label="이름"
      placeholder="이름를 입력해주세요"/>
    <GenderForm
      v-model:gender="props.form.gender"
    />
  <UserInput
      v-model="props.form.birthdate"
      label="생일"
      type="date"/>
  <BlueButton
      text="회원가입"
      @click="signup"/>
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

.id-form{
  width : 100%;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap : 8px;
}
.id-check-button{
  height:50%;
  background-color: #3b82f6;
  border-radius: 8px;
  border: 1px solid #cccccc;
  padding: 8px;
  color : white;
}
.id-check-button:hover {
  background-color: #0056b3; /* 더 어두운 색 */
  cursor: pointer;
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
  color: #3b82f6;
}



</style>