<template>
  <div class="my-dish-manage">
    <el-card>
      <div class="header">
        <h2>🍳 我的菜品</h2>
        <el-button type="primary" @click="openDialog()">+ 新增</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="category" label="分类"/>
        <el-table-column label="图片" width="100">
          <template #default="scope">
            <img v-if="scope.row.imageUrl" :src="scope.row.imageUrl" class="img"/>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip/>

        <el-table-column label="操作" width="280">
          <template #default="scope">
            <div style="display: flex; gap: 8px; justify-content: center;">
              <el-button size="small" @click="viewDish(scope.row)">详情</el-button>
              <el-button size="small" type="primary" @click="openDialog(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="remove(scope.row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑菜品' : '新增菜品'" width="700px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="请输入菜品名称"/>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width: 100%" placeholder="请选择分类">
            <el-option label="热菜" value="热菜"/>
            <el-option label="凉菜" value="凉菜"/>
            <el-option label="汤" value="汤"/>
            <el-option label="小吃" value="小吃"/>
          </el-select>
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.imageUrl" placeholder="请输入图片地址"/>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" placeholder="请输入菜品描述"/>
        </el-form-item>
      </el-form>

      <!-- 食材列表 -->
      <div class="ingredients-section">
        <div class="ingredients-header">
          <h3>食材配料</h3>
          <el-button type="primary" size="small" @click="addItem">+ 添加食材</el-button>
        </div>

        <div v-if="form.items.length === 0" class="empty-ingredients">
          点击上方"添加食材"开始添加
        </div>

        <div v-for="(item, index) in form.items" :key="index" class="food-item">
          <div class="food-info" v-if="item.foodName">
            <span class="food-name">{{ item.foodName }}</span>
            <span class="food-weight" v-if="item.weight">{{ item.weight }}g</span>
          </div>
          <div class="food-actions">
            <el-button type="primary" plain size="small" @click="openFoodDialog(index)">
              {{ item.foodId ? '修改食材' : '选择食材' }}
            </el-button>
            <el-button type="danger" plain size="small" @click="removeIngredient(index)">删除</el-button>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>

    <!-- 食材选择弹窗 -->
    <el-dialog v-model="foodDialogVisible" title="选择食材" width="650px" :append-to-body="true" class="food-dialog">
      <!-- 搜索框 -->
      <div class="dialog-header">
        <div class="search-wrapper">
          <el-input
            v-model="searchQuery"
            placeholder="搜索食材..."
            clearable
            class="search-input"
            @input="onSearchChange"
          >
            <template #prefix>🔍</template>
          </el-input>
        </div>
      </div>

      <div class="food-selector">
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
              v-for="food in filteredFoods"
              :key="food.id"
              class="food-cell"
              :class="{ selected: selectedFoodId === food.id }"
              @click="selectFood(food)"
            >
              <div v-if="selectedFoodId === food.id" class="check-mark">✓</div>
              <img
                v-if="food.imageUrl"
                :src="baseURL + food.imageUrl"
                class="food-cell-img"
                loading="lazy"
                @load="(e) => e.target.classList.add('loaded')"
              />
              <div v-else class="food-cell-img food-cell-img-placeholder">暂无图片</div>
              <div class="food-cell-name">{{ food.name }}</div>
              <div class="food-cell-cal">
                {{ nutritionMap[food.id]?.calorie || '--' }}kcal/100g
              </div>
            </div>
          </div>

          <div v-if="selectedFood" class="nutrition-info">
            <div class="nutrition-header">
              <div class="nutrition-title">{{ selectedFood.name }} 营养成分（每100g）</div>
              <div class="nutrition-close" @click="clearSelection">✕</div>
            </div>
            <div class="nutri-grid">
              <div class="nutri-card carbs">
                <div class="nutri-icon">🍞</div>
                <div class="nutri-label">碳水化合物</div>
                <div class="nutri-value">{{ nutrition.carbohydrate || 0 }}g</div>
              </div>
              <div class="nutri-card protein">
                <div class="nutri-icon">🥩</div>
                <div class="nutri-label">蛋白质</div>
                <div class="nutri-value">{{ nutrition.protein || 0 }}g</div>
              </div>
              <div class="nutri-card fat">
                <div class="nutri-icon">🥑</div>
                <div class="nutri-label">脂肪</div>
                <div class="nutri-value">{{ nutrition.fat || 0 }}g</div>
              </div>
            </div>
            <div class="weight-input-area">
              <span class="weight-label">使用重量：</span>
              <el-input-number v-model="inputWeight" :min="1" :max="10000" />
              <span class="weight-unit">g</span>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="foodDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmFood" class="confirm-btn">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="菜品详情" width="600px">
      <div v-if="currentDish">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="名称">{{ currentDish.dish?.name || '-' }}</el-descriptions-item>
          <el-descriptions-item label="分类">{{ currentDish.dish?.category || '-' }}</el-descriptions-item>
          <el-descriptions-item label="图片" :span="2">
            <img v-if="currentDish.dish?.imageUrl" :src="currentDish.dish.imageUrl" class="detail-img"/>
          </el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ currentDish.dish?.description || '-' }}</el-descriptions-item>
        </el-descriptions>

        <div class="ingredients-section">
          <h3>食材配料</h3>
          <el-table :data="currentDish.items || []" border style="margin-top: 10px">
            <el-table-column prop="foodName" label="食材名称"/>
            <el-table-column prop="weight" label="重量(g)"/>
          </el-table>
          <div v-if="!currentDish.items || currentDish.items.length === 0" class="empty-ingredients">
            暂无食材信息
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue"
import axios from "axios"
import { ElMessage } from "element-plus"

const tableData = ref([])
const dialogVisible = ref(false)
const detailVisible = ref(false)
const foodDialogVisible = ref(false)
const form = ref({})
const currentDish = ref(null)

// 食材管理相关
const foodList = ref([])
const nutrition = ref({})
const nutritionMap = ref({})
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

const activeCategory = ref("主食")
const currentItemIndex = ref(-1)
const selectedFoodId = ref(null)
const selectedFood = ref(null)
const inputWeight = ref(100)
const searchQuery = ref("")

const filteredFoods = computed(() => {
  let list = foodList.value.filter(food => food.category === activeCategory.value)
  if (searchQuery.value) {
    list = list.filter(food => food.name.includes(searchQuery.value))
  }
  return list
})

const onCategoryChange = () => {
  selectedFoodId.value = null
  selectedFood.value = null
  nutrition.value = {}
}

const onSearchChange = () => {
  selectedFoodId.value = null
  selectedFood.value = null
}

const openFoodDialog = async (index) => {
  currentItemIndex.value = index
  const item = form.value.items[index]
  if (item.foodId) {
    selectedFoodId.value = item.foodId
    selectedFood.value = foodList.value.find(f => f.id === item.foodId)
    inputWeight.value = item.weight || 100
    activeCategory.value = selectedFood.value?.category || "主食"
    await loadNutrition(item.foodId)
  } else {
    selectedFoodId.value = null
    selectedFood.value = null
    nutrition.value = {}
    inputWeight.value = 100
    activeCategory.value = "主食"
  }
  foodDialogVisible.value = true
}

const loadNutrition = async (foodId) => {
  try {
    const res = await axios.get(baseURL + "/nutrition/" + foodId)
    nutrition.value = res.data
  } catch (e) {
    console.error("获取营养失败", e)
  }
}

const selectFood = async (food) => {
  selectedFoodId.value = food.id
  selectedFood.value = food
  await loadNutrition(food.id)
}

const clearSelection = () => {
  selectedFoodId.value = null
  selectedFood.value = null
  inputWeight.value = 100
}

const confirmFood = () => {
  if (!selectedFood.value) {
    return
  }
  form.value.items[currentItemIndex.value] = {
    foodId: selectedFoodId.value,
    foodName: selectedFood.value.name,
    weight: inputWeight.value
  }
  foodDialogVisible.value = false
}

const addItem = () => {
  form.value.items.push({ foodId: "", foodName: "", weight: "" })
}

const removeIngredient = (index) => {
  form.value.items.splice(index, 1)
}

const openDialog = async (row) => {
  if (row) {
    // 编辑时先获取菜品详情，包含食材列表
    try {
      const res = await axios.get("http://localhost:8080/dish/detail", {
        params: { id: row.id }
      })
      const detail = res.data
      // 构建食材列表，包含 foodName
      const items = (detail.items || []).map(item => ({
        foodId: item.foodId,
        foodName: item.foodName || foodList.value.find(f => f.id === item.foodId)?.name || '未知',
        weight: item.weight
      }))
      form.value = {
        id: detail.dish?.id || row.id,
        name: detail.dish?.name || row.name,
        category: detail.dish?.category || row.category,
        imageUrl: detail.dish?.imageUrl || row.imageUrl,
        description: detail.dish?.description || row.description,
        items: items
      }
    } catch (e) {
      console.error("获取菜品详情失败", e)
      form.value = { ...row, items: [] }
    }
  } else {
    form.value = {
      name: "",
      category: "",
      imageUrl: "",
      description: "",
      items: []
    }
  }
  dialogVisible.value = true
}

const save = async () => {
  if (!form.value.name || !form.value.category) {
    ElMessage.error("请填写菜品名称和分类")
    return
  }

  const user = JSON.parse(localStorage.getItem("user") || "{}")
  const userId = user.id || 1

  const data = {
    dish: {
      ...form.value,
      creatorId: userId,
      creatorRole: user.role || "USER"
    },
    items: form.value.items.filter(item => item.foodId)
  }

  if (data.items.length === 0) {
    ElMessage.error("请至少添加一个食材")
    return
  }

  try {
    if (form.value.id) {
      await axios.put("http://localhost:8080/dish/update", data)
      ElMessage.success("更新成功")
    } else {
      await axios.post("http://localhost:8080/dish/addFull", data)
      ElMessage.success("创建成功")
    }
    dialogVisible.value = false
    loadMyDishes()
  } catch (e) {
    console.error("保存失败", e)
    ElMessage.error("保存失败")
  }
}

const remove = async (id) => {
  try {
    await axios.delete("http://localhost:8080/dish/delete", {
      params: { id: id }
    })
    ElMessage.success("删除成功")
    loadMyDishes()
  } catch (e) {
    console.error("删除失败", e)
    ElMessage.error("删除失败")
  }
}

const viewDish = async (row) => {
  try {
    const res = await axios.get("http://localhost:8080/dish/detail", {
      params: { id: row.id }
    })
    currentDish.value = res.data
    detailVisible.value = true
  } catch (e) {
    console.error("获取详情失败", e)
  }
}

const loadMyDishes = async () => {
  const user = JSON.parse(localStorage.getItem("user") || "{}")
  const userId = user.id || 1
  
  try {
    const res = await axios.get("http://localhost:8080/dish/listByCreator", {
      params: { creatorId: userId }
    })
    tableData.value = res.data
  } catch (e) {
    console.error("获取菜品失败", e)
  }
}

onMounted(async () => {
  try {
    const res = await axios.get("http://localhost:8080/food/listFull")
    // 转换listFull返回的嵌套结构为扁平结构
    foodList.value = res.data.map(item => ({
      ...item.food,
      calorie: item.nutrition?.calorie,
      protein: item.nutrition?.protein,
      fat: item.nutrition?.fat,
      carbohydrate: item.nutrition?.carbohydrate
    }))
    // 同时更新nutritionMap
    nutritionMap.value = {}
    res.data.forEach(item => {
      if (item.food && item.nutrition) {
        nutritionMap.value[item.food.id] = item.nutrition
      }
    })

    loadMyDishes()
  } catch (e) {
    console.error("获取食物失败", e)
  }
})
</script>

<style scoped>
.my-dish-manage {
  padding: 20px;
  background: linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%);
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
  color: #333;
  font-weight: 600;
}

.img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
}

.detail-img {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  border-radius: 8px;
}

.ingredients-section {
  margin-top: 20px;
}

.ingredients-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.ingredients-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.empty-ingredients {
  text-align: center;
  color: #999;
  padding: 20px;
  background: #fffaf5;
  border-radius: 8px;
  border: 1px dashed #fed7aa;
}

.food-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #fff7ed;
  border-radius: 8px;
  margin-bottom: 8px;
  border: 1px solid #fed7aa;
}

.food-actions {
  display: flex;
  gap: 8px;
}

.food-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.food-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.food-weight {
  font-size: 12px;
  color: #666;
  background: #ffedd5;
  padding: 2px 8px;
  border-radius: 4px;
}

/* 食材选择弹窗样式 */
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