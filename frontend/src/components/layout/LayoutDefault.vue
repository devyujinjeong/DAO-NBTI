<script setup>
import Header from "@/components/layout/Header.vue";
import Footer from "@/components/layout/Footer.vue";

</script>

<template>
  <div class="layout-default">
    <!-- 상단 헤더 섹션 -->
    <Header/>
    <!-- 좌측 사이드바 + 콘텐츠 섹션 -->
    <div class="content-wrapper">
      <main class="main-content" >
        <RouterView/>
        <Footer/>
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout-default {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100%;
  background-color: #f9f9f9;
  overflow: hidden;
}

/* 오른쪽 전체 (헤더 + 메인) */
.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: row;
  height: 100vh;
  overflow: hidden;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  background-color: white;
}

</style>