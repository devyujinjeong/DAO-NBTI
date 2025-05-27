<script setup>
// import '@/assets/css/init-styles.css'

</script>

<template>
  <footer class="business-footer-dark">
    <div class="footer-inner">
      <div class="footer-links">
        <a href="#">이용약관</a>
        <span class="divider">|</span>
        <a href="#">개인정보처리방침</a>
      </div>

      <div class="footer-info">
        <p>※ 본 정보는 예시용이며 실제 등록 정보가 아닙니다.</p>
        <p>상호명: 디에이오 주식회사 | 대표자: 정유진 | 사업자등록번호: 123-45-67890</p>
        <p>통신판매업신고번호: 제2025-서울강남-01234호</p>
        <p>주소: 서울특별시 강남구 테헤란로 123, 10층</p>
        <p>고객센터: 02-1234-5678 (평일 09:00 ~ 18:00) | 이메일: support@daonbti.com</p>
      </div>

      <div class="footer-copyright">
        ⓒ 2025 NBTI Inc. All rights reserved.
      </div>
    </div>
  </footer>
</template>

<style scoped>

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'Pretendard', sans-serif;
  font-size: 14px;
  color: #333;
  background-color: #fff;
}

a {
  text-decoration: none;
  color: inherit;
}

.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-main {
  flex: 1;
}

.main-header {
  height: 60px;
  background: linear-gradient(to right, #e7f0ff, #f0e7ff);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 10;
}

.main-header .logo img {
  height: 36px;
}

.nav-menu a {
  margin-left: 20px;
  font-size: 15px;
  color: #333;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s ease;
}

.nav-menu a:hover {
  color: #5b71f2;
}

.business-footer-dark {
  background: linear-gradient(to right, #f2f3f9, #e8f2f7);
  color: #1E1E1E;
  font-family: 'Pretendard', sans-serif;
  font-size: 13px;
  line-height: 1.8;
  padding: 40px 20px;
  text-align: center;
  border-top: 1px solid #e0e0e0;
}

.footer-inner {
  max-width: 960px;
  margin: 0 auto;
}

.footer-links {
  margin-bottom: 16px;
}

.footer-links a {
  color: #1E1E1E;
  margin: 0 8px;
  text-decoration: none;
  font-size: 13px;
}

.footer-links a:hover {
  color: #5790FF;
}

.footer-links .divider {
  color: #555;
  font-size: 13px;
}

.footer-info p {
  margin: 4px 0;
  font-size: 12px;
  color: #555;
}

.footer-info {
  margin-bottom: 16px;
}

.footer-copyright {
  font-size: 12px;
  color: #555;
  margin-top: 12px;
}

</style>