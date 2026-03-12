<template>
  <el-container class="layout-container">
    <!-- 左侧黑暗风菜单 -->
    <el-aside width="220px" class="aside">
      <div class="logo">风控系统 Pro</div>
      <!-- 注意这里：去掉了写死的菜单，改用 v-for 动态渲染 -->
      <el-menu active-text-color="#409eff" background-color="#001529" text-color="#fff" router default-active="/dashboard">
        <el-menu-item v-for="menu in menuList" :key="menu.id" :index="menu.path">
          <!-- 动态渲染图标和菜单名 -->
          <el-icon><Component :is="menu.perms" /></el-icon>
          <span>{{ menu.menuName }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="breadcrumb">欢迎来到投资项目风险管理系统</div>
        <div class="user-info">
          <el-avatar size="small" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
          <span class="username">{{ realName }}</span>
          <el-button type="danger" link @click="logout" style="margin-left: 15px">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

// 1. 全局注册所有 Element Plus 图标（这一步非常关键，否则 <Component :is="..."/> 会找不到图标）
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { getCurrentInstance } from 'vue'
const app = getCurrentInstance().appContext.app
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

const router = useRouter()
const menuList = ref([]) // 这里用来存放从后端抓回来的菜单
const realName = ref(localStorage.getItem('realName') || '投资经理')

// 2. 页面加载时，自动调用后端接口获取菜单数据
const fetchMenus = async () => {
  try {
    const res = await request.get('/menu/list')
    menuList.value = res.data // 把后端返回的菜单数组赋值给 menuList
  } catch (error) {
    ElMessage.error('菜单加载失败')
  }
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('realName')
  ElMessage.success('已安全退出')
  router.push('/login')
}

onMounted(() => {
  fetchMenus()
})
</script>
<style scoped>
/* 1. 让整个大框架占满整个屏幕 */
.layout-container {
  height: 100vh;
  width: 100vw;
}

/* 2. 左侧深色菜单栏：高度撑满，背景深蓝色 */
.aside {
  background-color: #001529;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 3. 左上角的系统 Logo 区域 */
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: white;
  font-size: 20px;
  font-weight: bold;
  background-color: #002140;
  letter-spacing: 1px;
}

/* 4. 去除菜单自带的右边框，让它看起来更平滑 */
.el-menu {
  border-right: none;
  flex: 1;
}

/* 5. 顶部白色导航栏：左右两端对齐，加个底边框 */
.header {
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e6e6e6;
  height: 60px;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  z-index: 10;
}

/* 6. 右上角头像和用户名垂直居中 */
.user-info {
  display: flex;
  align-items: center;
}

.username {
  margin-left: 10px;
  font-weight: bold;
  color: #333;
}

/* 7. 中间主要内容区域：灰白背景，留出内边距，内容多了可以滚动 */
.main {
  background-color: #f0f2f5;
  padding: 20px;
  height: calc(100vh - 60px);
  overflow-y: auto;
}
</style>