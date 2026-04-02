import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Dashboard from '../views/Dashboard.vue'
import ProjectList from '../views/ProjectList.vue' // 1. 顶部引入新页面
import SystemLog from '../views/SystemLog.vue' // 引入系统日志组件

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes:[
    { path: '/login', name: 'login', component: Login },
    { path: '/', redirect: '/login' },
    {
      path: '/layout',
      component: Layout,
      children:[
        { path: '/dashboard', name: 'dashboard', component: Dashboard },
        // 2. 加这一行！将 /projects 网址和 ProjectList 页面绑定
        { path: '/projects', name: 'projects', component: ProjectList },
        { path: '/logs', name: 'logs', component: SystemLog } // 系统操作审计日志
      ]
    }
  ]
})

export default router