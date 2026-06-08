<template>
  <div style="padding: 20px;">
    <h1 style="text-align: center; margin-bottom: 20px; font-size: 28px;">用户信息管理</h1>

    <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 15px; flex-wrap: wrap;">
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>用户名</span>
        <el-input v-model="query.username" placeholder="请输入用户名" style="width: 180px;" />
      </div>
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>邮箱</span>
        <el-input v-model="query.email" placeholder="请输入邮箱" style="width: 180px;" />
      </div>
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>角色</span>
        <el-select v-model="query.role" placeholder="请选择角色" style="width: 120px;">
          <el-option label="全部" value="" />
          <el-option label="管理员" value="admin" />
          <el-option label="普通用户" value="user" />
        </el-select>
      </div>
      <el-button type="primary" @click="getList">查询</el-button>
      <el-button @click="resetQuery">重置</el-button>
    </div>

    <div style="margin-bottom: 15px;" v-if="isAdmin">
      <el-button type="danger" @click="batchDelete">批量删除</el-button>
      <el-button type="primary" @click="openAdd">新增用户</el-button>
    </div>

    <el-table :data="userList" border @selection-change="handleSelectionChange" style="width: 100%;">
      <el-table-column type="selection" width="60" align="center" v-if="isAdmin" />
      <el-table-column label="ID" prop="id" align="center" width="80" />
      <el-table-column label="用户名" prop="username" align="center" />
      <el-table-column label="邮箱" prop="email" align="center" />
      <el-table-column label="手机号" prop="phone" align="center" />
      <el-table-column label="角色" align="center" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.role === 'admin' ? 'danger' : 'success'">
            {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" prop="createTime" align="center" />
      <el-table-column label="操作" align="center" width="180" v-if="isAdmin">
        <template #default="scope">
          <el-button size="small" type="primary" @click="openEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="450px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item label="密码" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" v-if="!form.id">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码" />
        </el-form-item>
        <el-form-item label="新密码" v-if="!!form.id">
          <el-input v-model="form.password" type="password" placeholder="不修改密码请留空" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const userList = ref([])
const selectedIds = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('编辑用户')
const form = ref({
  id: null,
  username: '',
  email: '',
  phone: '',
  role: '',
  password: '',
  confirmPassword: ''
})
const query = ref({
  username: '',
  email: '',
  role: ''
})

const currentUser = ref(null)

const isAdmin = computed(() => {
  return currentUser.value && currentUser.value.role === 'admin'
})

const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
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
      username: query.value.username || undefined,
      email: query.value.email || undefined,
      role: query.value.role || undefined
    }
    const response = await axiosInstance.get('/user/list')
    let data = response.data.records || []
    
    if (query.value.username) {
      data = data.filter(item => item.username.includes(query.value.username))
    }
    if (query.value.email) {
      data = data.filter(item => item.email.includes(query.value.email))
    }
    if (query.value.role) {
      data = data.filter(item => item.role === query.value.role)
    }
    
    userList.value = data
  } catch (error) {
    ElMessage.error('获取用户列表失败')
    console.error(error)
  }
}

const resetQuery = () => {
  query.value = { username: '', email: '', role: '' }
  getList()
}

const openAdd = () => {
  if (!checkAdmin()) return
  dialogTitle.value = '新增用户'
  form.value = {
    id: null,
    username: '',
    email: '',
    phone: '',
    role: 'user',
    password: '',
    confirmPassword: ''
  }
  dialogVisible.value = true
}

const openEdit = (row) => {
  if (!checkAdmin()) return
  dialogTitle.value = '编辑用户'
  form.value = { 
    id: row.id, 
    username: row.username, 
    email: row.email || '', 
    phone: row.phone || '', 
    role: row.role,
    password: '',
    confirmPassword: ''
  }
  dialogVisible.value = true
}

const save = async () => {
  if (!checkAdmin()) return
  
  if (!form.value.username) {
    return ElMessage.warning('请输入用户名')
  }
  
  if (!form.value.email) {
    return ElMessage.warning('请输入邮箱')
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(form.value.email)) {
    return ElMessage.warning('请输入正确的邮箱格式')
  }
  
  if (!form.value.phone) {
    return ElMessage.warning('请输入手机号')
  }
  const phoneRegex = /^1[3-9]\d{9}$/
  if (!phoneRegex.test(form.value.phone)) {
    return ElMessage.warning('请输入正确的手机号格式')
  }
  
  if (!form.value.role) {
    return ElMessage.warning('请选择角色')
  }
  
  if (!form.value.id) {
    if (!form.value.password) {
      return ElMessage.warning('请输入密码')
    }
    if (!form.value.confirmPassword) {
      return ElMessage.warning('请确认密码')
    }
    if (form.value.password !== form.value.confirmPassword) {
      return ElMessage.warning('两次输入的密码不一致')
    }
  }
  
  try {
    if (form.value.id) {
      const updateData = {
        id: form.value.id,
        username: form.value.username,
        email: form.value.email,
        phone: form.value.phone,
        role: form.value.role
      }
      
      if (form.value.password) {
        updateData.password = form.value.password
      }
      
      const response = await axiosInstance.put('/user/update', updateData)
      
      if (!response.data || !response.data.success) {
        return ElMessage.warning(response.data?.message || '修改失败')
      }
      
      ElMessage.success('修改成功')
    } else {
      const registerData = {
        username: form.value.username,
        password: form.value.password,
        email: form.value.email,
        phone: form.value.phone,
        role: form.value.role
      }
      
      const response = await axiosInstance.post('/user/register', registerData)
      
      if (!response.data || !response.data.success) {
        return ElMessage.warning(response.data?.message || '新增失败')
      }
      
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    getList()
  } catch (error) {
    const errorMessage = error.response?.data?.message || error.message || '操作失败'
    ElMessage.error(errorMessage)
    console.error(error)
  }
}

const deleteUser = async (id) => {
  if (!checkAdmin()) return
  
  const user = userList.value.find(u => u.id === id)
  if (user && user.role === 'admin') {
    return ElMessage.warning('不能删除管理员用户')
  }
  
  await ElMessageBox.confirm('确定删除该用户？', '提示', { type: 'warning' })
  try {
    await axiosInstance.delete(`/user/delete/${id}`)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const batchDelete = async () => {
  if (!checkAdmin()) return
  if (selectedIds.value.length === 0) {
    return ElMessage.warning('请选择要删除的用户')
  }
  
  const adminIds = userList.value
    .filter(u => u.role === 'admin')
    .map(u => u.id)
  
  const containsAdmin = selectedIds.value.some(id => adminIds.includes(id))
  if (containsAdmin) {
    return ElMessage.warning('不能删除管理员用户')
  }
  
  await ElMessageBox.confirm('确定删除选中的用户？', '提示', { type: 'warning' })
  try {
    await axiosInstance.post('/user/batchDelete', { ids: selectedIds.value })
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