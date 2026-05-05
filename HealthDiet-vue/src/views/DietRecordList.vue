<template>
  <div class="diet-record">
    <div class="page-header">
      <div class="header-content">
        <h1> 饮食记录查询</h1>
        <p class="subtitle">记录每日饮食，管理健康生活</p>
      </div>
    </div>

    <el-card class="record-card">
      <div class="record-list">
        <div v-for="dateItem in groupList" :key="dateItem.date" class="date-block">
          <div class="date-header">
            <div class="date-badge">
              <span class="date-icon"></span>
              <span class="date-text">{{ dateItem.date }}</span>
            </div>
          </div>

          <div v-for="meal in dateItem.meals" :key="meal.mealType" class="meal-block">
            <div class="meal-header">
              <div class="meal-info">
                <span class="meal-icon">{{ getMealIcon(meal.mealType) }}</span>
                <span class="meal-type">{{ meal.mealType }}</span>
                <span class="meal-cal-badge">{{ meal.totalCalorie.toFixed(0) }} kcal</span>
              </div>
              <div class="meal-actions">
                <el-button size="small" type="primary" plain @click="openEditDialog(dateItem.date, meal)">
                  <span class="btn-icon">✏️</span> 编辑
                </el-button>
                <el-button size="small" type="danger" plain @click="deleteMeal(dateItem.date, meal.mealType)">
                  <span class="btn-icon">🗑️</span> 删除
                </el-button>
              </div>
            </div>

            <div class="food-list">
              <div v-for="(food, index) in meal.foods" :key="index" class="food-item">
                <div class="food-info">
                  <span class="food-name">{{ food.foodName || '未知食物' }}</span>
                  <span class="food-tag" v-if="food.dishId">菜品</span>
                </div>
                <div class="food-details">
                  <span class="food-weight">
                    <span class="detail-icon"></span>
                    {{ parseFloat(food.weight || 0).toFixed(1) }}g
                  </span>
                  <span class="food-kcal">
                    <span class="detail-icon">🔥</span>
                    {{ parseFloat(food.calorie || 0).toFixed(0) }}kcal
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <el-empty v-if="groupList.length === 0" description="暂无饮食记录" class="empty-state" />
      </div>
    </el-card>

    <!-- 编辑饮食记录 -->
    <el-dialog v-model="dialogVisible" title="编辑饮食记录" width="700px" class="edit-dialog">
      <el-form :model="editForm" label-width="80px" class="edit-form">
        <el-form-item label="日期">
          <el-date-picker
            v-model="editForm.date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="餐别">
          <el-select v-model="editForm.mealType" placeholder="选择餐别" style="width: 100%">
            <el-option label="🌅 早餐" value="早餐" />
            <el-option label="☀️ 午餐" value="午餐" />
            <el-option label="🌙 晚餐" value="晚餐" />
            <el-option label="🍪 加餐" value="加餐" />
          </el-select>
        </el-form-item>
        <div class="ingredients-section">
          <div class="ingredients-header">
            <h3>食物清单</h3>
            <el-button type="primary" size="small" @click="openFoodDialog">
              + 添加食物/菜品
            </el-button>
          </div>
          <div class="edit-food-list">
            <div v-for="(item, index) in editForm.items" :key="index" class="edit-food-item">
              <div class="edit-food-info">
                <span class="edit-food-name">{{ item.foodName }}</span>
                <span class="edit-food-type" v-if="item.type === 'dish'">菜品</span>
              </div>
              <div class="edit-food-controls">
                <el-input-number v-model="item.weight" :min="0" :max="10000" :step="10" size="small" />
                <span class="weight-unit">g</span>
                <el-button size="small" type="danger" plain @click="removeEditItem(index)">删除</el-button>
              </div>
            </div>
            <div v-if="editForm.items.length === 0" class="no-foods">
              点击上方"添加食物/菜品"开始添加
            </div>
          </div>
        </div>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="submitEdit" class="save-btn">保存修改</el-button>
      </template>
    </el-dialog>

    <!-- 选择食材/菜品弹窗 -->
    <el-dialog v-model="foodDialogVisible" title="选择食材/菜品" width="750px" class="food-dialog">
      <div class="dialog-header">
        <div class="type-selector-wrapper">
          <div
            class="type-tab"
            :class="{ active: currentType === 'food' }"
            @click="currentType = 'food'; onCategoryChange()"
          >
            🥗 食材
          </div>
          <div
            class="type-tab"
            :class="{ active: currentType === 'dish' }"
            @click="currentType = 'dish'; onCategoryChange()"
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
              />
              <div v-else class="food-cell-img food-cell-img-placeholder">暂无图片</div>
              <div class="food-cell-name">{{ item.name }}</div>
              <div class="food-cell-cal">
                {{ currentType === 'food' ? (nutritionMap[item.id]?.calorie || '--') + 'kcal' : (item.category || '--') }}
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
                <div class="nutri-value">{{ nutritionMap[selectedItem.id]?.carbohydrate || 0 }}g</div>
              </div>
              <div class="nutri-card">
                <div class="nutri-icon">🥩</div>
                <div class="nutri-label">蛋白质</div>
                <div class="nutri-value">{{ nutritionMap[selectedItem.id]?.protein || 0 }}g</div>
              </div>
              <div class="nutri-card">
                <div class="nutri-icon">🥑</div>
                <div class="nutri-label">脂肪</div>
                <div class="nutri-value">{{ nutritionMap[selectedItem.id]?.fat || 0 }}g</div>
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
        <el-button @click="foodDialogVisible = false" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="confirmFoodItem" class="confirm-btn">确认添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from "vue"
import axios from "axios"
import { ElMessage } from "element-plus"

const groupList = ref([])
const dialogVisible = ref(false)
const foodDialogVisible = ref(false)
const editForm = reactive({
  date: "",
  mealType: "",
  items: []
})
const originalDate = ref("")
const originalMealType = ref("")

const nutritionMap = ref({})
const foodList = ref([])
const dishList = ref([])
const selectedId = ref(null)
const selectedItem = ref(null)
const inputWeight = ref(100)
const currentType = ref("food")
const searchQuery = ref("")
const baseURL = "http://localhost:8080"

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

const activeCategory = ref("主食")

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
}

const getMealIcon = (mealType) => {
  const icons = {
    "早餐": "🌅",
    "午餐": "☀️",
    "晚餐": "🌙",
    "加餐": "🍪"
  }
  return icons[mealType] || "🍽️"
}

const getCategoryIcon = (category) => {
  const icons = {
    "主食": "🍚",
    "肉类": "🥩",
    "水产": "🐟",
    "蛋奶类": "🥛",
    "蔬菜": "🥬",
    "水果": "🍎",
    "坚果": "🥜",
    "调料": "🧂"
  }
  return icons[category] || "🍽️"
}

const getDishCategoryIcon = (category) => {
  const icons = {
    "热菜": "🔥",
    "凉菜": "🥗",
    "汤": "🍲",
    "小吃": "🍟"
  }
  return icons[category] || "🍽️"
}

const onCategoryChange = () => {
  selectedId.value = null
  selectedItem.value = null
}

onMounted(async () => {
  const user = JSON.parse(localStorage.getItem("user") || "{}")
  const userId = user.id || 1

  try {
    const res = await axios.get(
      "http://localhost:8080/dietRecord/group",
      { params: { userId: userId } }
    )
    groupList.value = res.data
  } catch (e) {
    console.error("获取记录失败", e)
  }

  // 预加载食物数据（包含营养信息）
  try {
    const foodRes = await axios.get("http://localhost:8080/food/listFull")
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

    const dishRes = await axios.get("http://localhost:8080/dish/listByCreator", {
      params: { creatorId: userId }
    })
    dishList.value = dishRes.data
  } catch (e) {
    console.error("预加载食物数据失败", e)
  }
})

const openEditDialog = (date, meal) => {
  originalDate.value = date
  originalMealType.value = meal.mealType
  editForm.date = date
  editForm.mealType = meal.mealType
  editForm.items = meal.foods.map(f => ({
    foodId: f.dishId ? null : f.foodId,
    dishId: f.dishId || null,
    foodName: f.foodName,
    weight: parseFloat(f.weight),
    type: f.dishId ? "dish" : "food"
  }))
  dialogVisible.value = true
}

const openFoodDialog = () => {
  selectedId.value = null
  selectedItem.value = null
  inputWeight.value = 100
  currentType.value = "food"
  foodDialogVisible.value = true
}

const selectItem = (item) => {
  selectedId.value = Number(item.id)
  selectedItem.value = item
}

const clearSelection = () => {
  selectedId.value = null
  selectedItem.value = null
  inputWeight.value = 100
}

const confirmFoodItem = () => {
  if (!selectedItem.value) {
    ElMessage.warning("请选择一个食材")
    return
  }
  if (!inputWeight.value || inputWeight.value <= 0) {
    ElMessage.warning("请输入有效重量")
    return
  }

  editForm.items.push({
    foodId: currentType.value === "food" ? selectedId.value : null,
    dishId: currentType.value === "dish" ? selectedId.value : null,
    foodName: selectedItem.value.name,
    weight: Number(inputWeight.value),
    type: currentType.value
  })

  foodDialogVisible.value = false
}

const removeEditItem = (index) => {
  editForm.items.splice(index, 1)
}

const submitEdit = async () => {
  if (!editForm.date) {
    ElMessage.warning("请选择日期")
    return
  }
  if (!editForm.mealType) {
    ElMessage.warning("请选择餐别")
    return
  }
  if (editForm.items.length === 0) {
    ElMessage.warning("请添加至少一个食物")
    return
  }

  const user = JSON.parse(localStorage.getItem("user") || "{}")
  const userId = user.id || 1

  const dto = {
    record: {
      userId: userId,
      recordDate: editForm.date,
      mealType: editForm.mealType
    },
    items: editForm.items.map(item => ({
      foodId: item.foodId,
      dishId: item.dishId,
      weight: Number(item.weight),
      type: item.type
    }))
  }

  try {
    await axios.put(
      `http://localhost:8080/dietRecord/update?userId=${userId}&date=${originalDate.value}&mealType=${originalMealType.value}`,
      dto
    )
    ElMessage.success("更新成功")
    dialogVisible.value = false
    const res = await axios.get(
      "http://localhost:8080/dietRecord/group",
      { params: { userId: userId } }
    )
    groupList.value = res.data
  } catch (e) {
    ElMessage.error("更新失败: " + e.message)
  }
}

const deleteMeal = async (date, mealType) => {
  const user = JSON.parse(localStorage.getItem("user") || "{}")
  const userId = user.id || 1

  try {
    await axios.delete(
      `http://localhost:8080/dietRecord/delete?userId=${userId}&date=${date}&mealType=${mealType}`
    )
    ElMessage.success("删除成功")
    const res = await axios.get(
      "http://localhost:8080/dietRecord/group",
      { params: { userId: userId } }
    )
    groupList.value = res.data
  } catch (e) {
    ElMessage.error("删除失败: " + e.message)
  }
}
</script>

<style scoped>
.diet-record {
  padding: 20px;
  background: linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%);
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 24px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}

.header-content h1 {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.subtitle {
  font-size: 14px;
  color: #888;
  margin: 0;
}

.record-card {
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.record-list {
  padding: 8px;
}

.date-block {
  margin-bottom: 24px;
  background: #fffaf5;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid #fed7aa;
}

.date-header {
  margin-bottom: 16px;
}

.date-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 500;
}

.date-icon {
  font-size: 16px;
}

.date-text {
  font-size: 15px;
}

.meal-block {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid #fed7aa;
}

.meal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px dashed #fed7aa;
}

.meal-actions {
  display: flex;
  gap: 8px;
}

.meal-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.meal-icon {
  font-size: 20px;
}

.meal-type {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.meal-cal-badge {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 500;
}

.btn-icon {
  margin-right: 4px;
}

.food-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.food-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #fff7ed;
  border-radius: 8px;
  transition: all 0.2s;
}

.food-item:hover {
  background: #ffedd5;
}

.food-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.food-name {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.food-tag {
  background: #ffedd5;
  color: #ea580c;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.food-details {
  display: flex;
  gap: 16px;
}

.food-weight,
.food-kcal {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #666;
}

.detail-icon {
  font-size: 12px;
}

.empty-state {
  padding: 60px 0;
}

/* Edit Dialog Styles */
.edit-form :deep(.el-form-item__label) {
  font-weight: 500;
}

.ingredients-section {
  margin-bottom: 20px;
}

.ingredients-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e8e8e8;
}

.ingredients-header h3 {
  margin: 0;
  font-size: 14px;
  color: #333;
  font-weight: 600;
}

.edit-food-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.edit-food-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #fff7ed;
  border-radius: 8px;
  border: 1px solid #fed7aa;
}

.edit-food-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.edit-food-name {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.edit-food-type {
  background: #ffedd5;
  color: #ea580c;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.edit-food-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.weight-unit {
  color: #888;
  font-size: 14px;
}

.no-foods {
  text-align: center;
  color: #999;
  padding: 20px;
  background: #fffaf5;
  border-radius: 8px;
  border: 1px dashed #fed7aa;
}

.add-food-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
  border-radius: 8px;
}

.cancel-btn,
.save-btn {
  padding: 10px 24px;
  border-radius: 8px;
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

.dialog-header {
  margin-bottom: 16px;
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
}

.food-content .food-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  flex: 1;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #d1d5db transparent;
}

.food-content .food-grid::-webkit-scrollbar {
  width: 6px;
}

.food-content .food-grid::-webkit-scrollbar-thumb {
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
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
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

.confirm-btn {
  padding: 10px 28px;
  border-radius: 8px;
  font-size: 15px;
}
</style>