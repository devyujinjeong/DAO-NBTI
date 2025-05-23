<script setup>

import AdminSidebar from "@/features/admin/components/AdminSidebar.vue";
</script>

<template>
  <div class="layout">

    <div class="layout-body">
      <AdminSidebar />
      <main class="content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
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