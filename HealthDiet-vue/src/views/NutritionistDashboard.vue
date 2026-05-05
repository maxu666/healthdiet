<template>
  <div class="nutritionist-dashboard">
    <div class="dashboard-header">
      <h1>🧑‍⚕️ 营养师工作台</h1>
      <div class="header-right">
        <span class="user-name">{{ user.nickname || user.username || '营养师' }}</span>
        <el-button type="primary" plain @click="logout">退出</el-button>
      </div>
    </div>

    <div class="content-section">
      <!-- 左侧：顶部统计卡片 + 待回复咨询 -->
      <div class="left-section">
        <!-- 顶部三个统计卡片 -->
        <div class="stats-row">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-label">今日咨询数</div>
              <div class="stat-value">{{ dashboard.todayConsultationCount || 0 }}</div>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-label">待回复数量</div>
              <div class="stat-value">{{ dashboard.pendingReplyCount || 0 }}</div>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-label">服务用户数</div>
              <div class="stat-value">{{ dashboard.servedUserCount || 0 }}</div>
            </div>
          </el-card>
        </div>

        <!-- 待回复咨询 -->
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <h3>📋 待回复咨询（最新3条）</h3>
            </div>
          </template>
          <div class="consultation-list">
            <div v-if="dashboard.pendingConsultations && dashboard.pendingConsultations.length > 0">
              <div v-for="item in dashboard.pendingConsultations" :key="item.sessionId" class="consultation-item">
                <div class="consultation-header">
                  <span class="user-info">👤 {{ item.userName }} {{ item.messageTime }}</span>
                </div>
                <div class="consultation-content">
                  问：{{ item.lastMessage }}
                </div>
                <div class="consultation-actions">
                  <el-button type="primary" size="small" @click="goToChat(item.sessionId)">回复</el-button>
                </div>
              </div>
            </div>
            <div v-else class="empty-state">
              <el-empty description="暂无待回复咨询" />
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧：公告 + 健康资讯 + 我的食谱统计 + 本周服务数据 -->
      <div class="right-section">
        <AnnouncementList />
        <HealthNewsList />
        
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <h3>📝 我的食谱统计</h3>
            </div>
          </template>
          <div class="recipe-stats">
            <div class="stat-row">
              <span class="stat-text">📝 已发布：{{ dashboard.recipeStats?.publishedCount || 0 }}个</span>
            </div>
            <div class="stat-row">
              <span class="stat-text">❤️ 被收藏：{{ dashboard.recipeStats?.totalFavourites || 0 }}次</span>
            </div>
            <div class="stat-row">
              <span class="stat-text">⭐ 平均评分：{{ (dashboard.recipeStats?.averageRating || 0).toFixed(1) }}</span>
            </div>
          </div>
        </el-card>

        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <h3>📊 本周服务数据</h3>
            </div>
          </template>
          <div class="week-stats">
            <div class="week-stat-item">
              <span class="week-stat-label">咨询量</span>
              <span class="week-stat-value">{{ dashboard.weeklyStats?.weeklyConsultationCount || 0 }}</span>
            </div>
            <div class="week-stat-item">
              <span class="week-stat-label">回复率</span>
              <span class="week-stat-value">{{ dashboard.weeklyStats?.replyRate || 0 }}%</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AnnouncementList from "@/components/AnnouncementList.vue"
import HealthNewsList from "@/components/HealthNewsList.vue"
import request from "@/utils/request"

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const dashboard = ref({})

const loadDashboardData = async () => {
  try {
    if (!user.value.id) {
      return
    }
    const data = await request.get(`/nutritionistDashboard/get/${user.value.id}`)
    dashboard.value = data
  } catch (error) {
    console.error('加载营养师工作台数据失败', error)
  }
}

const goToChat = (sessionId) => {
  // 跳转到聊天页面，带上sessionId
  router.push({ name: 'chat', query: { sessionId } })
}

const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
  ElMessage.success('退出成功')
}

const createRecipe = () => {
  // 跳转到创建食谱页面
  ElMessage.info('跳转到创建食谱页面')
  // TODO: 跳转到实际的创建食谱页面
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style scoped>
.nutritionist-dashboard {
  padding: 20px;
  background: linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%);
  min-height: 100vh;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px 30px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.dashboard-header h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.content-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.left-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.08);
  border: none;
}

.stat-item {
  text-align: center;
  padding: 15px 10px;
}

.stat-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #f97316;
}

.right-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.content-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.08);
  border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f3f4f6;
}

.card-header h3 {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.consultation-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 12px;
}

.consultation-item {
  padding: 12px;
  border: 1px solid #f3f4f6;
  border-radius: 6px;
  background: #fafafa;
}

.consultation-header {
  margin-bottom: 6px;
}

.user-info {
  font-size: 13px;
  font-weight: 500;
  color: #333;
}

.consultation-content {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
  line-height: 1.4;
}

.empty-state {
  padding: 30px 20px;
}

.consultation-actions {
  display: flex;
  justify-content: flex-end;
}

.recipe-stats {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px 0;
}

.stat-row {
  font-size: 13px;
  color: #333;
  line-height: 1.5;
  padding: 0 16px;
}

.action-row {
  margin-top: 10px;
}

.week-stats {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px 0;
}

.week-stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f3f4f6;
}

.week-stat-label {
  font-size: 13px;
  color: #666;
}

.week-stat-value {
  font-size: 14px;
  font-weight: 600;
  color: #f97316;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stats-section {
    grid-template-columns: 1fr;
  }
  
  .content-section {
    grid-template-columns: 1fr;
  }
  
  .dashboard-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
}
</style>