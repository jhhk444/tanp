<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>用户登录</span>
        </div>
      </template>
      <el-form :model="loginForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item class="button-group">
          <el-button type="primary" class="login-btn" @click="handleLogin">登录</el-button>
          <el-button class="register-btn" @click="goToRegister">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()

const loginForm = ref({
  username: '',
  password: ''
})

const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

const handleLogin = async () => {
  if (!loginForm.value.username) {
    return ElMessage.warning('请输入用户名')
  }
  if (!loginForm.value.password) {
    return ElMessage.warning('请输入密码')
  }
  
  try {
    const response = await axiosInstance.post('/user/login', loginForm.value)
    if (response.data && response.data.success) {
      localStorage.setItem('currentUser', JSON.stringify({
        username: response.data.username,
        role: response.data.role
      }))
      ElMessage.success('登录成功')
      router.push('/dessert')
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } catch (error) {
    ElMessage.error('登录失败，请稍后重试')
    console.error(error)
  }
}

const goToRegister = () => {
  router.push({ path: '/user' })
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.card-header {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.button-group {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  margin-top: 10px;
}

.login-btn {
  flex: 1;
  height: 40px;
  font-size: 16px;
}

.register-btn {
  flex: 1;
  height: 40px;
  font-size: 16px;
}
</style>