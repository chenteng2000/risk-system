<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>投资项目风险管理系统</h2>
          <p>{{ isRegister ? '注册新账号' : 'Investment Risk Management' }}</p>
        </div>
      </template>
      
      <!-- 登录表单 -->
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" size="large">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" :placeholder="isRegister ? '请输入新账号' : '请输入账号 (如: admin)'">
            <!-- 账号图标 -->
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" :placeholder="isRegister ? '请输入新密码' : '请输入密码 (如: 123456)'" show-password @keyup.enter="handleLogin">
            <!-- 密码图标 -->
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
            {{ isRegister ? '注 册 新 账 号' : '登 录 进 入 系 统' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 在登录按钮下方添加切换按钮 -->
      <div style="text-align: center; margin-top: 15px;">
        <el-link type="primary" @click="toggleRegister">
          {{ isRegister ? '已有账号？去登录' : '没有账号？点击注册' }}
        </el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue' // 引入图标
import request from '../utils/request'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)
const isRegister = ref(false) // 切换登录/注册模式

// 切换登录和注册
const toggleRegister = () => {
  isRegister.value = !isRegister.value
  loginForm.username = ''
  loginForm.password = ''
}

// 表单绑定的数据对象
const loginForm = reactive({
  username: '',
  password: ''
})

// 表单必填校验规则
const rules = {
  username:[{ required: true, message: '账号不能为空', trigger: 'blur' }],
  password:[{ required: true, message: '密码不能为空', trigger: 'blur' }]
}

// 点击登录按钮触发的核心逻辑！！！
const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 根据 isRegister 状态，自动切换请求地址
        const url = isRegister.value ? '/user/register' : '/user/login'
        const res = await request.post(url, loginForm)
        
        if (isRegister.value) {
            ElMessage.success('注册成功，请登录！')
            isRegister.value = false
            loginForm.username = ''
            loginForm.password = ''
        } else {
            // 登录成功
            ElMessage.success('登录成功！')
            
            // 存储 token 和真实姓名 (后端返回的 res.data 是 { token: '...', realName: '...' })
            localStorage.setItem('token', res.data.token)
            localStorage.setItem('realName', res.data.realName || '投资经理')
            
            router.push('/dashboard')
        }
      } catch (e) {
        ElMessage.error(e.message)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
/* 超炫酷的渐变背景色 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #1d2b64 0%, #f8cdda 100%); 
}

/* 悬浮的玻璃质感卡片 */
.login-card {
  width: 420px;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0,0,0,0.3);
  border: none;
  background-color: rgba(255, 255, 255, 0.95);
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #1d2b64;
  font-size: 24px;
  letter-spacing: 2px;
}

.card-header p {
  margin: 5px 0 0 0;
  color: #909399;
  font-size: 14px;
}

.login-btn {
  width: 100%;
  font-size: 16px;
  font-weight: bold;
  letter-spacing: 4px;
  border-radius: 8px;
  background: linear-gradient(90deg, #1d2b64 0%, #304396 100%);
  border: none;
}

.login-btn:hover {
  background: linear-gradient(90deg, #304396 0%, #1d2b64 100%);
}
</style>