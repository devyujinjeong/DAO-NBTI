<script setup>
import {computed, reactive, ref} from "vue";
import LoginForm from "@/features/user/components/LoginForm.vue";
import {useAuthStore} from "@/stores/auth.js";
import {useRouter} from "vue-router";
import {fetchResetPassword, loginUser} from "@/features/user/api.js";
import SmallModal from "@/components/common/SmallModal.vue";
import {storeToRefs} from "pinia";
import ResetPasswordForm from "@/features/user/components/ResetPasswordForm.vue";

const router = useRouter();
const authStore = useAuthStore()

const form = reactive({
  verifiedPassword: '',
  password: ''
})
const modalVisible = ref(false)
const modalMessage = ref('')
const loginSuccess = ref(false);


const resetPassword = async () => {
  console.log(form.verifiedPassword+" "+form.password);
  try {
    const response = await fetchResetPassword({
      verifiedPassword: form.verifiedPassword,
      password: form.password
    });

    if (response.data.success === false) {
      throw new Error(response.data.message);
    }

    authStore.clearAuth();
    modalMessage.value = "비밀번호 변경에 성공했습니다."
    loginSuccess.value=true
  }  catch (error) {
      modalMessage.value = '비밀번호 변경 실패'

  } finally {
    modalVisible.value = true;
  }
}

const closeModal = async() => {
  modalVisible.value=false;
  if(loginSuccess.value===true){
    await router.push('/login')
  }
}

</script>

<template>
  <div class = "find-view">
    <ResetPasswordForm
        v-model:verifiedPassword="form.verifiedPassword"
        v-model:password="form.password"
        @login="resetPassword"
        />

  </div>
  <small-modal
      :visible="modalVisible"
      @cancel="closeModal"
  ><p>{{ modalMessage }}</p>
  </small-modal>

</template>

<style>

.find-view{
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  padding-top : 80px;
}
 </style>