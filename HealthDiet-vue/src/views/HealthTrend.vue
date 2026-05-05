<template>
  <div class="trend-container">

    <!-- 切换 -->
    <el-radio-group v-model="type" @change="loadData" class="type-selector">
      <el-radio-button label="day">今日</el-radio-button>
      <el-radio-button label="week">本周</el-radio-button>
      <el-radio-button label="month">本月</el-radio-button>
    </el-radio-group>

    <!-- 图表区域 -->
    <div class="charts-container">
      <div ref="pieRef" class="chart"></div>
      <div ref="lineRef" class="chart"></div>
    </div>

    <!-- 营养建议 -->
    <div class="advice-card">
      <div class="advice-title">📋 营养摄入分析建议</div>

      <div class="goal-tag">目标：{{ healthGoalLabel }}</div>

      <el-table :data="adviceData" border class="advice-table">
        <el-table-column prop="nutrient" label="营养素" width="100" />
        <el-table-column prop="actual" label="实际摄入占比" width="120" />
        <el-table-column prop="standard" label="推荐范围" width="150" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.tagType" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="suggestion" label="建议" />
      </el-table>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue"
import axios from "axios"
import * as echarts from "echarts"

const user = JSON.parse(localStorage.getItem("user") || "{}")

const type = ref("day")
const stat = ref({})
const healthGoal = ref("健康")

const pieRef = ref(null)
const lineRef = ref(null)

let pieChart, lineChart

const loadUserInfo = async () => {
  try {
    const res = await axios.get("http://localhost:8080/user/" + user.id)
    healthGoal.value = res.data.healthGoal || "健康"
  } catch (e) {
    console.error("获取用户信息失败", e)
  }
}

const healthGoalLabel = computed(() => {
  const map = { "健康": "健康维持", "减脂": "减脂", "增肌": "增肌" }
  return map[healthGoal.value] || "健康维持"
})

const adviceData = computed(() => {
  const total = (stat.value.carbohydrate || 0) + (stat.value.protein || 0) + (stat.value.fat || 0)
  if (total === 0) return []

  const carbPct = parseFloat(((stat.value.carbohydrate || 0) / total * 100).toFixed(1))
  const proteinPct = parseFloat(((stat.value.protein || 0) / total * 100).toFixed(1))
  const fatPct = parseFloat(((stat.value.fat || 0) / total * 100).toFixed(1))

  const standards = {
    "健康": {
      carb: { severeLow: 40, low: 50, high: 65, severeHigh: 75 },
      protein: { severeLow: 5, low: 10, high: 20, severeHigh: 25 },
      fat: { severeLow: 15, low: 20, high: 30, severeHigh: 35 }
    },
    "减脂": {
      carb: { severeLow: 40, low: 50, high: 60, severeHigh: 70 },
      protein: { severeLow: 10, low: 15, high: 20, severeHigh: 25 },
      fat: { severeLow: 15, low: 20, high: 30, severeHigh: 35 }
    },
    "增肌": {
      carb: { severeLow: 35, low: 45, high: 55, severeHigh: 65 },
      protein: { severeLow: 20, low: 25, high: 30, severeHigh: 35 },
      fat: { severeLow: 15, low: 20, high: 30, severeHigh: 35 }
    }
  }

  const std = standards[healthGoal.value] || standards["健康"]

  const checkCarb = (val) => {
    if (val < std.carb.severeLow) return { status: "严重不足", tagType: "danger" }
    if (val < std.carb.low) return { status: "不足", tagType: "warning" }
    if (val <= std.carb.high) return { status: "适宜", tagType: "success" }
    if (val <= std.carb.severeHigh) return { status: "超标", tagType: "warning" }
    return { status: "严重超标", tagType: "danger" }
  }

  const checkProtein = (val) => {
    if (val < std.protein.severeLow) return { status: "严重不足", tagType: "danger" }
    if (val < std.protein.low) return { status: "不足", tagType: "warning" }
    if (val <= std.protein.high) return { status: "适宜", tagType: "success" }
    if (val <= std.protein.severeHigh) return { status: "超标", tagType: "warning" }
    return { status: "严重超标", tagType: "danger" }
  }

  const checkFat = (val) => {
    if (val < std.fat.severeLow) return { status: "严重不足", tagType: "danger" }
    if (val < std.fat.low) return { status: "不足", tagType: "warning" }
    if (val <= std.fat.high) return { status: "适宜", tagType: "success" }
    if (val <= std.fat.severeHigh) return { status: "超标", tagType: "warning" }
    return { status: "严重超标", tagType: "danger" }
  }

  const carbCheck = checkCarb(carbPct)
  const proteinCheck = checkProtein(proteinPct)
  const fatCheck = checkFat(fatPct)

  const carbSuggestions = {
    "严重不足": { "健康": "⚠️ 碳水严重不足：疲乏无力。每餐加一拳头主食。", "减脂": " 碳水严重不足：低于减脂推荐下限，影响代谢和精力。每餐保证半拳头主食。", "增肌": " 碳水严重不足：增肌期碳水不足会导致肌肉分解。每餐加一拳头主食，训练前后补充快碳。" },
    "不足": { "健康": " 碳水偏低：能量不够。增加粗粮如红薯、玉米、燕麦。", "减脂": " 碳水偏低：接近下限，如疲劳可增加少量慢碳（燕麦、全麦）。", "增肌": " 碳水不足：影响训练强度和恢复。增加主食，尤其训练后补充。" },
    "适宜": { "健康": " 碳水适宜：能量充足，继续保持。", "减脂": " 碳水适宜：符合减脂推荐，控制良好。", "增肌": " 碳水适宜：满足增肌供能需求，保持现状。" },
    "超标": { "健康": " 碳水偏高：多余碳水易转脂肪。减少精米白面，用蔬菜替代部分主食。", "减脂": " 碳水超标：超出减脂推荐，影响减脂效果。减少米饭面条，增加绿叶蔬菜。", "增肌": " 碳水略高：超出增肌需求。如体脂上升，可适当减少主食。" },
    "严重超标": { "健康": "⚠️ 碳水严重超标：增加肥胖和糖尿病风险。大幅减少含糖食物和精制碳水。", "减脂": "⚠️ 碳水严重超标：严重阻碍减脂。主食减半，戒掉含糖饮料和零食。", "增肌": "⚠️ 碳水严重超标：易堆积脂肪。减少主食，增加蛋白质和蔬菜比例。" }
  }

  const proteinSuggestions = {
    "严重不足": { "健康": "⚠️ 蛋白严重不足：免疫力下降。每餐加蛋、奶、豆制品或瘦肉。", "减脂": "⚠️ 蛋白严重不足：低于减脂推荐下限，加速肌肉流失。每餐保证手掌大蛋白。", "增肌": "⚠️ 蛋白严重不足：无法支持肌肉生长。每餐1-2份蛋白，考虑补充蛋白粉。" },
    "不足": { "健康": " 蛋白偏低：修复不足。增加鱼肉、鸡胸、豆腐、鸡蛋。", "减脂": " 蛋白不足：接近下限，建议增加鸡胸肉、虾、豆制品保护肌肉。", "增肌": " 蛋白不足：低于增肌推荐，肌肉合成原料不够。每公斤体重摄入1.6-2.2g。" },
    "适宜": { "健康": " 蛋白适宜：修复充足，保持现状。", "减脂": " 蛋白适宜：符合减脂推荐，有效保护肌肉。", "增肌": " 蛋白适宜：满足增肌需求，保持当前摄入。" },
    "超标": { "健康": " 蛋白偏高：多余转能量。适当减少肉类。", "减脂": " 蛋白略高：一般无大碍，如肾负担重可微调。", "增肌": " 蛋白偏高：超出增肌需求。可适当减少蛋白粉补充。" },
    "严重超标": { "健康": "⚠️ 蛋白严重超标：增加肾脏负担。建议咨询营养师调整。", "减脂": "⚠️ 蛋白严重超标：长期过高影响肾脏。减少额外蛋白补充。", "增肌": "⚠️ 蛋白严重超标：身体无法利用多余蛋白。回归推荐范围。" }
  }

  const fatSuggestions = {
    "严重不足": "⚠️ 脂肪严重不足：影响激素分泌和维生素吸收。每天一小把坚果或用橄榄油烹饪。",
    "不足": " 脂肪偏低：可能影响脂溶性维生素吸收。烹饪适量用油，或每天吃10-15g坚果。",
    "适宜": " 脂肪适宜：供能合理，继续保持。优先选择不饱和脂肪（橄榄油、坚果、鱼油）。",
    "超标": " 脂肪超标：能量密度高，易导致体重增加。减少油炸食品、肥肉、黄油。",
    "严重超标": "⚠️ 脂肪严重超标：增加心血管疾病风险。改用蒸、煮、炖等低脂烹饪方式。"
  }

  return [
    {
      nutrient: "碳水化合物",
      actual: carbPct.toFixed(1) + "%",
      standard: `${std.carb.low}% - ${std.carb.high}%`,
      status: carbCheck.status,
      tagType: carbCheck.tagType,
      suggestion: carbSuggestions[carbCheck.status][healthGoal.value]
    },
    {
      nutrient: "蛋白质",
      actual: proteinPct.toFixed(1) + "%",
      standard: `${std.protein.low}% - ${std.protein.high}%`,
      status: proteinCheck.status,
      tagType: proteinCheck.tagType,
      suggestion: proteinSuggestions[proteinCheck.status][healthGoal.value]
    },
    {
      nutrient: "脂肪",
      actual: fatPct.toFixed(1) + "%",
      standard: `${std.fat.low}% - ${std.fat.high}%`,
      status: fatCheck.status,
      tagType: fatCheck.tagType,
      suggestion: fatSuggestions[fatCheck.status]
    }
  ]
})

const loadData = async () => {
  const statRes = await axios.get("http://localhost:8080/dietRecord/trend/stat", {
    params: { userId: user.id, type: type.value }
  })
  stat.value = statRes.data

  pieChart.setOption({
    title: { text: "营养占比", left: "center" },
    series: [{
      type: "pie",
      radius: "60%",
      data: [
        { value: stat.value.carbohydrate || 0, name: "碳水", itemStyle: { color: "#fbbf24" } },
        { value: stat.value.protein || 0, name: "蛋白质", itemStyle: { color: "#34d399" } },
        { value: stat.value.fat || 0, name: "脂肪", itemStyle: { color: "#fb923c" } }
      ]
    }]
  })

  const lineRes = await axios.get("http://localhost:8080/dietRecord/trend/line", {
    params: { userId: user.id }
  })

  const dates = lineRes.data.map(i => i.date)
  const calories = lineRes.data.map(i => i.calorie)

  lineChart.setOption({
    title: { text: "热量趋势", left: "center" },
    xAxis: { type: "category", data: dates },
    yAxis: { type: "value" },
    series: [{
      data: calories,
      type: "line",
      smooth: true
    }]
  })
}

onMounted(async () => {
  pieChart = echarts.init(pieRef.value)
  lineChart = echarts.init(lineRef.value)

  await loadUserInfo()
  await loadData()
})
</script>

<style scoped>
.trend-container {
  padding: 20px;
}

.type-selector {
  margin-bottom: 20px;
}

.charts-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.chart {
  flex: 1;
  height: 350px;
  background: #fff;
  border-radius: 10px;
  padding: 15px;
}

.advice-card {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
}

.advice-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}

.goal-tag {
  display: inline-block;
  background: #fef3c7;
  color: #d97706;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 13px;
  margin-bottom: 15px;
}

.advice-table {
  margin-top: 10px;
}
</style>
