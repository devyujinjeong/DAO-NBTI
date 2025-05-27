<template>
  <div class="header">
    <div class="container">
      <template v-if="!isAdmin">
        <RouterLink to="/">
          <div class="text-wrapper">
            <img class="logo" :src="logo" alt="두뇌 트레이닝 로고"/>NBTI
          </div>
        </RouterLink>
      </template>
      <template v-else>
        <RouterLink to="/admin">
          <div class="text-wrapper">
            <img class="logo" :src="logo" alt="두뇌 트레이닝 로고"/>NBTI
          </div>
        </RouterLink>
      </template>
    </div>

  <div class="navbar">
    <template v-if="!isAdmin">
      <RouterLink to="/" class="link">검사 및 학습</RouterLink>
    </template>
    <template v-if="isUser">
      <RouterLink to="/mypage/test" class="link">마이페이지</RouterLink>
    </template>
    <template v-if="isAuthenticated">
      <div class="button" @click="handleLogout">로그아웃</div>
    </template>
    <template v-else>
      <RouterLink to="/login" class="link">로그인</RouterLink>
      <RouterLink to="/signup" class="div">회원가입</RouterLink>
    </template>
  </div>
  </div>

  <small-modal :visible="modalVisible" @cancel="closeModal">
    <p>
      {{ modalMessage }}
    </p>

  </small-modal>
</template>

<script setup>
import {useAuthStore} from "@/stores/auth.js";
import {useRouter} from "vue-router";
import logo from "@/assets/images/logo.png";
import {logoutUser} from "@/features/user/api.js";
import SmallModal from "@/components/common/SmallModal.vue";
import {computed, ref} from "vue";
import {storeToRefs} from "pinia";

const router = useRouter()
const auth = useAuthStore()
const {isAuthenticated, userRole} = storeToRefs(auth)
const isUser = computed(() => isAuthenticated.value && userRole.value === 'USER')
const isAdmin = computed(() => isAuthenticated.value && userRole.value === 'ADMIN')
const modalVisible = ref(false);
const modalMessage = ref("로그아웃 되었습니다.")

const handleLogout = async () => {
  try {
    await logoutUser()
  } catch (e) {
    console.error('로그아웃 API 실패', e)
  }
  modalVisible.value = true;
  auth.clearAuth();
}

const closeModal = async () => {

  modalVisible.value = false;
  await router.push('/')
}

</script>

<style>
.header {
  align-items: center;
  background-color: #ffffff;
  justify-content: space-between; /* 핵심 */
  border-bottom-style: solid;
  border-bottom-width: 1px;
  border-color: #dddddd;
  display: flex;
  gap: 601.86px;
  padding: 16px 32px 17px;
  position: relative;
}

a{
  text-decoration: none;
}

.header .container {
  align-items: flex-start;
  display: inline-flex;
  flex: 0 0 auto;
  flex-direction: column;
  position: relative;
}

.header .logo {
  height: 40px;
  width: auto;
  object-fit: contain;
}

.header .text-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #007bff;
  font-size: 22.5px;
  font-weight: 700;
  letter-spacing: 0;
  line-height: normal;
  margin-top: -1.00px;
  position: relative;
  white-space: nowrap;
  width: fit-content;
}

.header .navbar {
  align-items: flex-start;
  display: inline-flex;
  flex: 0 0 auto;
  gap: 16px;
  margin-right: -8.86px;
  padding: 0px 0px 0px 16px;
  position: relative;
}

.header .link {
  color: #333333;
  font-size: 16px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: normal;
  margin-top: -1.00px;
  position: relative;
  white-space: nowrap;
  width: fit-content;
}

.header .div {
  color: #333333;
  margin-top: -1.00px;
  position: relative;
  white-space: nowrap;
  width: fit-content;
}

.button {
  /* text-decoration: underline;*/ /* 밑줄 */
  cursor: pointer; /* 마우스를 올리면 포인터로 변경 */
  color: #333333; /* 선택적으로 링크 스타일 색상 */
}
</style>
