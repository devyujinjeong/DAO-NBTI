<script setup>
import {computed, ref} from 'vue'
import {useRoute} from "vue-router";

const route = useRoute();

// 현재 경로를 기반으로 active 메뉴 판단
const isProblemOrObjectionActive = computed(() => {
  return route.path.includes('/admin/problems') || route.path.includes('/admin/objections')
});

// 현재 메뉴와 경로를 비교하여 active 클래스 적용
const isActive = (menuPath) => {
  return route.path.startsWith(menuPath);
};
</script>

<template>
  <aside class="sidebar">
    <h3>서비스 관리</h3>

    <RouterLink to="/admin/user">회원 관리</RouterLink>

    <ul v-if="!isProblemOrObjectionActive">
      <li>
        <RouterLink
            to="/admin/user"
            :class="{ active: isActive('/admin/user') }"
        >회원</RouterLink>
      </li>
      <li>
        <RouterLink
            to="/admin/test"
            :class="{ active: isActive('/admin/test') }"
        >검사 결과</RouterLink>
      </li>
      <li>
        <RouterLink
            to="/admin/study"
            :class="{ active: isActive('/admin/study') }"
        >학습 결과</RouterLink>
      </li>
    </ul>

    <RouterLink
        to="/admin/problems"
        :class="{ active: isActive('/admin/problems') }"
    >문제 관리</RouterLink>

    <RouterLink
        to="/admin/objections"
        :class="{ active: isActive('/admin/objections') }"
    >이의 제기 관리</RouterLink>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 300px;
  background: #ffffff;
  border-right: 1px solid #ddd;
  padding: 2rem 1rem;
  height: 100vh;
  box-sizing: border-box;
  position: sticky;
  top: 0;
}
.sidebar h3 {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}
.sidebar a {
  display: block;
  padding: 0.75rem 1rem;
  margin-bottom: 0.5rem;
  border-radius: 8px;
  text-decoration: none;
  color: #333;
}
.sidebar a:hover,
.sidebar a.active
{
  background: #e9f0ff;
  color: #007bff;
  font-weight: 500;
}

.sidebar ul {
  list-style: none;
  margin: 0;
  padding-left: 16px;
}

.sidebar li {
  margin: 0;
  padding-left: 24px;
}
</style>
