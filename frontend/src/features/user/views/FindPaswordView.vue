<script setup>
import {computed, reactive, ref} from "vue";
import LoginForm from "@/features/user/components/LoginForm.vue";
import {useAuthStore} from "@/stores/auth.js";
import {useRouter} from "vue-router";
import {fetchFindPassword, loginUser} from "@/features/user/api.js";
import SmallModal from "@/components/common/SmallModal.vue";
import {storeToRefs} from "pinia";
import FindPasswordForm from "@/features/user/components/FindPasswordForm.vue";

const router = useRouter();
const authStore = useAuthStore()
const { isAuthenticated, userRole } = storeToRefs(authStore)
const isUser = computed(() => isAuthenticated.value && userRole.value === 'USER')
const isAdmin = computed(() => isAuthenticated.value && userRole.value === 'ADMIN')
const form = reactive({
  loginId: '',
  name: ''
})
const modalVisible = ref(false)
const modalMessage = ref('')
const authSuccess = ref(false);


const findPassword = async () => {
  try {
    const response = await fetchFindPassword({
      accountId: form.loginId,
      name: form.name
    });
    const accessToken = response.data.data.accessToken;
    authStore.setTempAuth(accessToken);
    modalMessage.value = "인증에 성공했습니다."
    authSuccess.value=true
  }  catch (error) {
    modalMessage.value = '일치하는 계정이 없습니다.'

  } finally {
    modalVisible.value = true;
  }
}

const closeModal = async() => {
  modalVisible.value=false;
  if(authSuccess.value === true)
    await router.push('/reset-password');
}

</script>

<template>
  <div class = "find-view">
    <FindPasswordForm
        v-model:loginId="form.loginId"
        v-model:name="form.name"
        @check="findPassword"
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