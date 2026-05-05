<template>
  <div class="message-center">
    <div class="message-header">
      <span class="header-title">消息中心</span>
      <el-button type="primary" link @click="markAllRead">全部标为已读</el-button>
    </div>
    <div class="message-list" v-loading="loading">
      <div v-if="messageList.length === 0" class="empty-state">
        <el-empty description="暂无消息"></el-empty>
      </div>
      <div v-else>
        <div
          v-for="msg in messageList"
          :key="msg.id"
          class="message-item"
          :class="{ unread: !msg.isRead }"
          @click="handleMessageClick(msg)"
        >
          <div class="message-icon">
            <el-icon><Notification /></el-icon>
          </div>
          <div class="message-content">
            <div class="message-title">{{ msg.title }}</div>
            <div class="message-text">{{ msg.content }}</div>
            <div class="message-time">{{ formatTime(msg.createTime) }}</div>
          </div>
          <div v-if="!msg.isRead" class="unread-dot"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Notification } from '@element-plus/icons-vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const props = defineProps(['visible'])
const emit = defineEmits(['close'])

const messageList = ref([])
const loading = ref(false)
const user = JSON.parse(localStorage.getItem("user") || "{}")

const loadMessages = async () => {
  if (!user.id) return
  loading.value = true
  try {
    const res = await request.get('/api/message/list', {
      params: { userId: user.id }
    })
    if (res.success) {
      messageList.value = res.data || []
    }
  } catch (error) {
    console.error('加载消息失败', error)
  } finally {
    loading.value = false
  }
}

const markAllRead = async () => {
  try {
    await request.post('/api/message/read/all', null, {
      params: { userId: user.id }
    })
    ElMessage.success('已全部标为已读')
    await loadMessages()
    emit('refresh')
  } catch (error) {
    console.error('标记失败', error)
  }
}

const handleMessageClick = async (msg) => {
  if (!msg.isRead) {
    if (msg.type === 'ANNOUNCEMENT') {
      await request.post('/api/message/read/announcement', null, {
        params: { userId: user.id, announcementId: msg.id }
      })
    }
    await loadMessages()
    emit('refresh')
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`

  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
.message-center {
  width: 380px;
  max-height: 480px;
  display: flex;
  flex-direction: column;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

.message-item {
  display: flex;
  align-items: flex-start;
  padding: 14px 16px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.message-item:hover {
  background: #f9f9f9;
}

.message-item.unread {
  background: #fff8eb;
}

.message-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 12px;
  flex-shrink: 0;
}

.message-icon .el-icon {
  font-size: 18px;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.message-text {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 6px;
}

.unread-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #f97316;
  position: absolute;
  top: 20px;
  right: 16px;
}

.empty-state {
  padding: 40px 0;
}
</style>
