<template>
  <div style="padding: 20px;">
    <h1 style="text-align: center; margin-bottom: 20px; font-size: 28px;">用户注册</h1>

    <el-form :model="form" label-width="100px" style="max-width: 400px; margin: 0 auto;">
      <el-form-item label="用户名">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码" />
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
          <el-option label="管理员" value="admin" v-if="isAdmin" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="register">注册</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <template v-if="isAdmin">
      <h2 style="text-align: center; margin-top: 40px; margin-bottom: 20px; font-size: 24px;">登录记录</h2>
      
      <el-table :data="loginRecords" border style="width: 100%;">
        <el-table-column label="ID" prop="id" align="center" width="80" />
        <el-table-column label="用户名" prop="username" align="center" />
        <el-table-column label="登录IP" prop="ipAddress" align="center" />
        <el-table-column label="登录时间" prop="loginTime" align="center" />
      </el-table>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  role: 'user'
})

const loginRecords = ref([])

const currentUser = ref(null)

const isAdmin = computed(() => {
  return currentUser.value && currentUser.value.role === 'admin'
})

const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

const register = async () => {
  if (!form.value.username) {
    return ElMessage.warning('请输入用户名')
  }
  if (!form.value.password) {
    return ElMessage.warning('请输入密码')
  }
  if (form.value.password !== form.value.confirmPassword) {
    return ElMessage.warning('两次输入的密码不一致')
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
  
  try {
    const userData = {
      username: form.value.username,
      password: form.value.password,
      email: form.value.email,
      phone: form.value.phone,
      role: form.value.role
    }
    
    const registerResponse = await axiosInstance.post('/user/register', userData)
    
    if (!registerResponse.data) {
      return ElMessage.warning('注册失败')
    }
    
    if (!registerResponse.data.success) {
      return ElMessage.warning(registerResponse.data.message || '注册失败')
    }
    
    const hasLoggedInUser = localStorage.getItem('currentUser')
    
    if (hasLoggedInUser) {
      const loggedInUser = JSON.parse(hasLoggedInUser)
      
      if (loggedInUser.role === 'admin') {
        ElMessage.success('注册成功，新用户已添加')
        resetForm()
        getLoginRecords()
        router.push('/userManage')
      } else {
        const loginResponse = await axiosInstance.post('/user/login', {
          username: form.value.username,
          password: form.value.password
        })
        
        if (loginResponse.data && loginResponse.data.success) {
          localStorage.setItem('currentUser', JSON.stringify({
            username: loginResponse.data.username,
            role: loginResponse.data.role
          }))
          ElMessage.success('注册成功并已登录')
          
          setTimeout(() => {
            window.location.href = '/'
          }, 500)
        } else {
          ElMessage.success('注册成功，请登录')
          router.push('/login')
        }
      }
    } else {
      const loginResponse = await axiosInstance.post('/user/login', {
        username: form.value.username,
        password: form.value.password
      })
      
      if (loginResponse.data && loginResponse.data.success) {
        localStorage.setItem('currentUser', JSON.stringify({
          username: loginResponse.data.username,
          role: loginResponse.data.role
        }))
        ElMessage.success('注册成功并已登录')
        
        setTimeout(() => {
          window.location.href = '/'
        }, 500)
      } else {
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      }
    }
  } catch (error) {
    const errorMessage = error.response?.data?.message || error.message || '注册失败'
    ElMessage.error(errorMessage)
    console.error(error)
  }
}

const resetForm = () => {
  form.value = {
    username: '',
    password: '',
    confirmPassword: '',
    email: '',
    phone: '',
    role: 'user'
  }
}

const getLoginRecords = async () => {
  try {
    const response = await axiosInstance.get('/loginRecord/list')
    loginRecords.value = response.data.records || []
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  const userData = localStorage.getItem('currentUser')
  if (userData) {
    currentUser.value = JSON.parse(userData)
  }
  if (isAdmin.value) {
    getLoginRecords()
  }
})
</script>