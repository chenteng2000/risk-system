<template>
  <div class="app-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header"><span>📄 系统操作审计日志 (只读不可改)</span></div>
      </template>
      <el-table :data="logList" border stripe style="width: 100%" height="calc(100vh - 200px)">
        <el-table-column prop="createTime" label="操作时间" width="180" />
        <el-table-column prop="username" label="操作人" width="120" />
        <el-table-column prop="operation" label="操作描述" width="180">
          <template #default="scope">
            <el-tag :type="scope.row.operation.includes('删除') ? 'danger' : 'info'">{{ scope.row.operation }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="method" label="请求接口" />
        <el-table-column prop="params" label="操作参数" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const logList = ref([])

const fetchLogs = async () => {
  const res = await request.get('/log/list')
  logList.value = res.data
}

onMounted(fetchLogs)
</script>
