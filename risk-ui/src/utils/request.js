import axios from 'axios'
import { ElMessage } from 'element-plus'

// 1. 创建一个专属的 axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080', // 你后端的统一地址
  timeout: 5000 // 请求超时时间
})

// 2. 请求拦截器：自动贴上 JWT 门禁卡！
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  
  // 【关键修复】如果是登录或注册接口，不要带 Token，直接放行！
  if (config.url === '/user/login' || config.url === '/user/register') {
      return config
  }

  if (token) {
    config.headers['Authorization'] = token
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 3. 响应拦截器：统一处理报错
request.interceptors.response.use(response => {
  let res = response.data
  if (res.code !== 200) {
    ElMessage.error(res.message || '系统错误')
    return Promise.reject(new Error(res.message))
  }
  return res 
}, error => {
  ElMessage.error('网络连接异常，请检查后端！')
  return Promise.reject(error)
})

export default request