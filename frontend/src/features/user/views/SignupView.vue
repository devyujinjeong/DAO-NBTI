<script setup>

import SmallModal from "@/components/common/SmallModal.vue";
import {reactive, ref} from "vue";
import SignupForm from "@/features/user/components/SignupForm.vue";
import {id_check, signup} from "@/features/user/api.js";
import router from "@/router/index.js";

const modalMessage = ref('');
const modalVisible = ref(false);
const isDuplicate = ref(false);
const isSignup = ref(false)
const form = reactive({
  accountId: '',
  password: '',
  name:'',
  gender:'',
  birthdate:''
})

const passwordCheck = ref('');
const insertable = ref(true);

const idCheck = async () =>{
  try {
    const response = await id_check(form.accountId);
    if (response.data.success === false) {
      throw new Error(response.data.message);
    }
    isDuplicate.value = response.data.data.duplicate;
    if(isDuplicate.value===false)
      insertable.value = false
    modalMessage.value = isDuplicate.value?'사용할 수 없는 아이디입니다.':'사용 가능한 아이디입니다.';
    modalVisible.value=true
  }catch(e){
    console.log('에러 메세지 : ', e)
  }

}

const signupRes = async () => {
  const requiredFields = ['accountId', 'password', 'name', 'gender', 'birthdate']

  const isEmpty = requiredFields.some(key => !form[key]?.trim())

  if (isEmpty) {
    modalMessage.value = '입력하지 않은 값이 있습니다.'
    modalVisible.value = true
    return
  }
  if(form.password!==passwordCheck.value){
    modalMessage.value = '비밀번호가 일치하지 않습니다.'
    modalVisible.value = true;
    return;
  }
  if(insertable.value){
    modalMessage.value = '아이디 중복검사를 해주세요.'
    modalVisible.value = true
    return
  }
  try{
    const response = await signup(form);
    if (response.data.success === false) {
      throw new Error(response.data.message);
    }
    modalMessage.value = '회원가입에 성공했습니다!'
    isSignup.value=true;
  }catch(e){
    console.log(e);
    modalMessage.value = e.response?.data?.message;
  }finally{
    modalVisible.value = true;
  }
}

const closeModal = async() => {
  modalVisible.value=false;
  if(isSignup.value)
    await router.push('/login')
}

</script>

<template>
<div class="signup-view">
  <SignupForm
      v-model:form = "form"
      v-model:passwordCheck = "passwordCheck"
      @idCheck = "idCheck"
      @signup = "signupRes"
      :insertable="insertable"
  />
</div>
  <small-modal
   :visible="modalVisible"
   @cancel = "closeModal"
  ><p>{{ modalMessage }}</p>
  </small-modal>
</template>

<style scoped>
.signup-view{
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  padding-top : 80px;
}
</style>