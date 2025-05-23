<script setup>
import {ref, onMounted, watchEffect, computed} from 'vue'
import {getProblems, submitAnswers} from "@/features/test/api.js";
import {useRouter} from "vue-router";
import {useToast} from "vue-toastification";

/* 포인트가 없는 경우 접근을 제한하기 위한 부분 */
const router = useRouter();
const toast = useToast();

/* 문제와 관련된 const */
const problems = ref([]); // 문제를 담는 배열
const currentProblemIndex = ref(0); // 현재 문제 index
const totalProblems = computed(() => problems.value.length); // 전체 문제 수
const testResultId = ref(0);
const isLoading = ref(false);

/* 문제 진행률 const */
const percentage = ref(0);

/* 제한 시간 관련 const */
const time = ref(0);
const timerDisplay = ref('0');
let interval;

/* 문제 채점을 위해 넘겨줘야 하는 값들*/
const answers = ref([]); // 문제 전체 정답을 저장하는 배열
const userAnswer = ref(''); // 문제 하나의 정답
const guestId = ref(null); // 비회원을 위한 key

/* 문제를 가져오는 api*/
async function fetchProblems() {
    try {
        const res = await getProblems();

        problems.value = res.data.data.problemList; // 문제 리스트 가져오기
        guestId.value = res.data.data.guestId; // guestId 가져오기

        console.log(problems.value);
        console.log(guestId.value);

        currentProblemIndex.value = 0;

        startTimer(); // 타이머 시작
    } catch (e) {
        const errorCode = e.response?.data?.errorCode

        // 포인트가 없는데 test로 경로를 입력해서 접근하려는 경우 접근 제한하기
        if (errorCode === '30004') {
            toast.error('포인트가 부족해 검사를 실행할 수 없습니다!')
            await router.replace('/')
            return
        }
        console.error('문제 불러오기 실패:', e)
    }
}

/* 시간 제한을 위한 함수 */
function startTimer() {
    if (interval) clearInterval(interval)

    const current = problems.value[currentProblemIndex.value]
    time.value = current?.timeLimit ?? 60 // 실제 시간
    timerDisplay.value = time.value.toString(); // 화면에서 보여지는 부분을 설정하기 위함

    interval = setInterval(() => {
        if (time.value <= 0) {
            clearInterval(interval)
            timerDisplay.value = '0'

            goToNextProblem(); // 시간이 끝나면 다음 문제로 바로 넘어가기
        } else {
            time.value--
            timerDisplay.value = time.value.toString()
        }
    }, 1000)
}

/* 다음 문제로 넘어가는 함수 */
function goToNextProblem() {
    // 다음 문제로 넘어가기 전에 현재 시점의 정답을 저장함
    saveCurrentAnswer();

    if (currentProblemIndex.value < totalProblems.value - 1) {
        currentProblemIndex.value++
        userAnswer.value = ''; // 만약 정답을 적지 않는다면 빈 값이 넘어감
        startTimer(); // 타이머 시작하기
    } else {
        // 마지막 문제라면 자동 제출하게 설정
        submitAllAnswers();
        console.log('검사 종료!')
    }
}

/* 변경을 감지하기 위한 watchEffect*/
watchEffect(() => {
    percentage.value =
        totalProblems.value > 0
            ? ((currentProblemIndex.value + 1) / totalProblems.value) * 100
            : 0
})

/* 현재 문제의 답을 저장하는 함수 */
function saveCurrentAnswer() {
    const currentProblem = problems.value[currentProblemIndex.value]; // 현재 문제의 id를 가져오기 위해 선언
    answers.value.push({
        problemId: currentProblem.problemId, // 현재 문제의 아이디
        answer: userAnswer.value // 현재 문제의 정답
    })
}

/* 문제를 채점하는 api */
async function submitAllAnswers() {

    saveCurrentAnswer();

    if (interval) clearInterval(interval);

    isLoading.value = true

    try {
        const payload = {
            guestId: guestId.value,
            answers: answers.value
        }

        console.log(payload);

        const res = await submitAnswers(payload);
        console.log('제출 성공:', res.data);
        testResultId.value = res.data.data;
        console.log('테스트 결과 Id는?',testResultId.value);

        /* 결과 페이지로 이동 하기 */
        await router.push({ name: 'NowTestResult', params: { testResultId: testResultId.value }});
    } catch (err) {
        console.error('답안 제출 실패:', err);
    }
}

/* mount 되는 시점에 문제를 가져오기 */
onMounted(fetchProblems);
</script>

<template>
    <div class="container-wrapper">
        <div v-if="isLoading" class="loading-overlay">
            <div class="spinner" />
            <p>결과를 불러오는 중입니다...</p>
        </div>

        <div class="timer" v-else>{{ timerDisplay }}</div>

        <div class="container" v-if="!isLoading && problems.length > 0">
            <div class="progress-container">
                <div class="progress-bar" :style="{ width: percentage + '%' }"></div>
            </div>

            <div class="image-area">
                <img :src="problems[currentProblemIndex].contentImageUrl" alt="문제 이미지" class="problem-img"/>
            </div>

            <div class="answer-input">
                정답 :
                <input type="text" v-model="userAnswer"/>
            </div>

            <div class="button-group">
                <button class="btn" @click="goToNextProblem" v-if="currentProblemIndex < totalProblems - 1">다음</button>
                <button class="btn" @click="submitAllAnswers" v-else>끝내기</button>
            </div>

        </div>
    </div>
</template>

<style scoped>
.container {
    max-width: 900px;
    max-height: 800px;
    margin: 3rem auto;
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
    padding: 3rem 4rem;
    text-align: center;
    position: relative;
}

.progress-container {
    width: 100%;
    height: 6px;
    background-color: #e5e7eb;
    border-radius: 4px;
    overflow: hidden;
    margin-bottom: 2rem;
}

.progress-bar {
    height: 100%;
    background-color: #3b82f6;
    transition: width 0.3s ease;
}

.image-area {
    position: relative;
    display: flex;
    justify-content: center;
    margin-bottom: 2rem;
    height: auto;
    border-radius: 12px;
    box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
    object-fit: contain;
    cursor: pointer;
}

.problem-img {
    width: 100%;
    max-width: 720px;
    height: auto;
    border-radius: 12px;
    box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
    object-fit: contain;
    cursor: pointer;
}

.answer-input {
    margin-top: 1.5rem;
}

.answer-input input {
    padding: 0.5rem 1rem;
    font-size: 1rem;
    border-radius: 8px;
    border: 1px solid #ccc;
    width: 100px;
    text-align: center;
}

.button-group {
    margin-top: 2rem;
    display: flex;
    gap: 1.5rem;
    justify-content: center;
}

.btn {
    background: #3b82f6;
    color: #fff;
    border: none;
    padding: 0.75rem 1.5rem;
    border-radius: 12px;
    font-size: 1rem;
    cursor: pointer;
    transition: background 0.2s ease;
}

.btn:hover {
    background: #1e3a8a;
}

.timer {
    position: absolute;
    margin: 10px;
    right: 2rem;
    background: #eee;
    border-radius: 50%;
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    z-index: 10;
}

.loading-overlay {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 50;
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.spinner {
    width: 40px;
    height: 40px;
    border: 4px solid #3b82f6;
    border-top-color: transparent;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 1rem;
}

@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}
</style>