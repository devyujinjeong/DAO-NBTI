<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import {
  Chart,
  LineController,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip
} from 'chart.js'

Chart.register(LineController, CategoryScale, LinearScale, PointElement, LineElement, Filler, Tooltip)

const props = defineProps({
  labels: {
    type: Array,
    required: true
  },
  data: {
    type: Array,
    required: true
  }
})

const canvasRef = ref(null)
let chartInstance = null

const renderChart = () => {
  if (!props.labels || !props.data) return

  chartInstance = new Chart(canvasRef.value, {
    type: 'line',
    data: {
      labels: props.labels,
      datasets: [{
        label: '총점',
        data: props.data,
        borderColor: '#3b82f6',
        backgroundColor: 'rgba(147, 197, 253, 0.3)',
        fill: true,
        pointRadius: 4,
        pointBackgroundColor: '#1e3a8a'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: { legend: { display: false } },
      scales: {
        y: {
          min: 0,
          max: 36,
          ticks: { stepSize: 6, color: '#888' },
          grid: { color: '#eee' }
        },
        x: {
          ticks: { color: '#888' },
          grid: { display: false }
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
  <div class="chart-box line">
    <canvas ref="canvasRef" aria-label="총점 추이 차트" role="img"></canvas>
  </div>
</template>

<style scoped>
.chart-box.line {
  aspect-ratio: 1.6 / 1;
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
