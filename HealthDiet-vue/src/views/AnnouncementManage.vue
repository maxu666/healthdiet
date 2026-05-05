<template>
  <div class="announcement-manage">
    <div class="page-header">
      <h1>📢 公告管理</h1>
      <el-button type="primary" @click="openAddDialog">
        <el-icon><Plus /></el-icon>
        新增公告
      </el-button>
    </div>

    <el-card class="manage-card">
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索公告标题"
          clearable
          @keyup.enter="loadAnnouncements"
        >
          <template #append>
            <el-button @click="loadAnnouncements">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>

      <el-table :data="announcements" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="content" label="内容" :show-overflow-tooltip="true">
          <template #default="scope">
            {{ scope.row.content?.substring(0, 50) }}{{ scope.row.content?.length > 50 ? '...' : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          @current-change="handlePageChange"
          layout="prev, pager, next, jumper, ->, total"
        />
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog :title="editForm.id ? '编辑公告' : '新增公告'" v-model="dialogVisible" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="editForm.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input
            v-model="editForm.content"
            type="textarea"
            placeholder="请输入公告内容"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const announcements = ref([])
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)

const editForm = reactive({
  id: null,
  title: '',
  content: ''
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const loadAnnouncements = async () => {
  try {
    const res = await request.get('/annoucement/list')
    let data = res
    if (searchKeyword.value) {
      data = data.filter(item => 
        item.title?.toLowerCase().includes(searchKeyword.value.toLowerCase())
      )
    }
    total.value = data.length
    announcements.value = data.slice(
      (currentPage.value - 1) * pageSize.value,
      currentPage.value * pageSize.value
    )
  } catch (e) {
    console.error('获取公告列表失败', e)
    ElMessage.error('获取公告列表失败')
  }
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadAnnouncements()
}

const openAddDialog = () => {
  editForm.id = null
  editForm.title = ''
  editForm.content = ''
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  editForm.id = row.id
  editForm.title = row.title
  editForm.content = row.content
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!editForm.title.trim()) {
    ElMessage.warning('请输入公告标题')
    return
  }
  if (!editForm.content.trim()) {
    ElMessage.warning('请输入公告内容')
    return
  }

  try {
    if (editForm.id) {
      await request.post('/annoucement/update', editForm)
      ElMessage.success('修改成功')
    } else {
      await request.post('/annoucement/add', editForm)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    currentPage.value = 1
    loadAnnouncements()
  } catch (e) {
    console.error('保存公告失败', e)
    ElMessage.error('保存失败')
  }
}

const handleDelete = async (id) => {
  try {
    await request.delete(`/annoucement/delete/${id}`)
    ElMessage.success('删除成功')
    loadAnnouncements()
  } catch (e) {
    console.error('删除公告失败', e)
    ElMessage.error('删除失败')
  }
}

loadAnnouncements()
</script>

<style scoped>
.announcement-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.manage-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-bar {
  margin-bottom: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>