<template>
  <div v-if="totalPages > 0" class="pagination">
    <button
        class="page-btn"
        :disabled="currentPage === 1"
        @click="goToPage(currentPage - 1)"
    >
      ‹
    </button>

    <button
        v-for="page in totalPages"
        :key="page"
        class="page-btn"
        :class="{ active: currentPage === page }"
        @click="goToPage(page)"
    >
      {{ page }}
    </button>

    <button
        class="page-btn"
        :disabled="currentPage === totalPages"
        @click="goToPage(currentPage + 1)"
    >
      ›
    </button>
  </div>
</template>

<script setup>
import { computed, toRefs } from 'vue'

const props = defineProps({
  currentPage: { type: Number, required: true },
  pageSize:    { type: Number, default: 10 },
  totalItems:  { type: Number, required: true }
})
const { currentPage, pageSize, totalItems } = toRefs(props)
const emit = defineEmits(['update:currentPage'])

const totalPages = computed(() =>
    Math.ceil(totalItems.value / pageSize.value)
)

function goToPage(page) {
  if (page >= 1 && page <= totalPages.value) {
    emit('update:currentPage', page)
  }
}
</script>

<style scoped>
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.25rem;
  padding: 2rem 0;
}

.page-btn {
  width: 2.2rem;
  height: 2.2rem;
  border: none;
  border-radius: 0.5rem;
  background: #f0f4f8;
  color: #475569;
  font-weight: 500;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  transition: background 0.2s, transform 0.1s;
}

.page-btn:hover:not(:disabled) {
  background: #e2e8f0;
  transform: translateY(-1px);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-btn.active {
  background: var(--color-main, #3b82f6);
  color: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.15);
}

.page-btn:first-of-type,
.page-btn:last-of-type {
  font-size: 1.2rem;
}
</style>
