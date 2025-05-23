<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import QuestionDetail from "@/features/study/components/QuestionDetail.vue";
import QuestionOptions from "@/features/study/components/QuestionOptions.vue";
import ResultSummary from "@/features/study/components/ResultSummary.vue";
import router from "@/router/index.js";
import {getStudyResult} from "@/features/study/api.js";

const route = useRoute();
const studyId = Number(route.query.studyId);

const selectedIndex = ref(0);
const problems = ref([]);
const totalCount = ref(0);
const correctCount = ref(0);
const resultText = ref('');

async function fetchStudyResults() {
  try {
    const { data } = await getStudyResult(studyId);
    if (data.success) {
      problems.value = data.data.results;
      totalCount.value = data.data.totalCount;
      correctCount.value = data.data.correctCount;
      resultText.value = `${correctCount.value} / ${totalCount.value} 문제 정답`;
    }
  } catch (e) {
    console.error('API 호출 실패', e);
  }
}

function handleOptionClick(index) {
  selectedIndex.value = index;
}

function goHome() {
  router.push('/');
}

onMounted(fetchStudyResults);
</script>

<template>
  <div class="background-shadow" v-if="problems.length">
    <QuestionOptions
        :options="problems.map((_, i) => `${i + 1}번`)"
        :selectedIndex="selectedIndex"
        :resultText="resultText"
        @option-click="handleOptionClick"
    />
    <QuestionDetail
        :level="`레벨 ${problems[selectedIndex].level}`"
        :correctAnswer="problems[selectedIndex].correctAnswer"
        :submittedAnswer="problems[selectedIndex].submittedAnswer"
        :correct="problems[selectedIndex].correct"
        :imageUrl="problems[selectedIndex].contentImageUrl"
    />
    <ResultSummary :resultText="resultText" @go-home="goHome" />
  </div>
</template>

<style>
.background-shadow {
  max-width: 1176px;
  width: 100%;
  margin: 0 auto;
  padding: 40px 48px;
  border-radius: 20px;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  gap: 32px;
  position: relative;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}
</style>
