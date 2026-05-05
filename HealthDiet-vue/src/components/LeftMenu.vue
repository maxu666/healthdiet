<template>
  <el-menu
  :default-active="$route.path"
  background-color="#fff7ed"
  text-color="#7c2d12"
  active-text-color="#ffffff"
  router
>
    <el-menu-item v-if="user.role == 'USER'" index="/index/welcome">
      <el-icon><House /></el-icon>
      <span>首页</span>
    </el-menu-item>
    <el-menu-item v-if="user.role === 'NUTRITIONIST'" index="/index/nutritionistDashboard">
      <el-icon><TrendCharts /></el-icon>
      <span>首页</span>
    </el-menu-item>
    <el-menu-item v-if="user.role == 'USER'" index="/index/diet">
      <el-icon><Edit /></el-icon>
      <span>饮食记录</span>
    </el-menu-item>
    <el-menu-item v-if="user.role == 'USER'" index="/index/recordList">
      <el-icon><Search /></el-icon>
       <span>饮食记录查询</span>
    </el-menu-item>
    <el-menu-item v-if="user.role == 'USER'" index="/index/healthTrend">
      <el-icon><TrendCharts /></el-icon>
      <span>健康趋势</span>
    </el-menu-item>
    <el-menu-item v-if="user.role !== 'ADMIN'" index="/index/myDishManage">
      <el-icon><SetUp /></el-icon>
      <span>我的菜品</span>
    </el-menu-item>
    
    <el-menu-item v-if="user.role !== 'ADMIN'" index="/index/recipeCenter">
      <el-icon><Notebook /></el-icon>
      <span>食谱中心</span>
    </el-menu-item>
    <el-menu-item v-if="user.role === 'USER' || user.role === 'NUTRITIONIST'" index="/index/chat">
      <el-icon><ChatDotRound /></el-icon>
      <span>营养咨询</span>
    </el-menu-item>
    <el-menu-item v-if="user.role === 'ADMIN'" index="/userManage">
      <span>用户管理</span>
    </el-menu-item>
    <el-menu-item v-if="user.role === 'ADMIN'" index="/foodManage">
      <span>食物管理</span>
    </el-menu-item>
    <el-menu-item v-if="user.role === 'ADMIN'" index="/dishManage">
      <span>菜品管理</span>
    </el-menu-item>
    <el-menu-item v-if="user.role === 'ADMIN'" index="/announcementManage">
      <span>公告管理</span>
    </el-menu-item>
    <el-menu-item v-if="user.role === 'ADMIN'" index="/healthNewsManage">
      <span>健康资讯管理</span>
    </el-menu-item>
    
  </el-menu>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { House, Edit, Search, TrendCharts, SetUp, ChatDotRound, Notebook } from '@element-plus/icons-vue'

const $route = useRoute()
const user = ref(JSON.parse(localStorage.getItem("user") || "{}"))
</script>

<style scoped>
.el-menu {
  border-right: none;
}

.el-menu {
  border-right: none;
}

/* 普通项 */
.el-menu-item {
  border-radius: 8px;
  margin: 6px 10px;
  transition: all 0.2s;
}

.el-menu-item {
  background-color: #f4c07d;
}
/* hover效果 */
.el-menu-item:hover {
  background-color: #e3a95c;
}

/* 选中项（核心🔥） */
.el-menu-item.is-active {
  background: linear-gradient(90deg, #f97316, #fb923c);
  color: #fff !important;
}
</style>