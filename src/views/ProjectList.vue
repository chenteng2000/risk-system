<template>
  <div class="app-container">
    <!-- 顶部操作区 -->
    <el-card shadow="never" class="filter-container">
      <div style="display: flex; justify-content: space-between;">
        <div>
          <el-input v-model="searchKeyword" placeholder="请输入项目名称检索" style="width: 250px; margin-right: 10px;">
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
          <el-button type="primary" @click="fetchData">搜索</el-button>
        </div>
        <el-button type="success" @click="showAddDialog = true">
          <el-icon style="margin-right: 5px"><Plus /></el-icon> 新增投资项目
        </el-button>
      </div>
    </el-card>

    <!-- 数据表格区 -->
    <el-card shadow="never" style="margin-top: 15px;">
      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="projectName" label="项目名称" min-width="180" />
        <el-table-column prop="projectCode" label="项目编号" width="150" />
        <el-table-column prop="stage" label="投资阶段" width="120">
          <template #default="scope">
            <el-tag>{{ scope.row.stage }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="investmentAmount" label="拟投金额(万)" width="150" align="right">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold;">￥{{ scope.row.investmentAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="立项时间" width="180" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default>
            <el-button size="small" type="primary" link>查看风险</el-button>
            <el-button size="small" type="danger" link>删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增项目弹窗 -->
    <el-dialog v-model="showAddDialog" title="新建投资项目档案" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="项目名称"><el-input v-model="form.projectName" /></el-form-item>
        <el-form-item label="项目编号"><el-input v-model="form.projectCode" /></el-form-item>
        <el-form-item label="投资阶段">
          <el-select v-model="form.stage" placeholder="请选择" style="width: 100%">
            <el-option label="天使轮" value="天使轮" />
            <el-option label="A轮" value="A轮" />
            <el-option label="B轮" value="B轮" />
            <el-option label="IPO准备" value="IPO准备" />
          </el-select>
        </el-form-item>
        <el-form-item label="拟投金额"><el-input v-model="form.investmentAmount" type="number"><template #append>万元</template></el-input></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveProject">确认保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request' // 引入带 Token 的请求工具

const tableData = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const showAddDialog = ref(false)

const form = reactive({
  projectName: '', projectCode: '', stage: '', investmentAmount: ''
})

// 1. 调用后端的 /project/list 接口获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/project/list')
    tableData.value = res.data // 把后端返回的数组塞给表格
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 2. 调用后端的 /project/add 接口保存数据
const saveProject = async () => {
  try {
    const res = await request.post('/project/add', form)
    ElMessage.success(res.message)
    showAddDialog.value = false // 关闭弹窗
    fetchData() // 重新刷新表格数据！
  } catch (error) {
    console.error(error)
  }
}

// 页面一加载，自动去拉取表格数据
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.app-container { padding: 0; }
.filter-container { margin-bottom: 20px; }
</style>