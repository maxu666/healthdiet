<template>
  <div class="chat-page">
    <!-- 左侧：用户列表（营养师端）或营养师列表（用户端） -->
    <div class="nutritionist-list">
      <div class="list-header">
        <h3 v-if="user.role === 'NUTRITIONIST'">👤 咨询用户</h3>
        <h3 v-else>👨‍⚕️ 推荐营养师</h3>
      </div>

      <div
        v-for="item in leftList"
        :key="item.id"
        :class="['nutritionist-item', selectedLeftId === item.id ? 'active' : '']"
        @click="selectLeftItem(item)"
      >
        <div class="avatar">
          <span>{{ item.nickname?.charAt(0) || item.username?.charAt(0) || '?' }}</span>
        </div>
        <div class="info">
          <div class="name">{{ item.nickname || item.username }}</div>
          <div class="desc">{{ user.role === 'NUTRITIONIST' ? '点击回复咨询' : '点击开始咨询' }}</div>
        </div>
        <div class="action-btns">
          <el-button
            v-if="user.role === 'USER'"
            size="small"
            type="text"
            class="action-btn"
            @click.stop="viewNutritionistProfile(item)"
          >
            📋 查看资料
          </el-button>
          <el-button
            v-if="user.role === 'NUTRITIONIST'"
            size="small"
            type="text"
            class="action-btn"
            @click.stop="viewUserNutrition(item)"
          >
            📊 查看营养
          </el-button>
        </div>
        <div v-if="getUnreadCount(item.id) > 0" class="unread-badge">
          {{ getUnreadCount(item.id) }}
        </div>
      </div>

      <div v-if="leftList.length === 0" class="empty">
        <div class="empty-icon">{{ user.role === 'NUTRITIONIST' ? '👤' : '👨‍⚕️' }}</div>
        <div class="empty-text">{{ user.role === 'NUTRITIONIST' ? '暂无咨询用户' : '暂无营养师' }}</div>
      </div>
    </div>

    <!-- 右侧：聊天窗口 -->
    <div class="chat-area" v-if="selectedLeftId && selectedLeftItem">
      <!-- 聊天头部 -->
      <div class="chat-header">
        <div class="header-title">{{ user.role === 'NUTRITIONIST' ? '与用户 ' : '与营养师 ' }}{{ selectedLeftItem.nickname || selectedLeftItem.username }} 的咨询</div>
      </div>

      <!-- 消息列表 -->
      <div class="message-list" ref="messageListRef">
        <div
          v-for="msg in messages"
          :key="msg.id"
          :class="['message-item', msg.senderId === user.id ? 'self' : 'other']"
        >
          <div class="message-avatar">{{ msg.senderRole === 'NUTRITIONIST' ? '👨‍⚕️' : '👤' }}</div>
          <div class="message-content">
            <div class="bubble">{{ msg.content }}</div>
            <div class="time">{{ formatTime(msg.createTime) }}</div>
          </div>
        </div>

        <div v-if="messages.length === 0" class="empty-messages">
          <div class="empty-icon">💬</div>
          <div class="empty-text">{{ user.role === 'NUTRITIONIST' ? '回复用户的咨询吧' : '开始您的咨询吧' }}</div>
        </div>
      </div>

      <!-- 输入框 -->
      <div class="input-area">
        <el-input
          v-model="content"
          :placeholder="user.role === 'NUTRITIONIST' ? '回复用户...' : '输入消息...'"
          @keyup.enter="sendMessage"
          class="message-input"
        />
        <el-button type="primary" @click="sendMessage" class="send-btn">发送</el-button>
      </div>
    </div>

    <!-- 未选中 -->
    <div class="empty-right" v-else>
      <div class="empty-icon">👈</div>
      <div class="empty-text">{{ user.role === 'NUTRITIONIST' ? '选择一个用户回复咨询' : '选择一个营养师开始咨询' }}</div>
    </div>

    <!-- 营养师资料弹窗（用户端） -->
    <el-dialog v-model="profileDialogVisible" title="营养师资料" width="800px">
      <div v-if="nutritionistRecipes.length === 0" class="profile-empty">
        <el-empty description="该营养师暂无食谱" />
      </div>
      <div v-else class="recipe-grid">
        <div
          v-for="recipe in nutritionistRecipes"
          :key="recipe.id"
          class="recipe-card"
        >
          <div class="cover"></div>
          <div class="recipe-title">{{ recipe.name }}</div>
          <div class="tags">
            <el-tag size="small" type="success">
              {{ recipe.healthGoal || "健康" }}
            </el-tag>
          </div>
          <div class="recipe-actions">
            <div class="recipe-rating">
              <el-rate :model-value="ratingMap[recipe.id] || 0" disabled size="small" />
              <span class="rating-text">{{ (ratingMap[recipe.id] || 0.0).toFixed(1) }}分</span>
            </div>
            <div class="recipe-desc">{{ recipe.description || '暂无描述' }}</div>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 用户营养数据弹窗（营养师端） -->
    <el-dialog v-model="nutritionDialogVisible" :title="'👤 ' + (selectedLeftItem?.nickname || selectedLeftItem?.username || '用户') + ' 的营养数据'" width="900px">
      <div v-if="userNutritionData">
        <!-- 图表区域 -->
        <div class="charts-wrapper">
          <el-radio-group v-model="nutritionType" @change="loadUserNutritionData" class="type-selector">
            <el-radio-button label="day">今日</el-radio-button>
            <el-radio-button label="week">本周</el-radio-button>
            <el-radio-button label="month">本月</el-radio-button>
          </el-radio-group>

          <div class="charts-container">
            <div ref="pieRef" class="chart"></div>
            <div ref="lineRef" class="chart"></div>
          </div>
        </div>

        <!-- 饮食记录列表 -->
        <div class="diet-records-section">
          <h4>🍽️ 饮食记录</h4>
          <div v-if="dietGroupList.length === 0" class="records-empty">
            <el-empty description="暂无饮食记录" />
          </div>
          <div v-else class="diet-records-list">
            <div v-for="dateItem in dietGroupList" :key="dateItem.date" class="date-block">
              <div class="date-header">
                <div class="date-badge">
                  <span class="date-icon">📅</span>
                  <span class="date-text">{{ dateItem.date }}</span>
                </div>
              </div>

              <div v-for="meal in dateItem.meals" :key="meal.mealType" class="meal-block">
                <div class="meal-header">
                  <div class="meal-info">
                    <span class="meal-icon">{{ getMealIcon(meal.mealType) }}</span>
                    <span class="meal-type">{{ meal.mealType }}</span>
                    <span class="meal-cal-badge">{{ meal.totalCalorie?.toFixed(0) || 0 }} kcal</span>
                  </div>
                </div>

                <div class="food-list">
                  <div v-for="(food, index) in meal.foods" :key="index" class="food-item">
                    <div class="food-info">
                      <span class="food-name">{{ food.foodName || '未知食物' }}</span>
                      <span class="food-tag" v-if="food.dishId">菜品</span>
                    </div>
                    <div class="food-details">
                      <span class="food-weight">{{ parseFloat(food.weight || 0).toFixed(1) }}g</span>
                      <span class="food-kcal">{{ parseFloat(food.calorie || 0).toFixed(0) }}kcal</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const user = JSON.parse(localStorage.getItem('user') || '{}')

// 数据
const leftList = ref([])
const selectedLeftId = ref(null)
const selectedLeftItem = ref(null)
const messages = ref([])
const content = ref('')
const messageListRef = ref(null)
const unreadMap = ref({})
const currentSessionId = ref(null)

// 弹窗状态
const profileDialogVisible = ref(false)
const nutritionDialogVisible = ref(false)
const nutritionistRecipes = ref([])
const userNutritionData = ref(null)
const ratingMap = ref({})

// 图表相关
const pieRef = ref(null)
const lineRef = ref(null)
const nutritionType = ref('day')
let pieChart, lineChart
const dietGroupList = ref([])

// 加载左侧列表
const loadLeftList = async () => {
  try {
    let res = []
    if (user.role === 'NUTRITIONIST') {
      // 营养师端：获取咨询用户列表
      res = await request.get('/user/consultUsers/' + user.id)
    } else {
      // 用户端：获取营养师列表
      res = await request.get('/user/nutritionists')
    }
    leftList.value = res
    
    // 更新每个用户的未读数
    for (const item of res) {
      await updateUnreadCount(item.id)
    }
  } catch (e) {
    console.error('加载列表失败', e)
  }
}

// 选择左侧项目（营养师或用户）
const selectLeftItem = async (item) => {
  selectedLeftId.value = item.id
  selectedLeftItem.value = item
  messages.value = []
  content.value = ''
  
  // 获取或创建会话
  await getOrCreateSession(item)
  
  // 标记未读为已读
  unreadMap.value[item.id] = 0
}

// 获取或创建会话
const getOrCreateSession = async (item) => {
  try {
    let userId = user.id
    let nutritionistId = item.id
    
    if (user.role === 'NUTRITIONIST') {
      // 营养师端：用户是提问者
      userId = item.id
      nutritionistId = user.id
    }
    
    const res = await request.get('/chatSession/getOrCreate', {
      params: {
        userId: userId,
        nutritionistId: nutritionistId,
        title: `与${item.nickname || item.username}的咨询`
      }
    })
    
    currentSessionId.value = res.id
    await loadMessages()
  } catch (e) {
    console.error('创建会话失败', e)
  }
}

// 更新未读数
const updateUnreadCount = async (targetId) => {
  try {
    // 获取或创建会话
    let userId = user.id
    let nutritionistId = targetId
    
    if (user.role === 'NUTRITIONIST') {
      userId = targetId
      nutritionistId = user.id
    }
    
    const sessionRes = await request.get('/chatSession/getOrCreate', {
      params: {
        userId: userId,
        nutritionistId: nutritionistId,
        title: 'temp'
      }
    })
    
    const msgRes = await request.get(`/chatMessage/list/${sessionRes.id}`)
    const count = msgRes.filter(m => m.isRead === 0 && m.senderId !== user.id).length
    unreadMap.value[targetId] = count
  } catch (e) {
    unreadMap.value[targetId] = 0
  }
}

const getUnreadCount = (targetId) => {
  return unreadMap.value[targetId] || 0
}

// 加载消息
const loadMessages = async () => {
  if (!currentSessionId.value) return

  try {
    const res = await request.get(`/chatMessage/list/${currentSessionId.value}`)
    messages.value = res

    await nextTick()
    scrollToBottom()

    // 标记已读
    await request.put(`/chatMessage/read/${currentSessionId.value}/${user.id}`)
  } catch (e) {
    console.error('加载消息失败', e)
  }
}

// 发送消息
const sendMessage = async () => {
  if (!content.value.trim()) return
  if (!currentSessionId.value) {
    ElMessage.error('请先选择一个会话')
    return
  }

  try {
    await request.post('/chatMessage/send', {
      sessionId: currentSessionId.value,
      senderId: user.id,
      senderRole: user.role,
      content: content.value
    })

    content.value = ''
    await loadMessages()
    await loadLeftList()
  } catch (e) {
    console.error('发送失败', e)
    ElMessage.error('发送失败')
  }
}

// 获取食谱平均评分
const getAverageRating = async (recipeId) => {
  try {
    const res = await request.get('/recipereview/averageRating', {
      params: { recipeId }
    })
    return res
  } catch (e) {
    console.error("获取评分失败", e)
    return 0
  }
}

// 批量加载食谱评分
const loadRecipeRatings = async (recipeIds) => {
  for (const id of recipeIds) {
    if (!ratingMap.value[id]) {
      const rating = await getAverageRating(id)
      ratingMap.value[id] = rating
    }
  }
}

// 查看营养师资料（用户端）
const viewNutritionistProfile = async (item) => {
  try {
    const res = await request.get(`/recipe/byNutritionist/${item.id}`)
    nutritionistRecipes.value = res
    await loadRecipeRatings(res.map(r => r.id))
    profileDialogVisible.value = true
  } catch (e) {
    console.error('获取营养师资料失败', e)
    ElMessage.error('获取营养师资料失败')
  }
}

// 查看用户营养数据（营养师端）
const viewUserNutrition = async (item) => {
  selectedLeftItem.value = item
  nutritionDialogVisible.value = true
  // 等待弹窗和图表容器完全渲染
  await nextTick()
  await nextTick()
  initCharts()
  await loadUserNutritionData()
}

// 初始化图表
const initCharts = () => {
  if (pieRef.value) {
    pieChart = echarts.init(pieRef.value)
  }
  if (lineRef.value) {
    lineChart = echarts.init(lineRef.value)
  }
}

// 加载用户营养数据
const loadUserNutritionData = async () => {
  if (!selectedLeftItem.value) return
  if (!pieChart || !lineChart) {
    initCharts()
  }

  try {
    const targetUserId = selectedLeftItem.value.id

    const statRes = await request.get('/dietRecord/trend/stat', {
      params: { userId: targetUserId, type: nutritionType.value }
    })

    // 安全获取 stat 数据
    const statData = statRes || {}

    if (pieChart) {
      pieChart.setOption({
        title: { text: '营养占比', left: 'center', textStyle: { fontSize: 14 } },
        series: [{
          type: 'pie',
          radius: '55%',
          data: [
            { value: statData.carbohydrate || 0, name: '碳水', itemStyle: { color: '#fbbf24' } },
            { value: statData.protein || 0, name: '蛋白质', itemStyle: { color: '#34d399' } },
            { value: statData.fat || 0, name: '脂肪', itemStyle: { color: '#fb923c' } }
          ]
        }]
      })
    }

    const lineRes = await request.get('/dietRecord/trend/line', {
      params: { userId: targetUserId }
    })

    // 安全获取 line 数据
    const lineData = lineRes || []
    const dates = lineData.map(i => i.date || '')
    const calories = lineData.map(i => i.calorie || 0)

    if (lineChart) {
      lineChart.setOption({
        title: { text: '热量趋势', left: 'center', textStyle: { fontSize: 14 } },
        xAxis: { type: 'category', data: dates, axisLabel: { fontSize: 10 } },
        yAxis: { type: 'value', name: 'kcal', axisLabel: { fontSize: 10 } },
        series: [{
          type: 'line',
          data: calories,
          smooth: true,
          areaStyle: { color: 'rgba(102, 126, 234, 0.2)' },
          lineStyle: { color: '#667eea' },
          itemStyle: { color: '#667eea' }
        }]
      })
    }

    // 复用已有的 userData 接口来获取完整记录
    const userDataRes = await request.get('/dietRecord/userData', {
      params: { userId: targetUserId }
    })
    // 安全获取 record 数据
    const recordData = (userDataRes && userDataRes.records) || []
    userNutritionData.value = recordData
    processDietRecords(recordData)

  } catch (e) {
    console.error('获取用户营养数据失败', e)
  }
}

// 处理饮食记录，按日期和餐次分组
const processDietRecords = (records) => {
  const map = {}
  for (const r of records) {
    const date = r.recordDate
    if (!map[date]) {
      map[date] = { date, meals: [] }
    }
    const meal = map[date].meals.find(m => m.mealType === r.mealType)
    if (meal) {
      meal.foods.push(r)
      meal.totalCalorie = (meal.totalCalorie || 0) + (r.calorie || 0)
    } else {
      map[date].meals.push({
        mealType: r.mealType,
        totalCalorie: r.calorie || 0,
        foods: [r]
      })
    }
  }
  dietGroupList.value = Object.values(map).sort((a, b) => b.date.localeCompare(a.date))
}

// 餐次图标
const getMealIcon = (mealType) => {
  const map = { '早餐': '🌅', '午餐': '☀️', '晚餐': '🌙', '加餐': '🍪' }
  return map[mealType] || '🍽️'
}

// 滚动到底部
const scrollToBottom = () => {
  const el = messageListRef.value
  if (el) el.scrollTop = el.scrollHeight
}

// 时间格式化
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 86400000) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }
  if (diff < 172800000) {
    return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }
  return date.toLocaleString('zh-CN', { month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}

onMounted(() => {
  loadLeftList()

  // 轮询刷新
  setInterval(() => {
    loadMessages()
    loadLeftList()
  }, 5000)
})
</script>

<style scoped>
.chat-page {
  display: flex;
  height: calc(100vh - 120px);
  background: #f5f7fa;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 左侧列表 */
.nutritionist-list {
  width: 260px;
  border-right: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 12px 0 0 12px;
}

.nutritionist-item {
  display: flex;
  align-items: center;
  padding: 12px 14px;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 1px solid #f0f0f0;
}

.nutritionist-item:hover {
  background: #f8f9fa;
}

.nutritionist-item.active {
  background: #e6f7ff;
  border-left: 3px solid #1890ff;
}

.nutritionist-item .avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 600;
  font-size: 14px;
  margin-right: 10px;
  flex-shrink: 0;
}

.nutritionist-item .info {
  flex: 1;
  overflow: hidden;
  min-width: 0;
}

.nutritionist-item .name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 3px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.nutritionist-item .desc {
  font-size: 11px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.nutritionist-item .unread-badge {
  background: #f5222d;
  color: #fff;
  font-size: 11px;
  min-width: 18px;
  height: 18px;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 5px;
  flex-shrink: 0;
}

/* 通用头部样式 */
.list-header {
  padding: 14px 16px;
  border-bottom: 1px solid #e8e8e8;
  background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
}

.list-header h3 {
  margin: 0;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
}

/* 空状态 */
.empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  padding: 20px;
}

.empty-icon {
  font-size: 40px;
  margin-bottom: 10px;
}

.empty-text {
  font-size: 13px;
  text-align: center;
}

/* 右侧聊天区域 */
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 0 12px 12px 0;
}

.chat-header {
  padding: 14px 18px;
  border-bottom: 1px solid #e8e8e8;
  background: #fff;
  border-radius: 0 12px 0 0;
}

.header-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  text-align: center;
}

/* 消息列表 */
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 18px;
  background: #f5f7fa;
}

.message-item {
  display: flex;
  margin-bottom: 14px;
}

.message-item.self {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  flex-shrink: 0;
}

.message-item.self .message-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.message-content {
  max-width: 55%;
  margin: 0 10px;
}

.bubble {
  padding: 10px 14px;
  border-radius: 14px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  word-break: break-all;
  font-size: 13px;
  line-height: 1.5;
}

.message-item.self .bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 14px 14px 4px 14px;
}

.message-item.other .bubble {
  border-radius: 14px 14px 14px 4px;
}

.time {
  font-size: 11px;
  color: #999;
  margin-top: 4px;
}

.message-item.self .time {
  text-align: right;
}

.empty-messages {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #999;
}

/* 输入区域 */
.input-area {
  display: flex;
  padding: 14px 18px;
  gap: 10px;
  border-top: 1px solid #e8e8e8;
  background: #fff;
  border-radius: 0 0 12px 0;
}

.message-input {
  flex: 1;
}

.send-btn {
  min-width: 70px;
}

/* 右侧空状态 */
.empty-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  background: #fafafa;
  border-radius: 0 12px 12px 0;
}

.empty-right .empty-icon {
  font-size: 56px;
  margin-bottom: 14px;
}

.empty-right .empty-text {
  font-size: 15px;
}

/* 操作按钮 */
.action-btns {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-top: 4px;
}

.action-btn {
  font-size: 11px !important;
  padding: 2px 6px !important;
}

/* 营养师资料弹窗 */
.profile-empty {
  padding: 40px 0;
  text-align: center;
}

.recipe-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recipe-card-simple {
  padding: 14px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  transition: all 0.2s;
}

.recipe-card-simple:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
}

.recipe-card-simple .recipe-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.recipe-card-simple .recipe-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.recipe-card-simple .recipe-desc {
  font-size: 12px;
  color: #999;
}

/* 用户营养数据弹窗 */
.nutrition-summary {
  margin-bottom: 24px;
}

.nutrition-summary h4 {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 14px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.summary-item {
  background: #f5f7fa;
  padding: 14px;
  border-radius: 8px;
  text-align: center;
}

.summary-label {
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
}

.summary-value {
  font-size: 18px;
  font-weight: 700;
  color: #333;
}

.nutrition-records h4 {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 14px;
}

.records-empty {
  padding: 30px 0;
}

/* 图表区域 */
.charts-wrapper {
  margin-bottom: 20px;
}

.charts-wrapper .type-selector {
  margin-bottom: 15px;
  display: flex;
  justify-content: center;
}

.charts-container {
  display: flex;
  gap: 20px;
}

.charts-container .chart {
  flex: 1;
  height: 250px;
}

/* 饮食记录列表 */
.diet-records-section {
  max-height: 400px;
  overflow-y: auto;
}

.diet-records-section h4 {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 14px;
}

.diet-records-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.date-block {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px 14px;
}

.date-header {
  margin-bottom: 10px;
}

.date-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: #fff;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  color: #333;
  border: 1px solid #e8e8e8;
}

.date-icon {
  font-size: 12px;
}

.meal-block {
  background: #fff;
  border-radius: 8px;
  padding: 10px 12px;
  margin-bottom: 8px;
  border: 1px solid #f0f0f0;
}

.meal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.meal-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.meal-icon {
  font-size: 14px;
}

.meal-type {
  font-size: 13px;
  font-weight: 500;
  color: #333;
}

.meal-cal-badge {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
}

.food-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.food-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 10px;
  background: #f9fafb;
  border-radius: 6px;
  font-size: 12px;
}

.food-info {
  display: flex;
  align-items: center;
  gap: 6px;
}

.food-name {
  color: #333;
}

.food-tag {
  background: #e6f7ff;
  color: #1890ff;
  font-size: 10px;
  padding: 1px 5px;
  border-radius: 3px;
}

.food-details {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #666;
}

.food-weight,
.food-kcal {
  font-size: 12px;
}

/* 营养师资料食谱卡片 */
.recipe-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-top: 10px;
}

.recipe-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.recipe-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.cover {
  height: 110px;
  border-radius: 10px;
  margin-bottom: 10px;
  background-image: url('/recipe_image.jpg');
  background-size: cover;
  background-position: center;
}

.recipe-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.tags {
  margin-bottom: 12px;
}

.recipe-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.recipe-rating {
  display: flex;
  align-items: center;
  gap: 4px;
}

.rating-text {
  font-size: 14px;
  color: #f5a623;
  font-weight: bold;
}

.recipe-desc {
  font-size: 12px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.profile-empty {
  padding: 40px 0;
}
</style>