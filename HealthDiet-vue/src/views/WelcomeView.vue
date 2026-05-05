<template>
  <div class="dashboard">

    <!-- 欢迎语 -->
    <div class="welcome">
      欢迎回来，{{ user.nickname || user.username || '用户' }}
    </div>

    <!-- 卡片区域 -->
    <el-row :gutter="20" class="card-row">
      <el-col :span="6">
        <el-card class="card">
          <div class="card-title">🔥 今日热量</div>
          <div class="card-value">
            {{ calorieData.intake }} / {{ calorieData.target }} kcal
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="card">
          <div class="card-title">碳水</div>
          <div class="card-value">{{ stat.carbohydrate || 0 }} g</div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="card">
          <div class="card-title">蛋白质</div>
          <div class="card-value">{{ stat.protein || 0 }} g</div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="card">
          <div class="card-title">脂肪</div>
          <div class="card-value">{{ stat.fat || 0 }} g</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 主要内容区域：一个大卡片 -->
    <el-card class="main-card">
      <div class="main-content">
        <!-- 左侧：饼状图 -->
        <div class="left-content">
          <div ref="chartRef" class="chart"></div>
        </div>

        <!-- 右侧：公告 + 健康资讯 -->
        <div class="right-content">
          <AnnouncementList />
          <HealthNewsList />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue"
import axios from "axios"
import * as echarts from "echarts"
import AnnouncementList from "@/components/AnnouncementList.vue"
import HealthNewsList from "@/components/HealthNewsList.vue"

const user = ref(JSON.parse(localStorage.getItem("user") || "{}"))

const stat = ref({
  carbohydrate: 0,
  protein: 0,
  fat: 0
})

const calorieData = ref({
  intake: 0,
  target: 0
})

const totalKcal = computed(() => {
  return (
    (stat.value.carbohydrate || 0) * 4 +
    (stat.value.protein || 0) * 4 +
    (stat.value.fat || 0) * 9
  )
})

const chartRef = ref(null)
let chart = null

const initChart = () => {
  chart = echarts.init(chartRef.value)

  const option = {
    title: {
      text: "今日营养占比",
      left: "center"
    },
    tooltip: {
      trigger: "item"
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        name: "营养占比",
        type: "pie",
        radius: "60%",
        data: [
          {
            value: stat.value.carbohydrate || 0,
            name: "碳水",
            itemStyle: { color: "#fbbf24" }
          },
          {
            value: stat.value.protein || 0,
            name: "蛋白质",
            itemStyle: { color: "#34d399" }
          },
          {
            value: stat.value.fat || 0,
            name: "脂肪",
            itemStyle: { color: "#fb923c" }
          }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0
          }
        }
      }
    ]
  }

  chart.setOption(option)
}

const loadData = async () => {
  try {
    const [statRes, calorieRes] = await Promise.all([
      axios.get("http://localhost:8080/dietRecord/stat", {
        params: { userId: user.value.id }
      }),
      axios.get("http://localhost:8080/dietRecord/todayCalorie", {
        params: { userId: user.value.id }
      })
    ])

    stat.value = statRes.data
    calorieData.value = calorieRes.data

    initChart()
  } catch (e) {
    console.error("获取数据失败", e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard {
  padding: 15px;
}

.welcome {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
}

.card-row {
  margin-bottom: 15px;
}

.card {
  text-align: center;
}

.card-title {
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
}

.card-value {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.main-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.main-content {
  display: grid;
  grid-template-columns: 5fr 3fr;
  gap: 12px;
}

.left-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.right-content {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.chart {
  width: 100%;
  height: 420px;
}

.health-info-section {
  background: #f9fafb;
  border-radius: 6px;
  overflow: hidden;
  flex: 1;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
}

.header-icon {
  font-size: 14px;
}

.header-title {
  font-size: 12px;
  font-weight: 600;
}

.section-placeholder {
  padding: 12px 10px;
}

@media (max-width: 1200px) {
  .main-content {
    grid-template-columns: 3fr 2fr;
  }
}

@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }
}
</style>
