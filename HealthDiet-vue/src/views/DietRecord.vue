<template>
  <div class="diet-record">
    <el-card class="form-card">
      <template #header>
        <div class="card-header">
          <span>记录饮食</span>
        </div>
      </template>

      <el-form :model="form" label-width="60px" class="diet-form">
        <el-row :gutter="15">
          <el-col :span="8">
            <el-form-item label="日期">
              <el-date-picker
                v-model="form.recordDate"
                type="date"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="餐别">
              <el-select v-model="form.mealType" style="width: 100%">
                <el-option label="早餐" value="早餐" />
                <el-option label="午餐" value="午餐" />
                <el-option label="晚餐" value="晚餐" />
                <el-option label="加餐" value="加餐" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="备注">
              <el-input v-model="form.remark" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="food-card">
      <template #header>
        <div class="card-header">
          <span>食物列表</span>
          <el-button type="primary" size="small" @click="addItem">+ 添加食物</el-button>
        </div>
      </template>

      <div v-if="items.length === 0" class="empty-tip">
        点击上方"添加食物"开始记录
      </div>

      <div v-for="(item, index) in items" :key="index" class="food-item">
        <div class="food-info" v-if="item.foodName">
          <span class="food-name">{{ item.foodName }}</span>
          <span class="food-weight" v-if="item.weight">{{ item.weight }}g</span>
          <span class="food-type" v-if="item.type === 'dish'">[菜品]</span>
        </div>
        <el-button type="primary" plain @click="openFoodDialog(index)">
          {{ item.foodId ? '修改' : '选择' }}
        </el-button>
        <el-button type="danger" plain @click="removeItem(index)">删除</el-button>
      </div>

      <div class="submit-area">
        <el-button type="success" size="large" @click="submit">提交记录</el-button>
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="currentType === 'food' ? '选择食物' : '选择菜品'" width="750px" class="food-dialog" :append-to-body="true">
      <div class="dialog-header">
        <div class="type-selector-wrapper">
          <div 
            class="type-tab"
            :class="{ active: currentType === 'food' }"
            @click="currentType = 'food'; onTypeChange()"
          >
            🥗 食材
          </div>
          <div 
            class="type-tab"
            :class="{ active: currentType === 'dish' }"
            @click="currentType = 'dish'; onTypeChange()"
          >
            🍳 菜品
          </div>
        </div>
        <div class="search-wrapper">
          <el-input
            v-model="searchQuery"
            placeholder="🔍 搜索..."
            class="search-input"
            clearable
            @input="onSearchChange"
          />
        </div>
      </div>

      <div class="food-selector">
        <div v-if="currentType === 'food'" class="category-sidebar">
          <div
            v-for="cat in categories"
            :key="cat.value"
            class="category-item"
            :class="{ active: activeCategory === cat.value }"
            @click="activeCategory = cat.value; onCategoryChange()"
          >
            {{ cat.label }}
          </div>
        </div>
        <div v-else class="category-sidebar">
          <div
            v-for="cat in dishCategories"
            :key="cat.value"
            class="category-item"
            :class="{ active: activeDishCategory === cat.value }"
            @click="activeDishCategory = cat.value; onCategoryChange()"
          >
            {{ cat.label }}
          </div>
        </div>

        <div class="food-content">
          <div class="food-grid">
            <div
              v-for="item in filteredList"
              :key="item.id"
              class="food-cell"
              :class="{ selected: selectedId === item.id }"
              @click="selectItem(item)"
            >
              <div v-if="selectedId === item.id" class="check-mark">✓</div>
              <img
                v-if="item.imageUrl"
                :src="baseURL + item.imageUrl"
                class="food-cell-img"
                loading="lazy"
                @load="(e) => e.target.classList.add('loaded')"
                @error="(e) => handleImageError(e)"
              />
              <div v-else class="food-cell-img food-cell-img-placeholder">暂无图片</div>
              <div class="food-cell-name">{{ item.name }}</div>
              <div class="food-cell-cal">
                {{ currentType === 'food' ? (nutritionMap[item.id]?.calorie || '--') + 'kcal/100g' : (item.category || '--') }}
              </div>
            </div>
          </div>

          <div v-if="selectedItem" class="nutrition-info">
            <div class="nutrition-header">
              <div class="nutrition-title">{{ selectedItem.name }}
                <span v-if="currentType === 'dish'"> [菜品]</span>
              </div>
              <div class="nutrition-close" @click="clearSelection">✕</div>
            </div>
            
            <div v-if="currentType === 'food'" class="nutri-grid">
              <div class="nutri-card">
                <div class="nutri-icon">🍚</div>
                <div class="nutri-label">碳水化合物</div>
                <div class="nutri-value">{{ nutrition.carbohydrate || 0 }}g</div>
              </div>
              <div class="nutri-card">
                <div class="nutri-icon">🥩</div>
                <div class="nutri-label">蛋白质</div>
                <div class="nutri-value">{{ nutrition.protein || 0 }}g</div>
              </div>
              <div class="nutri-card">
                <div class="nutri-icon">🥑</div>
                <div class="nutri-label">脂肪</div>
                <div class="nutri-value">{{ nutrition.fat || 0 }}g</div>
              </div>
            </div>
            
            <div class="weight-input-area">
              <span class="weight-label">{{ currentType === 'food' ? '摄入重量' : '菜品重量' }}：</span>
              <el-input-number v-model="inputWeight" :min="1" :max="10000" />
              <span class="weight-unit">g</span>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmItem">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue"
import axios from "axios"

const nutrition = ref({})
const nutritionMap = ref({})
const baseURL = "http://localhost:8080"

const user = JSON.parse(localStorage.getItem("user") || "{}")

const form = ref({
  userId: user.id || 1,
  recordDate: "",
  mealType: "早餐",
  remark: ""
})

const items = ref([
  { foodId: "", foodName: "", weight: "", type: "food" }
])

const foodList = ref([])
const dishList = ref([])

const categories = [
  { label: "主食", value: "主食" },
  { label: "肉类", value: "肉类" },
  { label: "水产", value: "水产" },
  { label: "蛋奶类", value: "蛋奶类" },
  { label: "蔬菜", value: "蔬菜" },
  { label: "水果", value: "水果" },
  { label: "坚果", value: "坚果" },
  { label: "调料", value: "调料" }
]

const dishCategories = [
  { label: "热菜", value: "热菜" },
  { label: "凉菜", value: "凉菜" },
  { label: "汤", value: "汤" },
  { label: "小吃", value: "小吃" }
]

const activeDishCategory = ref("热菜")

const dialogVisible = ref(false)
const activeCategory = ref("主食")
const currentItemIndex = ref(-1)
const selectedId = ref(null)
const selectedItem = ref(null)
const inputWeight = ref(100)
const currentType = ref("food")
const searchQuery = ref("")

const filteredFoods = computed(() => {
  let list = foodList.value.filter(food => food.category === activeCategory.value)
  if (searchQuery.value) {
    list = list.filter(food => food.name.includes(searchQuery.value))
  }
  return list
})

const filteredDishes = computed(() => {
  let list = dishList.value.filter(dish => dish.category === activeDishCategory.value)
  if (searchQuery.value) {
    list = list.filter(dish => dish.name.includes(searchQuery.value))
  }
  return list
})

const currentList = computed(() => {
  return currentType.value === 'food' ? filteredFoods.value : filteredDishes.value
})

const filteredList = computed(() => {
  return currentList.value
})

const onSearchChange = () => {
  selectedId.value = null
  selectedItem.value = null
  nutrition.value = {}
}

const onTypeChange = () => {
  selectedId.value = null
  selectedItem.value = null
  nutrition.value = {}
}

const onCategoryChange = () => {
  selectedId.value = null
  selectedItem.value = null
  nutrition.value = {}
}

const openFoodDialog = async (index) => {
  currentItemIndex.value = index
  const item = items.value[index]
  
  if (item.type === 'dish' && item.dishId) {
    currentType.value = 'dish'
    selectedId.value = item.dishId
    selectedItem.value = dishList.value.find(d => d.id === item.dishId)
    inputWeight.value = item.weight || 100
  } else if (item.type === 'food' && item.foodId) {
    currentType.value = 'food'
    selectedId.value = item.foodId
    selectedItem.value = foodList.value.find(f => f.id === item.foodId)
    if (selectedItem.value) {
      activeCategory.value = selectedItem.value.category || "主食"
      await loadNutrition(item.foodId)
    }
    inputWeight.value = item.weight || 100
  } else {
    selectedId.value = null
    selectedItem.value = null
    nutrition.value = {}
    inputWeight.value = 100
    currentType.value = 'food'
    activeCategory.value = "主食"
  }
  dialogVisible.value = true
}

const loadNutrition = async (foodId) => {
  try {
    const res = await axios.get(baseURL + "/nutrition/" + foodId)
    nutrition.value = res.data
  } catch (e) {
    console.error("获取营养失败", e)
  }
}

const handleImageError = (e) => {
  const img = e.target
  img.style.opacity = '1'
  img.src = ''
  img.alt = '暂无图片'
}

const selectItem = async (item) => {
  selectedId.value = Number(item.id) // 确保是数字类型
  selectedItem.value = item
  if (currentType.value === 'food') {
    await loadNutrition(item.id)
  }
  console.log("选中的项目:", currentType.value, selectedId.value, item.name, typeof selectedId.value)
}

const clearSelection = () => {
  selectedId.value = null
  selectedItem.value = null
  nutrition.value = {}
  inputWeight.value = 100
}

const confirmItem = () => {
  if (!selectedItem.value) {
    alert("请选择一个" + (currentType.value === 'food' ? "食材" : "菜品"))
    return
  }
  items.value[currentItemIndex.value] = {
    foodId: currentType.value === 'food' ? selectedId.value : null,
    dishId: currentType.value === 'dish' ? selectedId.value : null,
    foodName: selectedItem.value.name,
    weight: Number(inputWeight.value), // 确保是数字类型
    type: currentType.value
  }
  console.log("添加的项目:", items.value[currentItemIndex.value])
  dialogVisible.value = false
}

const addItem = () => {
  items.value.push({ foodId: null, dishId: null, foodName: "", weight: 100, type: "food" })
}

const removeItem = (index) => {
  items.value.splice(index, 1)
}

const submit = async () => {
  const data = {
    record: form.value,
    items: items.value.filter(item => item.foodId || item.dishId).map(item => ({
      foodId: item.foodId,
      dishId: item.dishId,
      type: item.type,
      weight: Number(item.weight) // 确保是数字类型
    }))
  }

  console.log("提交的数据:", data)

  if (data.items.length === 0) {
    alert("请至少添加一个食物")
    return
  }

  try {
    const response = await axios.post("http://localhost:8080/dietRecord/add", data)
    console.log("提交成功响应:", response)
    alert("提交成功！")
  } catch (e) {
    console.error("提交失败详情:", e)
    console.error("提交数据:", data)
    let errorMessage = "提交失败"
    if (e.response?.data) {
      errorMessage = typeof e.response.data === 'string' ? e.response.data : JSON.stringify(e.response.data)
    } else if (e.message) {
      errorMessage = e.message
    }
    alert("提交失败: " + errorMessage)
  }
}

onMounted(async () => {
  try {
    // 获取当前用户ID
    const userId = user.id || 1
    
    // 同时加载食物（包含营养）和当前用户的菜品列表
    const [foodRes, dishRes] = await Promise.all([
      axios.get("http://localhost:8080/food/listFull"),
      axios.get("http://localhost:8080/dish/listByCreator", {
        params: { creatorId: userId }
      })
    ])
    
    // 转换listFull返回的嵌套结构为扁平结构
    foodList.value = foodRes.data.map(item => ({
      ...item.food,
      calorie: item.nutrition?.calorie,
      protein: item.nutrition?.protein,
      fat: item.nutrition?.fat,
      carbohydrate: item.nutrition?.carbohydrate
    }))
    // 同时更新nutritionMap
    nutritionMap.value = {}
    foodRes.data.forEach(item => {
      if (item.food && item.nutrition) {
        nutritionMap.value[item.food.id] = item.nutrition
      }
    })
    dishList.value = Array.isArray(dishRes.data) ? dishRes.data : (dishRes.data.value || [])
  } catch (e) {
    console.error("获取数据失败", e)
  }
})
</script>

<style scoped>
.diet-record {
  max-width: 900px;
  margin: 0 auto;
}

.form-card,
.food-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  font-weight: 600;
}

.empty-tip {
  text-align: center;
  color: #999;
  padding: 30px 0;
}

.food-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 6px;
  margin-bottom: 10px;
}

.food-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 10px;
}

.food-type {
  background: #f97316;
  color: #fff;
  padding: 2px 6px;
  border-radius: 8px;
  font-size: 11px;
  margin-left: 8px;
}

.dialog-header {
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
}

.type-selector-wrapper {
  display: flex;
  gap: 8px;
  background: #f3f4f6;
  padding: 4px;
  border-radius: 24px;
}

.type-tab {
  padding: 8px 20px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
  color: #666;
}

.type-tab:hover {
  background: rgba(255, 255, 255, 0.5);
}

.type-tab.active {
  background: #fff;
  color: #f97316;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.2);
}

.search-wrapper {
  flex: 1;
  max-width: 250px;
}

.search-input {
  width: 100%;
}

.food-name {
  font-weight: 600;
  color: #333;
  font-size: 15px;
}

.food-weight {
  background: #f97316;
  color: #fff;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.submit-area {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.food-grid {
  max-height: 280px;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #d1d5db transparent;
}

.food-grid::-webkit-scrollbar {
  width: 6px;
}

.food-grid::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 3px;
}

.food-cell {
  padding: 12px 8px;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: relative;
}

.food-cell:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.food-cell.selected {
  background: linear-gradient(135deg, #ffedd5 0%, #ffd5b5 100%);
  box-shadow: 0 4px 16px rgba(249, 115, 22, 0.25);
}

.food-cell.selected .food-cell-name {
  color: #9a3412;
}

.food-cell.selected .food-cell-cal {
  color: #c2410c;
}

.check-mark {
  position: absolute;
  top: 6px;
  right: 6px;
  width: 24px;
  height: 24px;
  background: #f97316;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.3);
}

.food-cell-name {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #333;
}

.food-cell-cal {
  font-size: 11px;
  color: #888;
}

.food-cell-img {
  width: 65px;
  height: 65px;
  object-fit: cover;
  border-radius: 10px;
  margin: 0 auto 8px;
  display: block;
  transition: opacity 0.3s ease;
  opacity: 0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.food-cell-img.loaded {
  opacity: 1;
}

.food-cell-img-placeholder {
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  color: #9ca3af;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 65px;
  height: 65px;
  border-radius: 10px;
  margin: 0 auto 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.food-cell.selected .food-cell-img-placeholder {
  background: linear-gradient(135deg, #fed7aa 0%, #fdba74 100%);
  color: #9a3412;
}

.food-selector {
  display: flex;
  gap: 0;
  height: 420px;
  background: #f9fafb;
  border-radius: 12px;
  overflow: hidden;
}

.category-sidebar {
  width: 100px;
  background: transparent;
  padding: 12px 8px;
  flex-shrink: 0;
}

.category-item {
  padding: 10px 8px;
  text-align: center;
  border-radius: 20px;
  cursor: pointer;
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
  transition: all 0.2s;
}

.category-item:hover {
  background: rgba(249, 115, 22, 0.08);
  color: #c2410c;
}

.category-item.active {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  color: #fff;
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.3);
}

.food-content {
  flex: 1;
  padding: 12px 15px;
  background: #fff;
  border-radius: 12px;
  margin: 8px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.food-content .food-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  flex: 1;
  overflow-y: auto;
}

.nutrition-info {
  margin-top: 15px;
  padding: 18px;
  background: linear-gradient(135deg, #fffaf5 0%, #fff7ed 100%);
  border-radius: 12px;
  flex-shrink: 0;
  border: 1px solid #fed7aa;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.06);
}

.nutrition-title {
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 0;
  color: #431407;
}

.nutrition-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.nutrition-close {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #fef3c7;
  color: #92400e;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.nutrition-close:hover {
  background: #f97316;
  color: #fff;
}

.nutri-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 15px;
}

.nutri-card {
  background: white;
  padding: 14px 10px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
  transition: all 0.2s;
}

.nutri-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.nutri-icon {
  font-size: 24px;
  margin-bottom: 6px;
}

.nutri-label {
  font-size: 12px;
  color: #78350f;
  margin-bottom: 4px;
}

.nutri-value {
  font-size: 18px;
  font-weight: 800;
  color: #f97316;
}

.weight-input-area {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 0;
  padding: 14px;
  background: linear-gradient(135deg, #fffef5 0%, #fef8e8 100%);
  border-radius: 10px;
  border: 1px solid #f0e9d8;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.04);
}

.weight-label {
  font-size: 14px;
  color: #7a6b5a;
  font-weight: 600;
}

.weight-unit {
  font-size: 14px;
  color: #8b7355;
  font-weight: 600;
}

/* Food Dialog Styles */
.food-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  margin: 0;
  padding: 20px;
  border-radius: 16px 16px 0 0;
}

.food-dialog :deep(.el-dialog__title) {
  color: white;
  font-weight: 700;
  font-size: 18px;
}

.food-dialog :deep(.el-dialog__close) {
  color: white;
}

.food-dialog :deep(.el-dialog__body) {
  padding: 24px;
  background: #f9fafb;
}

.food-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}
</style>
