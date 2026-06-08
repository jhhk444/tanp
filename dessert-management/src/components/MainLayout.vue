<template>
  <div class="main-container">
    <div class="header">
      <div class="logo">甜点信息管理系统</div>
      <div class="user-bar">
        <span v-if="!currentUser" class="nav-item" @click="goToLogin">⮕ 登录</span>
        <template v-else>
          <span class="nav-item">欢迎 {{ currentUser.username }} ({{ currentUser.role === 'admin' ? '管理员' : '普通用户' }})</span>
          <span class="nav-item" @click="handleLogout">⮕ 退出</span>
        </template>
      </div>
    </div>

    <div class="content-wrapper">
      <div class="sidebar">
        <el-menu
            mode="vertical"
            background-color="#CCCCCC"
            text-color="#000000"
            active-text-color="#FFFFFF"
            active-background-color="#333333"
            style="border: none; width: 100%;"
            router
        >
          <el-menu-item index="/user">用户注册</el-menu-item>
          <el-menu-item index="/usermanage" v-if="currentUser && currentUser.role === 'admin'">用户信息管理</el-menu-item>
          <el-menu-item index="/category" v-if="currentUser && currentUser.role === 'admin'">分类管理</el-menu-item>
          <el-menu-item index="/dessert">甜品管理</el-menu-item>
        </el-menu>
      </div>

      <div class="main-content">
        <router-view />
      </div>
    </div>

    <div class="footer">Copyright © 2024</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const currentUser = ref(null)

const getCurrentUser = () => {
  const userData = localStorage.getItem('currentUser')
  if (userData) {
    currentUser.value = JSON.parse(userData)
  }
}

const goToLogin = () => {
  router.push('/login')
}

const handleLogout = async () => {
  await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
  localStorage.removeItem('currentUser')
  currentUser.value = null
  ElMessage.success('退出成功')
  router.push('/login')
}


onMounted(() => {
  getCurrentUser()
})
</script>

<style scoped>
.main-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin: 0;
  padding: 0;
}

.header {
  height: 60px;
  line-height: 60px;
  background-color: #7e57c2;
  color: #ffffff;
  padding: 0 20px;
  font-size: 20px;
  font-weight: bold;
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-bar {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 18px;
  font-weight: normal;
}

.nav-item {
  cursor: pointer;
  transition: opacity 0.2s;
}
.nav-item:hover {
  opacity: 0.8;
}

.content-wrapper {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 180px;
  background-color: #cccccc;
  padding: 20px 0;
  flex-shrink: 0;
}

.main-content {
  flex: 1;
  background-color: #ffffff;
  padding: 20px;
  overflow-y: auto;
  min-height: 0;
}

.footer
{
  height: 40px;
  line-height: 40px;
  background-color: #7e57c2;
  color: #ffffff;
  text-align: center;
  flex-shrink: 0;
}
</style>