<template>
  <div class="recipe-create">
    <el-card>
      <template #header>
        <span>🍽 发布食谱</span>
      </template>

      <!-- 基本信息 -->
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item label="目标">
          <el-input v-model="form.healthGoal" placeholder="如：减脂 / 增肌" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" />
        </el-form-item>
      </el-form>

      <!-- 餐别 -->
      <div v-for="(meal, index) in meals" :key="index" class="meal-block">
        <div class="meal-header">
          <span>{{ meal.type }}</span>
          <el-button type="primary" size="small" @click="addItem(index)">
            + 添加
          </el-button>
        </div>

        <div v-for="(item, i) in meal.items" :key="i" class="food-item">
          <span>{{ item.foodName }}</span>
          <span>{{ item.weight }}g</span>
          <span v-if="item.type === 'dish'">[菜品]</span>

          <el-button size="small" @click="openDialog(index, i)">选择</el-button>
          <el-button size="small" type="danger" @click="removeItem(index, i)">删除</el-button>
        </div>
      </div>

      <div style="text-align:center;margin-top:20px;">
        <el-button @click="cancel">取消</el-button>
        <el-button type="success" @click="submit" :loading="loading">发布</el-button>
      </div>
    </el-card>

    <!-- 选择食物弹窗 -->
    <el-dialog v-model="dialogVisible" title="选择食材/菜品" width="600px" :append-to-body="true">
      <el-radio-group v-model="currentType" style="margin-bottom: 15px;">
        <el-radio-button label="food">食材</el-radio-button>
        <el-radio-button label="dish">菜品</el-radio-button>
      </el-radio-group>

      <div class="food-selector" v-if="currentType === 'food'">
        <div class="category-sidebar">
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

        <div class="food-content">
          <div class="food-grid">
            <div
              v-for="item in filteredFoods"
              :key="item.id"
              class="food-cell"
              :class="{ selected: selectedItemId === item.id }"
              @click="selectItem(item)"
            >
              <div class="food-cell-name">{{ item.name }}</div>
              <div class="food-cell-cal">
                {{ nutritionMap[item.id]?.calorie || '--' }}kcal/100g
              </div>
            </div>
          </div>

          <div v-if="selectedItem" class="nutrition-info">
            <div class="nutrition-title">{{ selectedItem.name }} 营养成分（每100g）</div>
            <el-row :gutter="10">
              <el-col :span="8">
                <div class="nutri-item">
                  <div class="nutri-label">碳水化合物</div>
                  <div class="nutri-value">{{ nutrition.carbohydrate || 0 }}g</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="nutri-item">
                  <div class="nutri-label">蛋白质</div>
                  <div class="nutri-value">{{ nutrition.protein || 0 }}g</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="nutri-item">
                  <div class="nutri-label">脂肪</div>
                  <div class="nutri-value">{{ nutrition.fat || 0 }}g</div>
                </div>
              </el-col>
            </el-row>
            <div class="weight-input-area">
              <span class="weight-label">使用重量：</span>
              <el-input-number v-model="inputWeight" :min="1" :max="10000" />
              <span class="weight-unit">g</span>
            </div>
          </div>
        </div>
      </div>

      <div class="dish-selector" v-else>
        <div class="food-grid">
          <div
            v-for="item in dishList"
            :key="item.id"
            class="food-cell"
            :class="{ selected: selectedItemId === item.id }"
            @click="selectItem(item)"
          >
            <div class="food-cell-name">{{ item.name }}</div>
            <div class="food-cell-cal">
              菜品
            </div>
          </div>
        </div>

        <div v-if="selectedItem" class="nutrition-info">
          <div class="nutrition-title">{{ selectedItem.name }}</div>
          <div class="weight-input-area">
            <span class="weight-label">使用重量：</span>
            <el-input-number v-model="inputWeight" :min="1" :max="10000" />
            <span class="weight-unit">g</span>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmItem">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue"
import { useRouter } from "vue-router"
import request from "@/utils/request"
import { ElMessage } from "element-plus"

const user = JSON.parse(localStorage.getItem("user") || "{}")
const router = useRouter()

// 表单
const form = ref({
  name: "",
  healthGoal: "",
  description: "",
  userId: user.id,
  creatorType: user.role === "NUTRITIONIST" ? "NUTRITIONIST" : "USER"
})

// 三餐结构
const meals = ref([
  { type: "早餐", items: [] },
  { type: "午餐", items: [] },
  { type: "晚餐", items: [] }
])

// 弹窗
const dialogVisible = ref(false)
const currentMealIndex = ref(0)
const currentItemIndex = ref(0)
const currentType = ref("food")

const selectedItem = ref(null)
const selectedItemId = ref(null)
const inputWeight = ref(100)

const foodList = ref([])
const dishList = ref([])
const nutrition = ref({})
const nutritionMap = ref({})

// 食材分类
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

const activeCategory = ref("主食")

const filteredFoods = computed(() => {
  if (!Array.isArray(foodList.value)) return []
  return foodList.value.filter(food => food.category === activeCategory.value)
})

// 打开弹窗
const openDialog = async (mealIndex, itemIndex) => {
  currentMealIndex.value = mealIndex
  currentItemIndex.value = itemIndex
  const item = meals.value[mealIndex].items[itemIndex]
  
  if (item.type === "food" && item.foodId) {
    selectedItemId.value = item.foodId
    selectedItem.value = foodList.value.find(f => f.id === item.foodId)
    inputWeight.value = item.weight || 100
    activeCategory.value = selectedItem.value?.category || "主食"
    await loadNutrition(item.foodId)
  } else if (item.type === "dish" && item.dishId) {
    selectedItemId.value = item.dishId
    selectedItem.value = dishList.value.find(d => d.id === item.dishId)
    inputWeight.value = item.weight || 100
  } else {
    selectedItemId.value = null
    selectedItem.value = null
    nutrition.value = {}
    inputWeight.value = 100
    activeCategory.value = "主食"
  }
  dialogVisible.value = true
}

// 分类变更
const onCategoryChange = () => {
  selectedItemId.value = null
  selectedItem.value = null
  nutrition.value = {}
}

// 加载营养成分
const loadNutrition = async (foodId) => {
  try {
    const res = await request.get(`/nutrition/${foodId}`)
    nutrition.value = res
  } catch (e) {
    console.error("获取营养失败", e)
  }
}

// 添加item
const addItem = (mealIndex) => {
  meals.value[mealIndex].items.push({
    foodId: null,
    dishId: null,
    foodName: "",
    weight: 100,
    type: "food"
  })
}

// 删除
const removeItem = (mealIndex, itemIndex) => {
  meals.value[mealIndex].items.splice(itemIndex, 1)
}

// 选择
const selectItem = async (item) => {
  selectedItemId.value = item.id
  selectedItem.value = item
  if (currentType.value === "food") {
    await loadNutrition(item.id)
  }
}

// 确认
const confirmItem = () => {
  if (!selectedItem.value) {
    return
  }
  const meal = meals.value[currentMealIndex.value]
  const item = meal.items[currentItemIndex.value]

  item.foodId = currentType.value === "food" ? selectedItem.value.id : null
  item.dishId = currentType.value === "dish" ? selectedItem.value.id : null
  item.foodName = selectedItem.value.name
  item.weight = inputWeight.value
  item.type = currentType.value

  dialogVisible.value = false
}

// 提交
const loading = ref(false)
const submit = async () => {
  if (!form.value.name) {
    ElMessage.warning("请输入食谱名称")
    return
  }
  
  const currentUser = JSON.parse(localStorage.getItem("user") || "{}")
  const dto = {
    name: form.value.name,
    description: form.value.description,
    userId: form.value.userId,
    creatorType: currentUser.role === "NUTRITIONIST" ? "NUTRITIONIST" : "USER",
    items: meals.value.flatMap(meal =>
      meal.items
        .filter(i => i.foodId || i.dishId)
        .map(i => ({
          foodId: i.foodId,
          dishId: i.dishId,
          type: i.type,
          mealType: meal.type,
          weight: i.weight
        }))
    )
  }

  if (dto.items.length === 0) {
    ElMessage.warning("请至少添加一种食材或菜品")
    return
  }

  console.log(dto)

  loading.value = true
  try {
    await request.post("/recipe/add", dto)
    ElMessage.success("发布成功")
    router.push({ name: "recipeCenter" })
  } catch (e) {
    console.error("发布失败", e)
    ElMessage.error("发布失败，请稍后重试")
  } finally {
    loading.value = false
  }
}

// 取消
const cancel = () => {
  router.push({ name: "recipeCenter" })
}

// 初始化
onMounted(async () => {
  try {
    const [f, d] = await Promise.all([
      request.get("/food/list"),
      request.get("/dish/list")
    ])
    console.log('Food API response:', f)
    console.log('Dish API response:', d)
    foodList.value = Array.isArray(f) ? f : []
    dishList.value = Array.isArray(d) ? d : []
    
    console.log('Food list after processing:', foodList.value)
    console.log('Dish list after processing:', dishList.value)

    // 加载营养成分
    for (const food of foodList.value) {
      try {
        const nutriRes = await request.get(`/nutrition/${food.id}`)
        nutritionMap.value[food.id] = nutriRes
      } catch (e) {
        console.error("获取营养失败", e)
      }
    }
  } catch (e) {
    console.error("加载数据失败", e)
    ElMessage.error("加载数据失败，请刷新页面重试")
  }
})
</script>

<style scoped>
.recipe-create {
  max-width: 900px;
  margin: 0 auto;
}

.meal-block {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.meal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: 600;
  color: #333;
}

.food-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 6px;
  margin-top: 8px;
}

.food-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  max-height: 280px;
  overflow-y: auto;
}

.food-cell {
  padding: 10px 8px;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  background: #fff;
}

.food-cell:hover {
  border-color: #409EFF;
  background: #ecf5ff;
}

.food-cell.selected {
  border-color: #409EFF;
  background: #409EFF;
  color: #fff;
}

.food-cell.selected .food-cell-cal {
  color: #fff;
}

.food-cell-name {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 4px;
}

.food-cell-cal {
  font-size: 11px;
  color: #999;
}

.food-selector {
  display: flex;
  gap: 0;
  height: 420px;
}

.category-sidebar {
  width: 100px;
  background: #f5f5f5;
  border-radius: 8px;
  padding: 8px;
  flex-shrink: 0;
}

.category-item {
  padding: 10px 8px;
  text-align: center;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
  transition: all 0.2s;
}

.category-item:hover {
  background: #e8e8e8;
}

.category-item.active {
  background: #304156;
  color: #fff;
}

.food-content {
  flex: 1;
  padding-left: 15px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.food-content .food-grid {
  flex: 1;
  overflow-y: auto;
}

.dish-selector {
  height: 420px;
  display: flex;
  flex-direction: column;
}

.dish-selector .food-grid {
  flex: 1;
  overflow-y: auto;
}

.nutrition-info {
  margin-top: 15px;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 8px;
  flex-shrink: 0;
}

.nutrition-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #333;
}

.nutri-item {
  background: #fff;
  padding: 12px;
  border-radius: 6px;
  text-align: center;
}

.nutri-label {
  font-size: 12px;
  color: #888;
  margin-bottom: 4px;
}

.nutri-value {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.weight-input-area {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 15px;
}

.weight-label {
  font-size: 14px;
  color: #333;
}

.weight-unit {
  font-size: 14px;
  color: #666;
}
</style>