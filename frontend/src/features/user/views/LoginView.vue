<script setup>
import {computed, reactive, ref} from "vue";
import LoginForm from "@/features/user/components/LoginForm.vue";
import {useAuthStore} from "@/stores/auth.js";
import {useRouter} from "vue-router";
import {loginUser} from "@/features/user/api.js";
import SmallModal from "@/components/common/SmallModal.vue";
import {storeToRefs} from "pinia";

const router = useRouter();
const authStore = useAuthStore()
const { isAuthenticated, userRole } = storeToRefs(authStore)
const isUser = computed(() =>isAuthenticated.value && userRole.value === 'USER')
const isAdmin = computed(() =>isAuthenticated.value && userRole.value === 'ADMIN')
const form = reactive({
  loginId: '',
  password: ''
})
const modalVisible = ref(false)
const modalMessage = ref('')
const loginSuccess = ref(false);


const login = async () => {
  console.log(`${form.loginId} ${form.password}`);
  try {
    const response = await loginUser({
      accountId: form.loginId,
      password: form.password
    });

    if (response.data.success === false) {
      throw new Error(response.data.message);
    }

    console.log('로그인 성공', response.data)
    const accessToken = response.data.data.accessToken;
    console.log(`토큰 ${accessToken}`)
    authStore.setAuth(accessToken);
    modalMessage.value = "로그인에 성공했습니다."
    loginSuccess.value=true
  }  catch (error) {
    modalMessage.value = error.response?.data?.message;

  } finally {
    modalVisible.value = true;
  }
}

const closeModal = async() => {
  modalVisible.value=false;
  if(loginSuccess.value===true){
    console.log('권한 : '+isUser.value)
    if(isUser.value)
      await router.push('/');
    else if(isAdmin.value)
      await router.push('/admin')
  }
}

</script>

<template>
  <div class = "find-view">
    <LoginForm
        v-model:loginId="form.loginId"
        v-model:password="form.password"
        @login="login"
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