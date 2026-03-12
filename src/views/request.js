import axios from 'axios'
import { ElMessage } from 'element-plus'

// 1. 创建一个专属的 axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080', // 你后端的统一地址
  timeout: 5000 // 请求超时时间
})

// 2. 请求拦截器（快递员出门前）：自动贴上 JWT 门禁卡！
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token') // 从浏览器兜里掏出 token
  if (token) {
    config.headers['Authorization'] = token // 霸气地拍在请求头上
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 3. 响应拦截器（快递员回来后）：统一处理报错
request.interceptors.response.use(response => {
  let res = response.data
  // 如果后端返回的 code 不是 200，说明业务报错了（比如越权、密码错）
  if (res.code !== 200) {
    ElMessage.error(res.message || '系统错误')
    return Promise.reject(new Error(res.message))
  }
  return res // 正常情况，直接把那一层 data 剥出来给页面用
}, error => {
  ElMessage.error('网络连接异常，请检查后端！')
  return Promise.reject(error)
})

export default request