<template>
  <div class="chat-container">
    <!-- 返回按钮 -->
    <div class="chat-header">
      <div class="back-btn" @click="goBack">
        <span class="back-icon">←</span>
        <span>返回</span>
      </div>
      <div class="header-title">{{ sessionTitle || '咨询会话' }}</div>
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
          <div class="sender-name">{{ msg.senderRole === 'NUTRITIONIST' ? '营养师' : '我' }}</div>
          <div class="bubble">{{ msg.content }}</div>
          <div class="time">{{ formatTime(msg.createTime) }}</div>
        </div>
      </div>

      <div v-if="messages.length === 0" class="empty-messages">
        <div class="empty-icon">💬</div>
        <div class="empty-text">开始您的咨询吧</div>
      </div>
    </div>

    <!-- 输入区 -->
    <div class="input-area">
      <el-input
        v-model="content"
        placeholder="输入消息..."
        @keyup.enter="sendMessage"
        class="message-input"
      />
      <el-button type="primary" @click="sendMessage" class="send-btn">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

// 当前用户
const user = JSON.parse(localStorage.getItem('user') || '{}')

// 从路由获取会话ID
const sessionId = computed(() => {
  return Number(route.params.sessionId) || null
})

const sessionTitle = ref('咨询会话')

const messages = ref([])
const content = ref('')
const messageListRef = ref(null)

// 获取聊天记录
const loadMessages = async () => {
  if (!sessionId.value) {
    ElMessage.error('会话ID无效')
    return
  }

  try {
    const res = await request.get(`/chatMessage/list/${sessionId.value}`)
    messages.value = res

    await nextTick()
    scrollToBottom()

    await request.put(`/chatMessage/read/${sessionId.value}/${user.id}`)
  } catch (e) {
    console.error('加载消息失败', e)
    ElMessage.error('加载消息失败')
  }
}

// 获取会话信息
const loadSessionInfo = async () => {
  if (!sessionId.value) return
  
  try {
    const res = await request.get(`/chatSession/detail/${sessionId.value}`)
    if (res && res.dish) {
      sessionTitle.value = res.dish.title || '咨询会话'
    }
  } catch (e) {
    console.error('获取会话信息失败', e)
  }
}

// 发送消息
const sendMessage = async () => {
  if (!content.value.trim()) return
  if (!sessionId.value) {
    ElMessage.error('会话ID无效')
    return
  }

  try {
    await request.post('/chatMessage/send', {
      sessionId: sessionId.value,
      senderId: user.id,
      senderRole: user.role,
      content: content.value
    })

    content.value = ''
    await loadMessages()
  } catch (e) {
    console.error('发送失败', e)
    ElMessage.error('发送失败')
  }
}

// 滚动到底部
const scrollToBottom = () => {
  const el = messageListRef.value
  if (el) {
    el.scrollTop = el.scrollHeight
  }
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

// 返回
const goBack = () => {
  router.push('/chat')
}

onMounted(() => {
  loadSessionInfo()
  loadMessages()

  setInterval(() => {
    loadMessages()
  }, 5000)
})
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 120px);
  background: #f5f7fa;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #e8e8e8;
  background: #fff;
  border-radius: 12px 12px 0 0;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  cursor: pointer;
  color: #1890ff;
  border-radius: 6px;
  transition: all 0.2s;
}

.back-btn:hover {
  background: #e6f7ff;
}

.back-icon {
  font-size: 16px;
  font-weight: bold;
}

.header-title {
  flex: 1;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f5f7fa;
}

.message-item {
  display: flex;
  margin-bottom: 16px;
}

.message-item.self {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.message-item.self .message-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.message-content {
  max-width: 60%;
  margin: 0 12px;
}

.sender-name {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.bubble {
  padding: 12px 16px;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  word-break: break-all;
  font-size: 14px;
  line-height: 1.5;
}

.message-item.self .bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 16px 16px 4px 16px;
}

.message-item.other .bubble {
  border-radius: 16px 16px 16px 4px;
}

.time {
  font-size: 12px;
  color: #999;
  margin-top: 6px;
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

.empty-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.empty-text {
  font-size: 14px;
}

.input-area {
  display: flex;
  padding: 16px 20px;
  gap: 12px;
  border-top: 1px solid #e8e8e8;
  background: #fff;
  border-radius: 0 0 12px 12px;
}

.message-input {
  flex: 1;
}

.send-btn {
  min-width: 80px;
}
</style>