import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../components/MainLayout.vue'
import CategoryManage from '../components/CategoryManage.vue'
import DessertManage from '../components/DessertManage.vue'
import UserRegister from '../components/UserRegister.vue'
import UserManage from '../components/UserManage.vue'
import Login from '../components/Login.vue'

const routes = [
    {
        path: '/login',
        component: Login
    },
    {
        path: '/',
        component: MainLayout,
        children: [
            { path: 'user', component: UserRegister },
            { path: 'usermanage', component: UserManage },
            { path: 'category', component: CategoryManage },
            { path: 'dessert', component: DessertManage },
            { path: '', redirect: '/dessert' }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const currentUser = localStorage.getItem('currentUser')
    if (to.path !== '/login' && to.path !== '/user' && !currentUser) {
        next('/login')
    } else {
        next()
    }
})

export default router

