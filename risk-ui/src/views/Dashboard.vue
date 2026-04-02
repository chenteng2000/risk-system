<template>
  <div class="dashboard-container">
    <!-- 第一行：顶层数字战报 (4张卡片) -->
    <el-row :gutter="20" class="summary-row">
      <el-col :span="6" v-for="item in summaryCards" :key="item.title">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">{{ item.title }}</div>
          <div class="stat-value" :style="{color: item.color}">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：项目选择器 + 核心雷达图 + 智能建议 -->
    <el-row :gutter="20">
      <!-- 左侧：雷达图控制中心 -->
      <el-col :span="15">
        <el-card shadow="hover" class="main-card">
          <template #header>
            <div class="card-header-flex">
              <span class="title-text">📊 风险健康度评估：{{ currentProjectName }}</span>
              <!-- 保留：项目切换下拉框 -->
              <el-select v-model="currentProjectId" @change="handleProjectChange" placeholder="切换项目" size="default" style="width: 200px">
                <el-option v-for="p in projectList" :key="p.id" :label="p.projectName" :value="p.id" />
              </el-select>
            </div>
          </template>
          <div ref="radarRef" style="width: 100%; height: 400px;"></div>
        </el-card>
      </el-col>

      <!-- 右侧：动态建议列表 -->
      <el-col :span="9">
        <el-card shadow="hover" class="main-card">
          <template #header>
            <div class="card-header"><span>💡 智能风险防控建议</span></div>
          </template>
          <div class="advice-content">
            <div v-if="adviceList.length === 0">
              <el-empty description="暂无评估数据" :image-size="80" />
            </div>
            <div v-else>
               <p v-for="(advice, index) in adviceList" :key="index" class="advice-item">
                 <span v-html="advice"></span>
               </p>
               <el-divider />
               <p class="notice">根据 P×I 模型自动计算生成的实时建议</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：新增的视觉图表 (仪表盘 + 饼图 + 排行榜) -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 仪表盘 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><div class="card-header"><span>⏱️ 项目综合危险系数</span></div></template>
          <div ref="gaugeRef" style="height: 280px;"></div>
        </el-card>
      </el-col>
      <!-- 饼图 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><div class="card-header"><span>🍩 全局风险等级分布</span></div></template>
          <div ref="pieRef" style="height: 280px;"></div>
        </el-card>
      </el-col>
      <!-- 排行榜 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><div class="card-header"><span>🏆 风险预警排行榜</span></div></template>
          <el-table :data="rankingList" stripe style="width: 100%" height="240" size="small">
            <el-table-column type="index" label="排位" width="50" />
            <el-table-column prop="projectName" label="项目" show-overflow-tooltip />
            <el-table-column prop="maxScore" label="峰值" width="70">
              <template #default="scope">
                <span style="color: #F56C6C; font-weight: bold;">{{ scope.row.maxScore }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from 'vue'
import { useRoute } from 'vue-router'
import * as echarts from 'echarts'
import request from '../utils/request'

const route = useRoute()

// --- DOM 引用 ---
const radarRef = ref(null)
const gaugeRef = ref(null)
const pieRef = ref(null)

// --- 数据状态 ---
const currentProjectId = ref(Number(route.query.projectId) || 1)
const currentProjectName = ref(route.query.projectName || '正在加载...')
const projectList = ref([])
const adviceList = ref([])
const rankingList = ref([])
const summaryCards = ref([
  { title: '监控项目总数', value: '0', color: '#409EFF' },
  { title: '高风险预警项', value: '0', color: '#F56C6C' },
  { title: '系统安全运行', value: '128天', color: '#67C23A' },
  { title: '模型计算引擎', value: 'P×I 2.0', color: '#E6A23C' }
])

// --- ECharts 实例引用 (防止重复初始化) ---
let radarChart = null
let gaugeChart = null
let pieChart = null

// --- 方法：初始化仪表盘 ---
const initGauge = (score) => {
  if (!gaugeChart) gaugeChart = echarts.init(gaugeRef.value)
  gaugeChart.setOption({
    series: [{
      type: 'gauge',
      startAngle: 200, endAngle: -20, min: 0, max: 120,
      axisLine: { lineStyle: { width: 8, color: [[0.3, '#67C23A'], [0.7, '#E6A23C'], [1, '#F56C6C']] } },
      progress: { show: true, width: 8 },
      pointer: { length: '60%', width: 4 },
      detail: { valueAnimation: true, formatter: '{value}', fontSize: 24, offsetCenter: [0, '70%'] },
      data: [{ value: score, name: '分' }]
    }]
  })
}

// --- 方法：初始化饼图 ---
const initPieChart = async () => {
  try {
    const res = await request.get('/risk/distribution')
    if (!pieChart) pieChart = echarts.init(pieRef.value)
    pieChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: '0', icon: 'circle', itemWidth: 8 },
      series: [{
        type: 'pie', radius: ['40%', '70%'], avoidLabelOverlap: false,
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        data: res.data
      }]
    })
  } catch (e) { console.error(e) }
}

// --- 方法：生成建议逻辑 (保留你原来的心血) ---
const generateAdvice = (riskData) => {
  if (!riskData || riskData.length === 0) {
    adviceList.value = []; return
  }
  adviceList.value = riskData.map(item => {
    if (item.score > 80) return `🚨 <b style="color:#F56C6C">${item.category}</b>：风险极高，建议立即暂停投资并启动二级风控预案。`
    if (item.score > 50) return `⚠️ <b>${item.category}</b>：中度偏高，需在季度审计中重点关注数据披露。`
    return `✅ <b>${item.category}</b>：状态良好，维持现有监管频率即可。`
  })
}

// --- 方法：核心雷达图渲染 (保留详细配置) ---
const initRadarChart = async () => {
  try {
    const res = await request.get(`/risk/radar/${currentProjectId.value}`)
    const riskData = res.data || []
    generateAdvice(riskData)
    
    // 联动：同步更新仪表盘
    const avg = riskData.length ? (riskData.reduce((a,b)=>a+b.score,0)/riskData.length).toFixed(1) : 0
    initGauge(avg)

    if (!radarChart) radarChart = echarts.init(radarRef.value)
    
    const indicators = [
      { name: '市场风险', max: 100 }, { name: '合规风险', max: 150 },
      { name: '信用风险', max: 100 }, { name: '操作风险', max: 100 },
      { name: '财务风险', max: 150 }
    ]
    const values = indicators.map(i => {
      const found = riskData.find(d => d.category === i.name)
      return found ? found.score : 0
    })

    radarChart.setOption({
      tooltip: {},
      radar: { 
        indicator: indicators, shape: 'circle', 
        splitArea: { areaStyle: { color: ['#fff', '#f6f6f6'] } } 
      },
      series: [{
        type: 'radar',
        data: [{ 
          value: values, name: currentProjectName.value,
          areaStyle: { color: 'rgba(64, 158, 255, 0.3)' },
          lineStyle: { color: '#409EFF', width: 2 }
        }]
      }]
    })
  } catch (e) { console.error(e) }
}

// --- 交互逻辑 ---
const handleProjectChange = (val) => {
  const p = projectList.value.find(i => i.id === val)
  if (p) { currentProjectName.value = p.projectName; initRadarChart() }
}

const loadAllData = async () => {
  // 1. 加载项目列表
  const pRes = await request.get('/project/list')
  projectList.value = pRes.data
  summaryCards.value[0].value = projectList.value.length

  // 2. 加载排行榜
  const rRes = await request.get('/risk/ranking')
  rankingList.value = rRes.data
  summaryCards.value[1].value = rankingList.value.filter(i => i.maxScore > 80).length

  // 3. 渲染图表
  initRadarChart()
  initPieChart()
}

onMounted(loadAllData)

// 窗口自适应
window.onresize = () => {
  radarChart?.resize(); gaugeChart?.resize(); pieChart?.resize()
}
</script>

<style scoped>
.dashboard-container { padding: 20px; background-color: #f5f7fa; min-height: 100vh; }
.summary-row { margin-bottom: 20px; }
.stat-card { text-align: center; border: none; }
.stat-title { font-size: 13px; color: #909399; margin-bottom: 10px; }
.stat-value { font-size: 26px; font-weight: bold; }
.main-card { height: 500px; }
.card-header-flex { display: flex; justify-content: space-between; align-items: center; }
.title-text { font-weight: bold; font-size: 16px; }
.advice-content { height: 380px; overflow-y: auto; padding-right: 10px; }
.advice-item { font-size: 14px; line-height: 1.8; margin-bottom: 12px; padding: 10px; background: #fdfdfd; border-left: 4px solid #409EFF; }
.notice { font-size: 12px; color: #999; font-style: italic; }
</style>