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
          <span class="username">超级管理员</span>
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

// 动态注册所有的 Element Plus 图标，供后台数据调用
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { getCurrentInstance } from 'vue'
const app = getCurrentInstance().appContext.app
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

const router = useRouter()
const menuList = ref(