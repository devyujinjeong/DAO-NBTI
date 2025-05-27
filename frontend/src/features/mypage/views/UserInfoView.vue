<template>
  <div class="container">
    <h2 class="section-title">개인 정보 조회</h2>
    <div class ="card-section">
      <div class="card">
        <div class="card-header">
          <div class="avatar"><img src="@/assets/images/profile.png" alt="프로필 이미지" class="profile-image" /></div>
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
            <span class="value">{{ genderLabel }}</span>
          </div>
          <div class="info-row">
            <span class="label">보유 포인트</span>
            <span class="value">{{ userInfo.point }}</span>
          </div>
        </div>
      </div>
      <div>
        <button class="delete-button" @click="onClickDelete">회원 탈퇴</button>
      </div>
    </div>
  </div>
  <small-modal
      :visible="modalVisible"
      :confirmVisible="confirmVisible"
      @cancel="closeModal"
      @confirm="showSubmitModal"
  >
    <br>
    정말 탈퇴하시겠습니까?<br> 탈퇴를 위해서 비밀번호를 입력해주세요
        <UserInput
            v-model="form.password"
            :placeholder = "'비밀번호를 입력해주세요'"
            :type="'password'"
        ></UserInput>
        <br>
  </small-modal>
  <small-modal
        :visible="resultModalVisible"
        @cancel="closeModal"
  >
    <br>
    {{resultModalMessage}}
    <br>
  </small-modal>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue'
import {fetchUserInfo, fetchUserWithdraw} from "@/features/mypage/api.js";
import SmallModal from "@/components/common/SmallModal.vue";
import router from "@/router/index.js";
import UserInput from "@/features/user/components/UserInput.vue";
import {useAuthStore} from "@/stores/auth.js";

const userInfo = ref({
  name: '',
  accountId: '',
  birthdate: '',
  gender: '',
  point: ''
})
const modalVisible = ref(false);
const confirmVisible = ref(true);
const modalMessage = ref('');
const form = ref({
  password : ''
})

const resultModalVisible =ref(false);
const resultModalMessage = ref('');
const isDeleted = ref(false);

const closeModal = async () => {
  modalVisible.value=false;
  resultModalVisible.value=false;

  if(isDeleted.value){
    useAuthStore().clearAuth();
    await router.push('/')
  }
}
const genderLabel = computed(() => {
  if (userInfo.value.gender === 'M') return '남성';
  if (userInfo.value.gender === 'F') return '여성';
  return userInfo.value.gender || ''; // 혹시 값이 없거나 예외적인 경우 대비
});


const showSubmitModal = async () => {
  modalVisible.value = false;
  try{
    await fetchUserWithdraw(form.value.password);
    resultModalMessage.value = '탈퇴에 성공했습니다.';
    isDeleted.value = true
  }catch(e){
    resultModalMessage.value= '비밀번호가 일치하지 않습니다.';
  }
  resultModalVisible.value = true

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

const onClickDelete = async () =>{
    modalVisible.value=true;
}

onMounted(loadUserInfo)
</script>

<style scoped>
.container {
  justify-content: center;
  box-sizing: border-box;
}

.card-section{
  justify-items: center;
  margin-top : 80px;
}

.section-title {
  font-size: 1.4rem;
  margin-bottom: 1rem;
  font-weight: bold;
  border-bottom: 2px solid #ddd;
  padding-bottom: 0.5rem;
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
  background-color: #f8f9fa;
  border-radius: 50%;
  margin-right: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.profile-image {
  width: 60%;
  height: 60%;
  object-fit: cover;
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

.delete-button {
  background-color: #f44336;
  color: white;
  padding: 8px 16px;
  margin-top : 50px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #d32f2f;
}
</style>
