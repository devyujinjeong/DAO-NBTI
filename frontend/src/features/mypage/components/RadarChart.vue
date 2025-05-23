<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import {
  Chart,
  RadarController,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip
} from 'chart.js'

Chart.register(RadarController, RadialLinearScale, PointElement, LineElement, Filler, Tooltip)

const props = defineProps({
  labels: {
    type: Array,
    required: true
  },
  data: {
    type: Array,
    required: true
  },
  aspectRatio: {
    type: Number,
    default: 1
  }
})

const canvasRef = ref(null)
let chartInstance = null

function renderChart() {
  if (!canvasRef.value) return

  chartInstance = new Chart(canvasRef.value, {
    type: 'radar',
    data: {
      labels: props.labels,
      datasets: [{
        label: '검사 점수 (6점 만점)',
        data: props.data,
        backgroundColor: 'rgba(59, 130, 246, 0.2)',
        borderColor: '#3b82f6',
        pointBackgroundColor: '#3b82f6',
        borderWidth: 2
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: { legend: { display: false } },
      scales: {
        r: {
          min: 0,
          max: 6,
          ticks: { stepSize: 1, color: '#888' },
          grid: { color: '#ddd' },
          pointLabels: { color: '#333', font: { size: 13 } }
        }
      }
    }
  })
}

onMounted(renderChart)

watch(() => props.data, () => {
  if (chartInstance) chartInstance.destroy()
  renderChart()
})

onBeforeUnmount(() => {
  if (chartInstance) chartInstance.destroy()
})
</script>

<template>
  <div
      class="chart-box radar"
      :style="{ aspectRatio: aspectRatio }"
  >
    <canvas ref="canvasRef" aria-label="영역별 점수 레이더 차트" role="img"></canvas>
  </div>
</template>

<style scoped>
.chart-box.radar {
  background: #fff;
  padding: 1rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}
</style>
