<template>
  <div class="app-container">
    <el-card shadow="never" class="filter-container">
      <div class="action-bar">
        <!-- 左侧搜索区 -->
        <div class="left-action">
          <el-input v-model="searchKeyword" placeholder="请输入项目名称" style="width: 250px; margin-right: 10px;" />
          <el-button type="primary" @click="fetchData">搜索</el-button>
        </div>
        
        <!-- 右侧按钮区 -->
        <div class="right-action">
          <!-- 在导入 Excel 按钮的旁边加一个下载按钮 -->
          <el-button type="info" @click="downloadTemplate" style="margin-right: 10px;">
            下载导入模板
          </el-button>

          <!-- 修复点 1：使用在 script 中定义好的 uploadHeaders -->
          <el-upload
            action="http://localhost:8080/project/import"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            class="upload-demo"
          >
            <el-button type="warning">导入 Excel</el-button>
          </el-upload>
          
          <el-button type="success" @click="showAddDialog = true" style="margin-left: 10px;">
            新增投资项目
          </el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" style="margin-top: 15px;">
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="projectName" label="项目名称" min-width="180" />
        <el-table-column prop="projectCode" label="项目编号" width="150" />
        <el-table-column prop="stage" label="投资阶段" width="120" />
        <el-table-column prop="investmentAmount" label="拟投金额(万)" width="150" align="right">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold;">￥{{ scope.row.investmentAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button size="small" type="primary" link @click="goToRiskDashboard(scope.row)">
              查看风险
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 修复点 2：加上 append-to-body 防止 parentNode 报错 -->
    <el-dialog v-model="showAddDialog" title="新建投资项目" width="500px" append-to-body>
      <el-form :model="form" label-width="100px">
        <el-form-item label="项目名称"><el-input v-model="form.projectName" /></el-form-item>
        <el-form-item label="项目编号"><el-input v-model="form.projectCode" /></el-form-item>
        <el-form-item label="拟投金额"><el-input v-model="form.investmentAmount" type="number" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveProject">确认保存</el-button>
      </template>
    </el-dialog>

    <!-- 模型说明弹窗 -->
    <el-dialog v-model="showModelDialog" title="风险矩阵模型说明 (P × I)" width="500px">
      <div style="line-height: 1.8; color: #606266;">
        <p><b>1. 核心公式：</b> Risk Score = Probability(P) × Impact(I) × Weight(W)</p>
        <p><b>2. 参数说明：</b></p>
        <ul>
          <li><b>P (可能性)：</b> 风险发生的概率（1-10分）。</li>
          <li><b>I (影响程度)：</b> 风险发生后带来的损失大小（1-10分）。</li>
          <li><b>W (权重)：</b> 系统根据风险维度配置的加权系数（如市场风险 W=1.2）。</li>
        </ul>
        <p><b>3. 模型逻辑：</b> 该模型将定性的风险描述转化为定量的数值，旨在为投资决策委员会提供标准化的风险预警基准。本系统支持通过扩展数据库维度，灵活调整评估指标。</p>
      </div>
      <template #footer>
        <el-button type="primary" @click="showModelDialog = false">我知道了</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'
import request from '../utils/request'

// --- 必须定义在最外层，不要写在函数里 ---
const router = useRouter()
const tableData = ref([])      // <--- 这一行至关重要！
const loading = ref(false)
const searchKeyword = ref('')
const showAddDialog = ref(false)
const showModelDialog = ref(false) // 定义弹窗显示状态
const form = reactive({ projectName: '', projectCode: '', stage: '天使轮', investmentAmount: '' })

// 定义函数
const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/project/list')
    // 确保返回的是数组，如果是 Result 封装格式，res.data 就是数组
    tableData.value = res.data || [] 
  } catch (e) {
    ElMessage.error('加载项目列表失败')
  } finally {
    loading.value = false
  }
}

const saveProject = async () => {
  try {
    await request.post('/project/add', form)
    ElMessage.success('保存成功')
    showAddDialog.value = false
    fetchData()
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

const goToRiskDashboard = (row) => {
  router.push({
    path: '/dashboard',
    query: { projectId: row.id, projectName: row.projectName }
  })
}

// 上传 Excel 表头认证信息
const uploadHeaders = ref({
  'Authorization': localStorage.getItem('token') || ''
})

// 处理上传成功
const handleUploadSuccess = (response, file, fileList) => {
  if (response.code === 200) {
    ElMessage.success('Excel 导入成功')
    fetchData() // 重新加载数据
  } else {
    ElMessage.error(response.message || 'Excel 导入失败')
  }
}

// 处理上传失败
const handleUploadError = (error, file, fileList) => {
  ElMessage.error('Excel 导入失败，请检查文件格式')
}

// 下载导入模板
const downloadTemplate = () => {
  // 因为放在了 public 文件夹，所以直接访问根路径下的文件名即可
  // 浏览器会直接下载它，不需要任何 Token，后端根本不会参与！
  window.location.href = '/project_template.xlsx'
}

onMounted(() => {
  fetchData()
})
</script>


<style scoped>
.app-container { padding: 20px; }
.action-bar { display: flex; justify-content: space-between; align-items: center; }
.upload-demo { display: inline-block; }
</style>