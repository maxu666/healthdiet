<template>
  <div class="health-news-list">
    <div class="list-header">
      <el-icon class="header-icon"><Document /></el-icon>
      <span class="header-title">📰 健康资讯</span>
    </div>
    <div v-if="healthNews.length === 0" class="empty-state">
      <el-empty description="暂无健康资讯" />
    </div>
    <div v-else class="news-items">
      <div
        v-for="item in healthNews"
        :key="item.id"
        class="news-item"
        @click="openDetail(item)"
      >
        <div class="item-title">{{ item.title }}</div>
        <div class="item-summary">{{ item.summary }}</div>
        <div class="item-footer">
          <span class="item-source">{{ item.source }}</span>
          <span class="item-date">{{ formatDate(item.createTime) }}</span>
        </div>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog
      :title="selectedNews?.title || '资讯详情'"
      v-model="dialogVisible"
      width="500px"
      :close-on-click-modal="true"
    >
      <div class="detail-content">
        <div class="detail-summary">
          <span class="detail-label">摘要：</span>
          <span>{{ selectedNews?.summary || '-' }}</span>
        </div>
        <div class="detail-content-text">
          <span class="detail-label">内容：</span>
          <div class="content-body">{{ selectedNews?.content || '-' }}</div>
        </div>
        <div class="detail-source">
          <span class="detail-label">来源：</span>
          <span>{{ selectedNews?.source || '-' }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">关闭</el-button>
        <el-button
            type="primary"
            v-if="selectedNews?.url"
            @click="goToUrl"
          >
            <el-icon><Link /></el-icon>
            查看原文
          </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Document, Link } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const healthNews = ref([])
const dialogVisible = ref(false)
const selectedNews = ref(null)

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

const loadHealthNews = async () => {
  try {
    const res = await request.get('/healthNews/list')
    healthNews.value = res.slice(0, 3)
  } catch (e) {
    console.error('获取健康资讯失败', e)
  }
}

const openDetail = (item) => {
  selectedNews.value = item
  dialogVisible.value = true
}

const goToUrl = () => {
  if (selectedNews.value?.url) {
    window.open(selectedNews.value.url, '_blank')
    dialogVisible.value = false
  } else {
    ElMessage.info('暂无原文链接')
  }
}

onMounted(() => {
  loadHealthNews()
})
</script>

<style scoped>
.health-news-list {
  background: #f9fafb;
  border-radius: 6px;
  overflow: hidden;
}

.list-header {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 12px;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
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

.news-items {
  padding: 6px;
}

.news-item {
  padding: 10px;
  border-bottom: 1px solid #e5e7eb;
  cursor: pointer;
  transition: background 0.2s;
}

.news-item:hover {
  background: #f3f4f6;
}

.news-item:last-child {
  border-bottom: none;
}

.item-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-summary {
  font-size: 13px;
  color: #666;
  line-height: 1.4;
  margin-bottom: 5px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-source {
  font-size: 12px;
  color: #11998e;
}

.item-date {
  font-size: 12px;
  color: #999;
}

.detail-content {
  padding: 10px 0;
}

.detail-summary,
.detail-source {
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.5;
}

.detail-content-text {
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.6;
}

.content-body {
  margin-top: 4px;
  white-space: pre-wrap;
  word-break: break-all;
  color: #333;
}

.detail-label {
  font-weight: 600;
  color: #666;
}
</style>