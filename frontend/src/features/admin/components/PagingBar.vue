<template>
  <div class="paging-bar">
    <button
        class="nav-arrow"
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)">
        &laquo;
    </button>

    <span v-for="page in visiblePages" :key="page">
      <button
          :class="{ pageBtn: true, active: page === currentPage }"
          @click="changePage(page)">
        {{ page }}
      </button>
    </span>

    <button
        class="nav-arrow"
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)">
        &raquo;
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  currentPage: Number,
  totalPages: Number,
  totalItems: Number
});

const emit = defineEmits(['page-changed']);

const changePage = (page) => {
  if (page >= 1 && page <= props.totalPages) {
    emit('page-changed', page);
  }
};

const visiblePages = computed(() => {
  const range = 2; // currentPage 기준 좌우 2개씩, 총 5개
  let start = props.currentPage - range;
  let end = props.currentPage + range;

  // 범위 조정: 시작이 1보다 작으면 끝을 보정
  if (start < 1) {
    end += 1 - start;
    start = 1;
  }

  // 끝이 총 페이지 수를 넘으면 시작을 보정
  if (end > props.totalPages) {
    start -= end - props.totalPages;
    end = props.totalPages;
  }

  // 여전히 start가 1보다 작을 수 있음
  start = Math.max(1, start);

  const pages = [];
  for (let i = start; i <= end && pages.length < 5; i++) {
    pages.push(i);
  }

  return pages;
});
</script>

<style scoped>
.paging-bar {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 0.5rem;
    margin-top: 1rem;
}

.paging-bar button {
    padding: 0.4rem 0.8rem;
    border: 1px solid #ddd;
    background: #fff;
    border-radius: 4px;
    cursor: pointer;
}

.paging-bar button.active {
    background: #007bff;
    color: #fff;
    border-color: #007bff;
}

.nav-arrow {
    padding: 0.4rem 0.8rem;
    border: 1px solid #ddd;
    background: #fff;
    border-radius: 4px;
    cursor: pointer;
}
</style>
