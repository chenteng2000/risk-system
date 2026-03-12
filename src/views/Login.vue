<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>投资项目风险管理系统</h2>
          <p>Investment Risk Management</p>
        </div>
      </template>
      
      <!-- 登录表单 -->
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" size="large">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入账号 (如: admin)">
            <!-- 账号图标 -->
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码 (如: 123456)" show-password @keyup.enter="handleLogin">
            <!-- 密码图标 -->
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
            登 录 进 入 系 统
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue' // 引入图标
import axios from 'axios'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

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
        // 建议：生产环境下应使用环境变量或 axios 实例配置 baseURL
        // 1. 发送登录请求
        const res = await axios.post('http://localhost:8080/user/login', loginForm)
        
        // 2. 拿到后端返回的 JSON 数据
        const data = res.data
        
        // 3. 判断是否登录成功 (我们在后端定义的成功状态码是 200)
        if (data.code === 200) {
          ElMessage.success('登录成功！欢迎回来。')
          
          // 【核心护城河】将拿到的 JWT Token 门禁卡存入浏览器的 localStorage！
          // 开题报告第 8 页“提交凭证返回JWT令牌”就在这里实现了闭环！
          localStorage.setItem('token', data.data)
          
          // 登录成功后，暂时随便跳到一个测试路由（后面我们会做后台主页）
          router.push('/dashboard')
        } else {
          // 密码错误等情况，弹出后端给的提示
          ElMessage.error(data.message || '登录失败')
        }
      } catch (error) {
        ElMessage.error('网络请求失败，请检查后端是否启动或跨域配置！')
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