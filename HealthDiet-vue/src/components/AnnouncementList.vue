<template>
  <div class="announcement-list">
    <div class="list-header">
      <el-icon class="header-icon"><Bell /></el-icon>
      <span class="header-title">📢 最新公告</span>
    </div>
    <div v-if="announcements.length === 0" class="empty-state">
      <el-empty description="暂无公告" />
    </div>
    <div v-else class="announcement-items">
      <div
        v-for="item in announcements"
        :key="item.id"
        class="announcement-item"
      >
        <div class="item-header">
          <span class="item-title">{{ item.title }}</span>
          <span class="item-date">{{ formatDate(item.createTime) }}</span>
        </div>
        <div class="item-content">{{ item.content }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Bell } from '@element-plus/icons-vue'
import request from '@/utils/request'

const announcements = ref([])

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

const loadAnnouncements = async () => {
  try {
    const res = await request.get('/annoucement/list')
    announcements.value = res.slice(0, 3)
  } catch (e) {
    console.error('获取公告失败', e)
  }
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.announcement-list {
  background: #f9fafb;
  border-radius: 6px;
  overflow: hidden;
}

.list-header {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.header-icon {
  font-size: 16px;
}

.header-title {
  font-size: 14px;
  font-weight: 600;
}

.empty-state {
  padding: 15px 10px;
}

.announcement-items {
  padding: 6px;
}

.announcement-item {
  padding: 10px;
  border-bottom: 1px solid #e5e7eb;
  cursor: pointer;
  transition: background 0.2s;
}

.announcement-item:hover {
  background: #f3f4f6;
}

.announcement-item:last-child {
  border-bottom: none;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.item-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.item-date {
  font-size: 12px;
  color: #999;
}

.item-content {
  font-size: 13px;
  color: #666;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>