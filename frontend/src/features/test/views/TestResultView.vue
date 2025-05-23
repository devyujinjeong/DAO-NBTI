<script setup>
import {computed, nextTick, onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {useToast} from "vue-toastification";
import {getTestResult, saveResultToMyPage} from "@/features/test/api.js";
import Chart from 'chart.js/auto'
import Url from "@/features/test/components/Url.vue";
import {useAuthStore} from "@/stores/auth.js";

const route = useRoute();
const router = useRouter();
const toast = useToast();
const authStore = useAuthStore();

const radarCanvas = ref(null);
const modalVisible = ref(false);
const modalMessage = ref('');
const isUser = computed(() => authStore.isAuthenticated);
const testResultId = route.params.testResultId;

console.log('저장된 회원의 아이디는? : ', authStore.userId)
console.log('받은 testResultId:', testResultId)

const categoryIcons = {
    '언어 이해':   new URL('@/assets/images/language_comprehension.png', import.meta.url).href,
    '시사 상식':   new URL('@/assets/images/common_sense.png', import.meta.url).href,
    '지각 추론':   new URL('@/assets/images/perceptual_reasoning.png', import.meta.url).href,
    '공간 지각력': new URL('@/assets/images/spatial_perception.png', import.meta.url).href,
    '작업 기억':   new URL('@/assets/images/work_memory.png', import.meta.url).href,
    '처리 속도':   new URL('@/assets/images/processing_speed.png', import.meta.url).href
}

const scores = ref([]);
const summary = ref('');

/* 결과를 가져오는 api 연결하기 */
onMounted(async () => {
    try {
        const res = await getTestResult(testResultId);

        scores.value = res.data.data.scores;
        summary.value = res.data.data.aiText;

        await nextTick();

        const ctx = radarCanvas.value?.getContext('2d');
        if (!ctx) return;

        new Chart(ctx, {
            type: 'radar',
            data: {
                labels: scores.value.map(s => s.categoryName),
                datasets: [{
                    label: '검사 점수',
                    data: scores.value.map(s => s.score),
                    backgroundColor: 'rgba(59, 130, 246, 0.2)',
                    borderColor: '#3b82f6',
                    pointBackgroundColor: '#3b82f6',
                    borderWidth: 2
                }]
            },
            options: {
                scales: {
                    r: {
                        min: 0,
                        max: 6,
                        ticks: { stepSize: 1, color: '#555' },
                        grid: { color: '#ddd' },
                        pointLabels: { color: '#333', font: { size: 14 } }
                    }
                },
                plugins: { legend: { display: false } }
            }
        })
    } catch (e) {
        toast.error('결과 불러오기에 실패했습니다.')
        console.error(e)
    }
})

/* 모달과 관련된 함수들 (open, close) */
function openModal() {
    modalMessage.value = window.location.href
    modalVisible.value = true
}

function closeModal() {
    modalVisible.value = false
}

/* 마이페이지에 저장하는 api*/
async function saveToMyPage() {
    try {
        await saveResultToMyPage(testResultId);
        toast.success('마이페이지에 저장되었습니다.');
    } catch (e) {
        if (e.response && e.response.data && e.response.data.message) {
            const code = e.response.data.errorCode;

            if (code === '30005') {
                toast.error('해당 검사는 본인이 한 검사가 아닙니다.');
            }

        } else {
            toast.error('마이페이지 저장에 실패했습니다. 다시 시도해주세요.');
        }
    }
}

/* main 페이지로 이동하기 */
function goToMain() {
    router.push('/')
}
</script>

<template>
    <div class="container">
        <h2>검사 결과</h2>

        <div class="chart-container">
            <canvas ref="radarCanvas"></canvas>
        </div>

        <div class="grid">
            <div
                v-for="(item, idx) in scores"
                :key="idx"
                class="grid-item"
            >
                <img
                    :src="categoryIcons[item.categoryName]"
                    alt="카테고리"
                    class="category-icon"
                />
                <div class="score-content">
                    <div class="score-header">
                        <div class="category-title">{{ item.categoryName }}</div>
                        <div class="score-num">{{ item.score }}점</div>
                    </div>
                    <div class="score-bar">
                        <div class="bar" :style="{ width: (item.score / 6) * 100 + '%' }"></div>
                    </div>
                    <div class="category-content">{{ item.description }}</div>
                </div>
            </div>

        </div>

        <div class="summary">
            <h3>AI 총평</h3>
            <p>{{ summary }}</p>
        </div>

        <div class="buttons">
            <button class="btn" @click="saveToMyPage" v-if="isUser">저장하기</button>
            <button class="btn" @click="openModal">공유하기</button>
            <Url
                :visible="modalVisible"
                :message="modalMessage"
                @close="closeModal"
            />
            <button class="btn" @click="goToMain">메인으로</button>
        </div>
    </div>
</template>

<style scoped>
body {
    margin: 0;
    background: #f8f9fa;
    font-family: 'Pretendard', sans-serif;
    color: #222;
}

.container {
    max-width: 1000px;
    margin: 4rem auto;
    background: #ffffff;
    border-radius: 16px;
    padding: 3rem 2rem;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.06);
}

h2 {
    text-align: center;
    font-size: 2rem;
    color: #1e3a8a;
    margin-bottom: 3rem;
}

.chart-container {
    max-width: 500px;
    margin: 0 auto 4rem;
}

.grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 2rem;
    margin: 2rem 0;
}

.grid-item {
    background: #fff;
    border-radius: 16px;
    padding: 1.5rem;
    border: 1px solid #e5e7eb;
    display: flex;
    align-items: center;
    gap: 20px;
}

.category-icon {
    width: 100px;
    height: 100px;
    object-fit: contain;
}

.category-title {
    font-size: 1.1rem;
    font-weight: bold;
    color: #1e3a8a;
}

.score-num {
    font-size: 1rem;
    font-weight: 500;
    color: #3b82f6;
}

.category-content {
    font-size: 0.8rem;
    color: #666;
    margin-top: 0.5rem;
}

.score-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 0.75rem;
}

.score-bar {
    height: 10px;
    background: #f1f5f9;
    border-radius: 8px;
    overflow: hidden;
    margin-top: 0.5rem;
}

.score-bar .bar {
    height: 100%;
    background: linear-gradient(to right, #3b82f6, #93c5fd);
    transition: width 0.4s ease-in-out;
}

.summary {
    margin-top: 3rem;
    padding: 2rem;
    border-radius: 12px;
    background: #f5f7fb;
    border: 1px solid #dbeafe;
}

.summary h3 {
    font-size: 1.2rem;
    color: #1e3a8a;
    margin-bottom: 1rem;
}

.summary p {
    font-size: 0.95rem;
    line-height: 1.6;
    color: #444;
}

.buttons {
    display: flex;
    justify-content: center;
    gap: 1rem;
    margin-top: 2.5rem;
}

.btn {
    padding: 0.75rem 1.5rem;
    background: #3b82f6;
    color: #fff;
    border: none;
    border-radius: 12px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background 0.2s ease;
}

.btn:hover {
    background: #1e3a8a;
}
</style>