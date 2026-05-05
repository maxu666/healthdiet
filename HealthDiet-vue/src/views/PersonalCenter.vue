<template>
  <div class="personal-center">
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button v-if="!isEditing" type="primary" size="small" @click="isEditing = true">
            修改信息
          </el-button>
          <el-button v-else size="small" @click="cancelEdit">取消</el-button>
        </div>
      </template>

      <!-- 信息展示模式 -->
      <div v-if="!isEditing" class="info-grid">
        <div class="info-item">
          <div class="info-label">用户名</div>
          <div class="info-value">{{ form.username || '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">昵称</div>
          <div class="info-value">{{ form.nickname || '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">性别</div>
          <div class="info-value">{{ form.gender === 1 ? '男' : form.gender === 0 ? '女' : '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">年龄</div>
          <div class="info-value">{{ form.age || '-' }}岁</div>
        </div>
        <div class="info-item">
          <div class="info-label">身高</div>
          <div class="info-value">{{ form.height || '-' }}cm</div>
        </div>
        <div class="info-item">
          <div class="info-label">体重</div>
          <div class="info-value">{{ form.weight || '-' }}kg</div>
        </div>
        <div class="info-item">
          <div class="info-label">健康目标</div>
          <div class="info-value">{{ form.healthGoal || '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">饮食偏好</div>
          <div class="info-value">{{ form.dietPreference || '-' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">活动水平</div>
          <div class="info-value">{{ getActivityLabel(form.activityLevel) || '-' }}</div>
        </div>
      </div>

      <!-- 修改模式 -->
      <div v-else class="edit-form">
        <el-form :model="form" label-width="80px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名">
                <el-input v-model="form.username" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="昵称">
                <el-input v-model="form.nickname" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="年龄">
                <el-input v-model="form.age" placeholder="请输入年龄" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="性别">
                <el-select v-model="form.gender" style="width: 100%">
                  <el-option label="男" :value="1" />
                  <el-option label="女" :value="0" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="身高(cm)">
                <el-input v-model="form.height" placeholder="请输入身高" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="体重(kg)">
                <el-input v-model="form.weight" placeholder="请输入体重" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="健康目标">
            <el-radio-group v-model="form.healthGoal">
              <el-radio label="维持" />
              <el-radio label="减脂" />
              <el-radio label="增肌" />
            </el-radio-group>
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

          <el-form-item label="饮食偏好">
            <el-input v-model="form.dietPreference" placeholder="例如：素食 / 清真" />
          </el-form-item>

          <div class="btn-area">
            <el-button type="primary" size="large" @click="updateUser">保存修改</el-button>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue"
import axios from "axios"

const isEditing = ref(false)
const form = ref({
  username: '',
  nickname: '',
  gender: '',
  age: '',
  height: '',
  weight: '',
  healthGoal: '',
  dietPreference: '',
  activityLevel: ''
})

const user = JSON.parse(localStorage.getItem("user") || "{}")

const activityMap = {
  "久坐": "久坐",
  "轻度": "轻度",
  "中度": "中度",
  "重度": "重度",
  "运动员": "运动员"
}

const getActivityLabel = (val) => activityMap[val] || val

onMounted(async () => {
  try {
    const res = await axios.get("http://localhost:8080/user/" + user.id)
    form.value = res.data
  } catch (e) {
    console.error("获取用户失败", e)
  }
})

const cancelEdit = () => {
  isEditing.value = false
}

const updateUser = async () => {
  try {
    await axios.put("http://localhost:8080/user/update", form.value)
    alert("修改成功")
    localStorage.setItem("user", JSON.stringify(form.value))
    isEditing.value = false
  } catch (e) {
    console.error("更新失败", e)
    alert("更新失败")
  }
}
</script>

<style scoped>
.personal-center {
  max-width: 900px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.info-item {
  padding: 15px;
  background: #f9fafb;
  border-radius: 8px;
}

.info-label {
  font-size: 13px;
  color: #999;
  margin-bottom: 5px;
}

.info-value {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.edit-form {
  padding: 10px 0;
}

.btn-area {
  text-align: center;
  margin-top: 30px;
}
</style>
