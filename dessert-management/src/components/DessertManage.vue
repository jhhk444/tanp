<template>
  <div style="padding: 20px;">
    <h1 style="text-align: center; margin-bottom: 20px; font-size: 28px;">甜点信息</h1>

    <div style="display: flex; align-items: center; gap: 15px; margin-bottom: 15px; flex-wrap: wrap;">
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>分类</span>
        <el-select v-model="query.categoryId" placeholder="请选择" style="width: 150px;">
          <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
        </el-select>
      </div>
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>名称</span>
        <el-input v-model="query.name" placeholder="请输入名称" style="width: 150px;" />
      </div>
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>描述</span>
        <el-input v-model="query.description" placeholder="请输入描述" style="width: 180px;" />
      </div>
      <div style="display: flex; align-items: center; gap: 5px;">
        <span>价格</span>
        <el-input v-model.number="query.minPrice" placeholder="最低价" style="width: 80px;" />
        <span>~</span>
        <el-input v-model.number="query.maxPrice" placeholder="最高价" style="width: 80px;" />
      </div>
      <el-button type="primary" @click="getList">查询</el-button>
      <el-button @click="resetQuery">重置</el-button>
    </div>

    <div style="margin-bottom: 15px;" v-if="isAdmin">
      <el-button type="danger" @click="batchDelete">删除</el-button>
      <el-button type="primary" @click="openAdd">新增</el-button>
    </div>

    <el-table :data="list" border @selection-change="handleSelectionChange" style="width: 100%;">
      <el-table-column type="selection" width="60" align="center" v-if="isAdmin" />
      <el-table-column label="图片" align="center" width="100">
        <template #default="scope">
          <div class="dessert-image-container">
            <el-image 
              :src="scope.row.image" 
              class="dessert-image"
              :preview-src-list="[scope.row.image]"
              preview-teleported
              fit="cover"
            >
              <template #error>
                <div class="image-placeholder">
                  <span class="placeholder-icon">🍰</span>
                </div>
              </template>
              <template #loading>
                <div class="image-placeholder">
                  <span class="placeholder-icon">⏳</span>
                </div>
              </template>
            </el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="甜点名称" prop="name" align="center" />
      <el-table-column label="所属分类" align="center">
        <template #default="scope">
          {{ getCategoryName(scope.row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column label="单价" prop="price" align="center" />
      <el-table-column label="描述" prop="description" align="center" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="150" v-if="isAdmin">
        <template #default="scope">
          <el-button size="small" type="primary" @click="openEdit(scope.row)">修改</el-button>
          <el-button size="small" type="danger" @click="deleteItem(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="display: flex; justify-content: center; margin-top: 20px; align-items: center; gap: 10px;">
      <el-pagination v-model:current-page="page" :total="total" layout="prev, pager, next" @current-change="getList" />
      <span>共 {{ total }} 条</span>
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑甜点' : '新增甜点'">
      <el-form label-width="100px" model="form">
        <el-form-item label="图片链接">
          <el-input v-model="form.image" placeholder="输入图片链接" />
        </el-form-item>
        <el-form-item label="甜点名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="所属分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="单价">
          <el-input v-model.number="form.price" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
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

const list = ref([])
const total = ref(0)
const page = ref(1)
const query = ref({ categoryId: '', name: '', description: '', minPrice: '', maxPrice: '' })
const selectedIds = ref([])
const dialogVisible = ref(false)
const form = ref({ id: '', image: '', name: '', categoryId: '', price: '', description: '' })
const categories = ref([])

const currentUser = ref(null)
const isAdmin = computed(() => {
  return currentUser.value && currentUser.value.role === 'admin'
})

const getCurrentUser = () => {
  const userData = localStorage.getItem('currentUser')
  if (userData) {
    currentUser.value = JSON.parse(userData)
  }
}

const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

const getList = async () => {
  try {
    const params = {
      page: page.value,
      size: 10,
      categoryId: query.value.categoryId === '' ? undefined : query.value.categoryId,
      name: query.value.name || undefined,
      description: query.value.description || undefined,
      minPrice: query.value.minPrice || undefined,
      maxPrice: query.value.maxPrice || undefined
    }
    const response = await axiosInstance.get('/dessert/list', { params })
    list.value = response.data.records || []
    total.value = response.data.total || 0
  } catch (error) {
    ElMessage.error('获取数据失败')
    console.error(error)
  }
}

const getCategories = async () => {
  try {
    const response = await axiosInstance.get('/category/list')
    categories.value = response.data
  } catch (error) {
    ElMessage.error('获取分类列表失败')
    console.error(error)
  }
}

const getCategoryName = (categoryId) => {
  const category = categories.value.find(cat => cat.id === categoryId)
  return category ? category.name : '未知分类'
}

const resetQuery = () => {
  query.value = { categoryId: '', name: '', description: '', minPrice: '', maxPrice: '' }
  page.value = 1
  getList()
}

const handleSelectionChange = (val) => {
  selectedIds.value = val.map(item => item.id)
}

const batchDelete = async () => {
  if (selectedIds.value.length === 0) return ElMessage.warning('请选择要删除的数据')
  await ElMessageBox.confirm('确定删除选中的甜点？', '提示', { type: 'warning' })
  try {
    await axiosInstance.post('/dessert/batchDelete', { ids: selectedIds.value })
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const deleteItem = async (id) => {
  await ElMessageBox.confirm('确定删除该甜点？', '提示', { type: 'warning' })
  try {
    await axiosInstance.delete(`/dessert/delete/${id}`)
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const openAdd = () => {
  form.value = { id: '', image: '', name: '', categoryId: '', price: '', description: '' }
  dialogVisible.value = true
}

const openEdit = (row) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const save = async () => {
  if (!form.value.name) {
    return ElMessage.warning('请输入甜点名称')
  }
  
  if (!form.value.price && form.value.price !== 0) {
    return ElMessage.warning('请输入单价')
  }
  
  if (form.value.price < 0) {
    return ElMessage.warning('单价不能为负数')
  }
  
  if (!form.value.categoryId) {
    return ElMessage.warning('请选择分类')
  }
  
  try {
    if (form.value.id) {
      await axiosInstance.put('/dessert/update', form.value)
      ElMessage.success('修改成功')
    } else {
      await axiosInstance.post('/dessert/add', form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error(form.value.id ? '修改失败' : '新增失败')
  }
}

onMounted(() => {
  getCurrentUser()
  getCategories()
  getList()
})
</script>

<style scoped>
.dessert-image-container {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.dessert-image-container:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.dessert-image {
  width: 100%;
  height: 100%;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.placeholder-icon {
  font-size: 24px;
}
</style>