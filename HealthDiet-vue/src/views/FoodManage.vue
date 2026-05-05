<template>
  <div class="page">
    <el-card>

      <div class="header">
        <h2>食物管理</h2>
        <el-button type="primary" @click="openDialog()">+ 新增</el-button>
      </div>

      <el-table :data="tableData" border>

        <el-table-column label="图片" width="100">
          <template #default="scope">
            <img
              v-if="scope.row.food.imageUrl"
              :src="baseURL + scope.row.food.imageUrl"
              class="img"
            />
          </template>
        </el-table-column>

        <el-table-column label="名称">
          <template #default="scope">
            {{ scope.row.food.name }}
          </template>
        </el-table-column>

        <el-table-column label="分类">
          <template #default="scope">
            {{ scope.row.food.category }}
          </template>
        </el-table-column>

        <el-table-column label="热量">
          <template #default="scope">
            {{ scope.row.nutrition?.calorie || 0 }}
          </template>
        </el-table-column>

        <el-table-column label="蛋白质">
          <template #default="scope">
            {{ scope.row.nutrition?.protein || 0 }}
          </template>
        </el-table-column>

        <el-table-column label="脂肪">
          <template #default="scope">
            {{ scope.row.nutrition?.fat || 0 }}
          </template>
        </el-table-column>

        <el-table-column label="碳水">
          <template #default="scope">
            {{ scope.row.nutrition?.carbohydrate || 0 }}
          </template>
        </el-table-column>

        <el-table-column label="膳食纤维">
          <template #default="scope">
            {{ scope.row.nutrition?.fiber || 0 }}
          </template>
        </el-table-column>

        <el-table-column label="钠">
          <template #default="scope">
            {{ scope.row.nutrition?.sodium || 0 }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="remove(scope.row.food.id)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>

    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑食物' : '新增食物'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="请输入食物名称"/>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="主食" value="主食"/>
            <el-option label="肉类" value="肉类"/>
            <el-option label="水产" value="水产"/>
            <el-option label="蛋奶类" value="蛋奶类"/>
            <el-option label="蔬菜" value="蔬菜"/>
            <el-option label="水果" value="水果"/>
            <el-option label="坚果" value="坚果"/>
            <el-option label="调料" value="调料"/>
          </el-select>
        </el-form-item>
        <el-form-item label="图片地址">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL"/>
        </el-form-item>
        <el-form-item label="热量(kcal)">
          <el-input v-model="form.calorie" placeholder="请输入热量"/>
        </el-form-item>
        <el-form-item label="蛋白质(g)">
          <el-input v-model="form.protein" placeholder="请输入蛋白质"/>
        </el-form-item>
        <el-form-item label="脂肪(g)">
          <el-input v-model="form.fat" placeholder="请输入脂肪"/>
        </el-form-item>
        <el-form-item label="碳水(g)">
          <el-input v-model="form.carbohydrate" placeholder="请输入碳水"/>
        </el-form-item>
        <el-form-item label="膳食纤维(g)">
          <el-input v-model="form.fiber" placeholder="请输入膳食纤维"/>
        </el-form-item>
        <el-form-item label="钠(mg)">
          <el-input v-model="form.sodium" placeholder="请输入钠"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import axios from "axios"
import { ElMessage } from "element-plus"

const tableData = ref([])
const dialogVisible = ref(false)

const baseURL = "http://localhost:8080"

const form = ref({
  id: null,
  name: '',
  category: '',
  imageUrl: '',
  calorie: 0,
  protein: 0,
  fat: 0,
  carbohydrate: 0,
  fiber: 0,
  sodium: 0
})

const load = async () => {
  const res = await axios.get("http://localhost:8080/food/listFull")
  tableData.value = res.data
}

const openDialog = (row) => {
  dialogVisible.value = true
  if (row) {
    form.value = {
      id: row.food.id,
      name: row.food.name,
      category: row.food.category,
      imageUrl: row.food.imageUrl,
      calorie: row.nutrition?.calorie || 0,
      protein: row.nutrition?.protein || 0,
      fat: row.nutrition?.fat || 0,
      carbohydrate: row.nutrition?.carbohydrate || 0,
      fiber: row.nutrition?.fiber || 0,
      sodium: row.nutrition?.sodium || 0
    }
  } else {
    form.value = {
      id: null,
      name: '',
      category: '',
      imageUrl: '',
      calorie: 0,
      protein: 0,
      fat: 0,
      carbohydrate: 0,
      fiber: 0,
      sodium: 0
    }
  }
}

const save = async () => {
  if (!form.value.name || !form.value.category) {
    ElMessage.warning("请填写名称和分类")
    return
  }

  const data = {
    food: {
      id: form.value.id,
      name: form.value.name,
      category: form.value.category,
      imageUrl: form.value.imageUrl
    },
    nutrition: {
      calorie: form.value.calorie,
      protein: form.value.protein,
      fat: form.value.fat,
      carbohydrate: form.value.carbohydrate,
      fiber: form.value.fiber,
      sodium: form.value.sodium
    }
  }

  try {
    if (form.value.id) {
      await axios.put("http://localhost:8080/food/updateFull", data)
      ElMessage.success("修改成功")
    } else {
      await axios.post("http://localhost:8080/food/addFull", data)
      ElMessage.success("新增成功")
    }
    dialogVisible.value = false
    load()
  } catch (e) {
    console.error("保存失败", e)
    ElMessage.error("保存失败")
  }
}

const remove = async (id) => {
  try {
    await axios.delete("http://localhost:8080/food/delete/" + id)
    ElMessage.success("删除成功")
    load()
  } catch (e) {
    console.error("删除失败", e)
    ElMessage.error("删除失败")
  }
}

onMounted(() => {
  load()
})
</script>

<style scoped>
.page {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
}
</style>
