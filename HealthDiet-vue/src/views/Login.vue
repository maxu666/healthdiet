<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="title">系统登录</h2>

      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>

        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="用户" value="USER" />
            <el-option label="营养师" value="NUTRITIONIST" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>

      <div class="btn-wrapper">
        <el-button type="primary" class="login-btn" @click="login">
          登录
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from "vue"
import request from "@/utils/request"
import { useRouter } from "vue-router"

const router = useRouter()

const form = ref({
  username: "",
  password: "",
  role: "USER"
})

const login = async () => {
  try {
    const res = await request.post("/login", form.value)

    localStorage.setItem("user", JSON.stringify(res))

    alert("登录成功")

    if (res.role === "ADMIN") {
      router.push("/userManage")
    } else if (res.role === "NUTRITIONIST") {
      router.push("/index/nutritionistDashboard")
    } else {
      router.push("/index")
    }
  } catch (e) {
    alert("用户名或密码错误")
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url("/login_background.png") no-repeat center center;
  background-size: cover;
}

.login-card {
  padding: 30px 40px;
  border-radius: 10px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.btn-wrapper {
  text-align: center;
  margin-top: 10px;
}

.login-btn {
  width: 60px;
}
</style>