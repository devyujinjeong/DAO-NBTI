<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()

const isActive = (path) => route.path === path || route.path.startsWith(path + '/')
</script>

<template>
  <aside class="sidebar">
    <h3>마이페이지</h3>
    <router-link
        to="/mypage/test"
        class="menu-link"
        :class="{ active: isActive('/mypage/test') }"
    >검사 결과 및 통계</router-link>

    <router-link
        to="/mypage/study"
        class="menu-link"
        :class="{ active: isActive('/mypage/study') }"
    >학습 결과 내역</router-link>

    <router-link
        to="/mypage/objection"
        class="menu-link"
        :class="{ active: isActive('/mypage/objection') }"
    >이의제기 내역</router-link>

    <router-link
        to="/mypage/userinfo"
        class="menu-link"
        :class="{ active: isActive('/mypage/userinfo') }"
    >개인정보 조회</router-link>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #ddd;
  padding: 2rem 1rem;
  box-sizing: content-box;
  position: sticky;
  top: 0;
  //height: auto;
  //flex-shrink: 0;
}

.sidebar h3 {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.menu-link {
  display: block;
  padding: 0.75rem 1rem;
  margin-bottom: 0.5rem;
  border-radius: 8px;
  text-decoration: none;
  color: #333;
  transition: background 0.2s ease;
}

.menu-link:hover {
  background: #f1f5ff;
  color: #1e3a8a;
}

.menu-link.active {
  background: #e9f0ff;
  color: #3b82f6;
  font-weight: 500;
}
</style>
