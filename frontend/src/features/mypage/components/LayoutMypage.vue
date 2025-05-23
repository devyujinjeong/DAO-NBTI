<script setup>
import Sidebar from '@/features/mypage/components/Sidebar.vue'
</script>

<template>
  <div class="layout">
    <!-- <Header /> -->

    <div class="layout-body">
      <Sidebar />
      <main class="content" aria-label="마이페이지 콘텐츠">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  min-height: 100%;
  background-color: var(--bg-light, #f8f9fa);
}

.layout-body {
  display: flex;
  width: 100%;
}

.content {
  flex: 1;
  padding: 2rem 3rem;
  background-color: var(--bg-light, #f8f9fa);
}
</style>
