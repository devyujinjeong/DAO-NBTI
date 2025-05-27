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
const authStore = useAuthStore();
const toast = useToast();

const radarCanvas = ref(null);

const urlModalVisible = ref(false);
const urlModalMessage = ref('');

const isUser = computed(() => authStore.isAuthenticated);
const isUserTest = ref(false);
const testResultId = route.params.testResultId;
const maxScore = ref(0);

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

        const userId = res.data.data.userId;
        isUserTest.value = !!userId;
        const maxScoreValue = userId ? 6 : 2;

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
                        max: maxScoreValue,
                        ticks: { stepSize: 1, color: '#555' },
                        grid: { color: '#ddd' },
                        pointLabels: { color: '#333', font: { size: 14 } }
                    }
                },
                plugins: { legend: { display: false } }
            }
        })

        maxScore.value = maxScoreValue;

    } catch (e) {
        if (scores == null) {
            toast.error('결과 불러오기에 실패했습니다.')
        } else{
            toast.error('에러가 발생했습니다.')
        }

    }
})

/* 모달과 관련된 함수들 (open, close) */
function openModal() {
    urlModalMessage.value = window.location.href
    urlModalVisible.value = true
}

function closeModal() {
    urlModalVisible.value = false
}

/* 마이페이지에 저장하는 api*/
async function saveToMyPage() {
    if (!isUser.value) {
        urlModalMessage.value = '회원가입한 사용자만 이용할 수 있습니다!';
        urlModalVisible.value = true;
        return;
    }

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
        <h2>인지 능력 검사 결과</h2>

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
                        <div class="bar" :style="{ width: (item.score / maxScore) * 100 + '%' }"></div>
                    </div>
                    <div class="category-content">{{ item.description }}</div>
                </div>
            </div>

        </div>

        <div class="summary">
            <h3>AI 총평</h3>
            <p>{{ summary }}</p>
        </div>

        <div class="guest-hint" v-if="!isUserTest">
            <p>
                🔒 회원 가입하면 더 다양한 문제를 풀 수 있습니다.
            </p>
        </div>

        <div class="buttons">
            <button class="btn" @click="saveToMyPage" v-if="isUser">저장하기</button>
            <button class="btn" @click="openModal">공유하기</button>

            <Url
                :visible="urlModalVisible"
                :message="urlModalMessage"
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
    background: #f9f9fa;
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
.score-content {
    width: 100%;
}

.score-bar {
    width: 100%;
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

.guest-hint {
    margin-top: 2rem;
    padding: 1rem;
    background: #fff4f4;
    border: 1px solid #fca5a5;
    border-radius: 12px;
    text-align: center;
    color: #b91c1c;
    font-size: 0.95rem;
}

</style>