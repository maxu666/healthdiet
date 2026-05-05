<template>
  <div class="header">
    <div class="header-left">
      <span class="header-title">健康食谱记录分析系统</span>
    </div>
    <div class="header-right">
      <el-popover
        placement="bottom"
        :width="380"
        trigger="click"
        popper-class="message-popover"
        @show="loadMessages"
      >
        <MessageCenter @refresh="loadUnreadCount" />
        <template #reference>
          <div class="message-icon-wrapper" @click.stop>
            <el-icon class="message-icon"><Bell /></el-icon>
            <span v-if="unreadCount > 0" class="unread-badge">{{ unreadCount > 99 ? '99+' : unreadCount }}</span>
          </div>
        </template>
      </el-popover>
      
      <el-dropdown @command="handleCommand">
        <span class="user-info">
          <el-icon><User /></el-icon>
          <span>{{ nickname }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="personalCenter">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, ArrowDown, Bell } from '@element-plus/icons-vue'
import MessageCenter from './MessageCenter.vue'
import request from '../utils/request'

const router = useRouter()

const user = JSON.parse(localStorage.getItem("user") || "{}")
const nickname = ref(user.nickname || user.username || "用户")
const unreadCount = ref(0)

const loadUnreadCount = async () => {
  if (!user.id) return
  try {
    const res = await request.get('/api/message/unread/count', {
      params: { userId: user.id }
    })
    console.log('未读消息响应:', res)
    if (res.success) {
      unreadCount.value = res.total || 0
    }
  } catch (error) {
    console.error('获取未读消息数失败', error)
  }
}

const loadMessages = () => {
  loadUnreadCount()
}

const handleCommand = (command) => {
  if (command === 'personalCenter') {
    router.push('/index/personalCenter')
  } else if (command === 'logout') {
    localStorage.removeItem('user')
    router.push('/login')
  }
}

onMounted(() => {
  loadUnreadCount()
  setInterval(loadUnreadCount, 30000)
})
</script>

<style scoped>
.header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left .header-title {
  color: #ffffff;
  font-size: 20px;
  font-weight: 600;
  letter-spacing: 1px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.message-icon-wrapper {
  position: relative;
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  transition: all 0.2s;
}

.message-icon-wrapper:hover {
  background: rgba(255, 255, 255, 0.1);
}

.message-icon {
  font-size: 22px;
  color: #fff;
}

.unread-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 18px;
  height: 18px;
  padding: 0 4px;
  border-radius: 9px;
  background: #f97316;
  color: white;
  font-size: 11px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.user-info {
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}
</style>

<style>
.message-popover {
  padding: 0 !important;
  border-radius: 12px !important;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15) !important;
}
</style>
