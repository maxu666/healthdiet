<template>
  <div class="page">
    <el-card>

      <div class="header">
        <h2>用户管理</h2>
        <el-button type="primary" @click="openDialog()">+ 新增</el-button>
      </div>

      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column prop="nickname" label="昵称"/>
        <el-table-column label="性别">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : scope.row.gender === 0 ? '女' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄"/>
        <el-table-column prop="height" label="身高(cm)"/>
        <el-table-column prop="weight" label="体重(kg)"/>
        <el-table-column prop="healthGoal" label="健康目标"/>
        <el-table-column prop="activityLevel" label="活动水平"/>
        <el-table-column prop="role" label="角色"/>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" :value="1"/>
            <el-option label="女" :value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age" placeholder="请输入年龄"/>
        </el-form-item>
        <el-form-item label="身高(cm)">
          <el-input v-model="form.height" placeholder="请输入身高"/>
        </el-form-item>
        <el-form-item label="体重(kg)">
          <el-input v-model="form.weight" placeholder="请输入体重"/>
        </el-form-item>
        <el-form-item label="健康目标">
          <el-select v-model="form.healthGoal" style="width: 100%">
            <el-option label="健康" value="健康"/>
            <el-option label="减脂" value="减脂"/>
            <el-option label="增肌" value="增肌"/>
          </el-select>
        </el-form-item>
        <el-form-item label="活动水平">
          <el-select v-model="form.activityLevel" style="width: 100%">
            <el-option label="久坐（少运动）" value="久坐" />
            <el-option label="轻度（每周1-3天）" value="轻度活动" />
            <el-option label="中度（每周3-5天）" value="中度活动" />
            <el-option label="重度（每周6-7天）" value="高度活动" />
            <el-option label="极高度活动（每天高强度训练或职业运动员）" value="高强度" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="普通用户" value="USER"/>
            <el-option label="管理员" value="ADMIN"/>
          </el-select>
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

const form = ref({
  id: null,
  username: '',
  password: '',
  nickname: '',
  gender: '',
  age: '',
  height: '',
  weight: '',
  healthGoal: '健康',
  activityLevel: '久坐',
  role: 'USER'
})

const load = async () => {
  try {
    const res = await axios.get("http://localhost:8080/user/list")
    tableData.value = res.data
  } catch (e) {
    console.error("获取用户列表失败", e)
  }
}

const openDialog = (row) => {
  dialogVisible.value = true
  if (row) {
    form.value = {
      id: row.id,
      username: row.username,
      password: '',
      nickname: row.nickname || '',
      gender: row.gender,
      age: row.age || '',
      height: row.height || '',
      weight: row.weight || '',
      healthGoal: row.healthGoal || '健康',
      activityLevel: row.activityLevel || '久坐',
      role: row.role || 'USER'
    }
  } else {
    form.value = {
      id: null,
      username: '',
      password: '',
      nickname: '',
      gender: '',
      age: '',
      height: '',
      weight: '',
      healthGoal: '健康',
      activityLevel: '久坐',
      role: 'USER'
    }
  }
}

const save = async () => {
  if (!form.value.username) {
    ElMessage.warning("请填写用户名")
    return
  }
  if (!form.value.id && !form.value.password) {
    ElMessage.warning("请填写密码")
    return
  }

  try {
    const data = { ...form.value }
    if (!data.password) {
      delete data.password
    }

    if (form.value.id) {
      await axios.put("http://localhost:8080/user/update", data)
      ElMessage.success("修改成功")
    } else {
      await axios.post("http://localhost:8080/user/add", data)
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
    await axios.delete("http://localhost:8080/user/delete/" + id)
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
</style>
