<template>
  <div class="category-manage">
    <h1 style="text-align: center; margin-bottom: 20px; font-size: 28px;">甜品分类</h1>
    
    <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 15px; flex-wrap: wrap;">
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>分类名称</span>
        <el-input v-model="query.name" placeholder="请输入分类名称" style="width: 180px;" />
      </div>
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>分类描述</span>
        <el-input v-model="query.description" placeholder="请输入分类描述" style="width: 200px;" />
      </div>
      <el-button type="primary" @click="getList">查询</el-button>
      <el-button @click="resetQuery">重置</el-button>
    </div>
    
    <div class="toolbar" v-if="isAdmin">
      <el-button type="primary" @click="openAdd">新增分类</el-button>
      <el-button type="danger" @click="batchDelete" :disabled="selectedIds.length === 0">删除</el-button>
    </div>
    
    <el-table :data="tableData" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" v-if="isAdmin"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="分类名称"></el-table-column>
      <el-table-column prop="description" label="分类描述"></el-table-column>
      <el-table-column label="操作" align="center" width="150" v-if="isAdmin">
        <template #default="scope">
          <el-button size="small" type="primary" @click="openEdit(scope.row)">修改</el-button>
          <el-button size="small" type="danger" @click="deleteItem(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        <el-form-item label="分类描述">
          <el-input v-model="form.description" placeholder="请输入分类描述"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

const tableData = ref([])
const selectedIds = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const form = ref({
  id: null,
  name: '',
  description: ''
})
const query = ref({
  name: '',
  description: ''
})

const currentUser = ref(null)

const isAdmin = computed(() => {
  return currentUser.value && currentUser.value.role === 'admin'
})

const checkAdmin = () => {
  if (!isAdmin.value) {
    ElMessage.error('该用户权限不足')
    return false
  }
  return true
}

const getList = async () => {
  try {
    const params = {
      name: query.value.name || undefined,
      description: query.value.description || undefined
    }
    const response = await axiosInstance.get('/category/list', { params })
    tableData.value = response.data
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败')
  }
}

const resetQuery = () => {
  query.value = { name: '', description: '' }
  getList()
}

const openAdd = () => {
  if (!checkAdmin()) return
  dialogTitle.value = '新增分类'
  form.value = { id: null, name: '', description: '' }
  dialogVisible.value = true
}

const openEdit = (row) => {
  if (!checkAdmin()) return
  dialogTitle.value = '修改分类'
  form.value = { ...row }
  dialogVisible.value = true
}

const save = async () => {
  if (!checkAdmin()) return
  if (!form.value.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  
  try {
    if (form.value.id) {
      await axiosInstance.put('/category/update', form.value)
      ElMessage.success('修改成功')
    } else {
      await axiosInstance.post('/category/add', form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getList()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error(form.value.id ? '修改失败' : '新增失败')
  }
}

const deleteItem = async (id) => {
  if (!checkAdmin()) return
  await ElMessageBox.confirm('确定删除该分类？', '提示', { type: 'warning' })
  try {
    await axiosInstance.delete(`/category/delete/${id}`)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const batchDelete = async () => {
  if (!checkAdmin()) return
  await ElMessageBox.confirm('确定删除选中的分类？', '提示', { type: 'warning' })
  try {
    await axiosInstance.post('/category/batchDelete', { ids: selectedIds.value })
    ElMessage.success('批量删除成功')
    getList()
    selectedIds.value = []
  } catch (error) {
    ElMessage.error('批量删除失败')
  }
}

const handleSelectionChange = (val) => {
  selectedIds.value = val.map(item => item.id)
}

onMounted(() => {
  const userData = localStorage.getItem('currentUser')
  if (userData) {
    currentUser.value = JSON.parse(userData)
  }
  getList()
})
</script>

<style scoped>
.category-manage {
  padding: 20px;
}

.toolbar {
  margin-bottom: 15px;
}
</style>