<script setup>
import {computed, reactive, ref} from "vue";
import LoginForm from "@/features/user/components/LoginForm.vue";
import {useAuthStore} from "@/stores/auth.js";
import {useRouter, useRoute} from "vue-router";
import {loginUser} from "@/features/user/api.js";
import SmallModal from "@/components/common/SmallModal.vue";
import {storeToRefs} from "pinia";
const router = useRouter();
const route = useRoute()

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

const login = async () => {
  try {
    const response = await loginUser({
      accountId: form.loginId,
      password: form.password
    });
    const accessToken = response.data.data.accessToken;
    authStore.setAuth(accessToken);

    if(isUser.value)
        await router.push('/');
    else if(isAdmin.value)
        await router.push('/admin')

  }  catch (error) {
      modalVisible.value = true;
      modalMessage.value = '아이디 또는 비밀번호를 확인해주세요.'

  }
}

const closeModal = () => {
    modalVisible.value = false;
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