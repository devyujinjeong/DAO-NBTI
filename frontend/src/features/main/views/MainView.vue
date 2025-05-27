<script setup>
import StudyCategoryListFrame from "@/features/main/components/StudyCategoryListFrame.vue";

import TestStart from "@/features/main/components/TestStart.vue";

</script>

<template>
  <div class="wrapper-frame">
    <TestStart/>
    <StudyCategoryListFrame></StudyCategoryListFrame>
  </div>

</template>

<style scoped>
.wrapper-frame {
  background-color: #ffffff;
  padding: 32px;
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
}
</style>