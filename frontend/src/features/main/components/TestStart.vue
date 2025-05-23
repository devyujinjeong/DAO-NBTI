<script setup>
import {computed, onMounted, ref} from 'vue'
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth.js";
import {getUserPoints} from "@/features/test/api.js";
import BigModal from "@/components/common/BigModal.vue";

// 경로 이동을 위한 부분
const router = useRouter();
const authStore = useAuthStore()

// 맛보기 검사, 정식 검사 여부 확인
const isFormalTest = computed(() => authStore.isAuthenticated)
// 회원이면 18문제, 비회원이면 6문제
const totalProblems = computed(() => authStore.isAuthenticated ? 18 : 6)
// 회원의 포인트 내역
const userPoint = ref(0);

const modalVisible = ref(false)
const modalMessage = ref('')
const showConfirmButton = ref(true)
const confirmButtonText = ref('계속')

// 회원의 포인트 불러오는 api
onMounted(async () => {
    if (isFormalTest.value) {
        try {
            const res = await getUserPoints();
            console.log(res.data.data);
            userPoint.value = res.data.data;
            console.log(userPoint.value);
        } catch (err) {
            console.error('포인트 불러오기 실패:', err);
        }
    }
})

function handleStartClick() {
    modalVisible.value = true

    if (isFormalTest.value) {
        if (userPoint.value < 5) {
            modalMessage.value =
                `<strong>[정식 검사]</strong><br>포인트가 부족해 검사를 진행할 수 없습니다.<br><br><strong>잔여 포인트</strong><br>${userPoint.value} point`
            showConfirmButton.value = false
        } else {
            modalMessage.value =
                '<strong>[정식 검사]</strong><br>검사 중간에 종료하는 경우 포인트는 차감되지 않지만<br>이 페이지로 다시 돌아올 수 없습니다.'
            showConfirmButton.value = true
        }
    } else {
        modalMessage.value =
            '<strong>[맛보기 검사]</strong><br>검사 중간에 종료하는 경우<br>다시 현재 페이지로 돌아올 수 없습니다.'
        showConfirmButton.value = true
    }
}

function closeModal() {
    modalVisible.value = false
}

// 테스트 진행하기
function proceedTest() {
    router.push('/test');
}
</script>

<template>
    <div class="container">
        <h2>인지 능력 검사</h2>
        <p>
            {{ totalProblems }}문제의 간단한 두뇌 테스트를 통해<br />
            당신의 인지 능력을 확인해보세요.
        </p>
        <button class="btn" @click="handleStartClick">검사 시작하기</button>
    </div>

    <BigModal
        :visible="modalVisible"
        :confirm-visible="showConfirmButton"
        :confirm-text="confirmButtonText"
        @confirm="proceedTest"
        @cancel="closeModal"
    >
        <template #default>
            <p v-html="modalMessage  " />
        </template>
    </BigModal>
</template>

<style scoped>
.container {
    min-width: 500px;
    margin: 4rem auto;
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
    padding: 3rem 2rem;
    text-align: center;
}

.container h2 {
    font-size: 1.8rem;
    font-weight: 600;
    margin-bottom: 0.5rem;
}

.container p {
    color: #666;
    font-size: 1rem;
    margin-bottom: 2rem;
    line-height: 1.5;
}

.btn {
    display: inline-block;
    padding: 0.75rem 1.5rem;
    background: #3b82f6;
    color: #fff;
    border-radius: 12px;
    font-weight: 500;
    text-align: center;
    width: 100%;
    box-sizing: border-box;
    transition: all 0.2s ease;
    cursor: pointer;
    border: none;
    font-size: 16px;
}

.btn:hover {
    background: #1e3a8a;
}

.btn:active {
    transform: scale(0.98);
    filter: brightness(0.95);
}
</style>
