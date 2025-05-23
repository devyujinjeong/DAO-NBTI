<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
        <div class="avatar"></div>
        <div class="greeting">
          <p class="name">{{ userInfo.name }}님</p>
          <p class="message">반갑습니다! 아래는 현재 등록된 회원정보입니다.</p>
        </div>
      </div>
      <div class="card-body">
        <div class="info-row">
          <span class="label">아이디</span>
          <span class="value">{{ userInfo.accountId }}</span>
        </div>
        <div class="info-row">
          <span class="label">생년월일</span>
          <span class="value">{{ userInfo.birthdate }}</span>
        </div>
        <div class="info-row">
          <span class="label">성별</span>
          <span class="value">{{ userInfo.gender }}</span>
        </div>
        <div class="info-row">
          <span class="label">보유 포인트</span>
          <span class="value">{{ userInfo.point }}</span>
        </div>
      </div>
    </div>
  </div>
  <small-modal
      :visible="false"
      @cancel="closeModal"
  >
    <p>
      {{modalMessage}}
    </p>
  </small-modal>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {fetchUserInfo} from "@/features/mypage/api.js";
import SmallModal from "@/components/common/SmallModal.vue";
import router from "@/router/index.js";

const userInfo = ref({
  name: '',
  accountId: '',
  birthdate: '',
  gender: '',
  point: ''
})
const modalVisible = ref(false);
const modalMessage = ref('');

const closeModal = async () => {
  modalVisible.value=false;
  await router.push('/')
}

async function loadUserInfo(){
  try{
    const response = await fetchUserInfo();
    if (response.data.success === false) {
      throw new Error(response.data.message);
    }
    userInfo.value = response.data.data;
  }catch(e){
    modalMessage.value='접근할 수 없습니다.'
    modalVisible.value=true;
  }
}



onMounted(loadUserInfo)
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  padding-top: 80px;
  background-color: #f9f9f9;
  min-height: 100vh;
  box-sizing: border-box;
}

.card {
  background-color: white;
  width: 100%;
  height : fit-content;
  max-width: 600px;
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.avatar {
  width: 64px;
  height: 64px;
  background-color: #ddd;
  border-radius: 50%;
  margin-right: 16px;
}

.greeting .name {
  font-size: 20px;
  font-weight: bold;
}

.greeting .message {
  font-size: 14px;
  color: #666;
  margin-top: 4px;
}

.card-body {
  border-top: 1px solid #eee;
  padding-top: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
}

.label {
  color: #666;
  font-size: 15px;
}

.value {
  font-weight: 500;
  font-size: 15px;
}
</style>
