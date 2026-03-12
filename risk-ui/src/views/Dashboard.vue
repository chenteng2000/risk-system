<template>
  <div class="dashboard-container">
    <!-- 顶部数据统计卡片 -->
    <el-row :gutter="20" class="summary-row">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="statistic-card">
            <div class="stat-title">当前评估项目 (可快速切换)</div>
            <!-- 超炫的下拉框，用于快速切换项目 -->
            <el-select v-model="currentProjectId" @change="handleProjectChange" style="width: 80%" size="large">
              <el-option 
                v-for="p in projectList" 
                :key="p.id" 
                :label="p.projectName" 
                :value="p.id" 
              />
            </el-select>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="statistic-card">
            <div class="stat-title">应用评估模型
              <!-- 增加一个问号小图标，点击触发弹窗 -->
              <el-icon @click="showModelDialog = true" style="cursor: pointer; color: #409EFF;"><InfoFilled /></el-icon>
            </div>
            <div class="stat-value">P × I 风险矩阵模型</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="statistic-card">
            <div class="stat-title">综合健康状态</div>
            <!-- 根据风险最高分，动态改变状态颜色和文字 -->
            <div class="stat-value" :style="{ color: healthStatus.color }">
              {{ healthStatus.text }}
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表与建议区域 -->
    <el-row :gutter="20">
      <!-- 左侧：雷达图 -->
      <el-col :span="14">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>📊 {{ currentProjectName }} - 风险健康度多维评估</span>
            </div>
          </template>
          <!-- 这是 ECharts 的画布容器 -->
          <div ref="radarRef" style="width: 100%; height: 400px;"></div>
        </el-card>
      </el-col>

      <!-- 右侧：智能风险防控建议卡片 (动态渲染) -->
      <el-col :span="10">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>💡 智能风险防控建议</span>
            </div>
          </template>
          <div class="advice-content">
            <!-- 如果没有数据，显示提示 -->
            <div v-if="adviceList.length === 0" class="empty-tips">
              <el-empty description="该项目暂未进行风险评估" :image-size="100" />
            </div>
            
            <!-- 如果有数据，循环渲染生成的建议 -->
            <div v-else>
               <p v-for="(advice, index) in adviceList" :key="index" class="advice-item">
                 <!-- 使用 v-html 渲染带有颜色标签的文字 -->
                 <span v-html="advice"></span>
               </p>
               <el-divider />
               <p class="notice"><i>* 以上建议基于系统 P×I 风险矩阵算法自动生成，仅供投资决策委员会参考。</i></p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 模型说明弹窗 -->
    <el-dialog v-model="showModelDialog" title="P × I 风险矩阵模型说明" width="600px" append-to-body>
      <div class="model-explanation">
        <h3>📊 模型定义</h3>
        <p>P × I 风险矩阵是投资风险评估的标准模型，其中：</p>
        <ul>
          <li><b>P (Probability)</b>：风险发生的概率，范围 0-100</li>
          <li><b>I (Impact)</b>：风险发生后的影响程度，范围 0-100</li>
          <li><b>风险值 = P × I</b>：综合评分，最高可达 10,000</li>
        </ul>
        
        <h3>📈 风险等级划分</h3>
        <el-table :data="riskLevels" border stripe style="margin-top: 10px;">
          <el-table-column prop="level" label="风险等级" width="100"></el-table-column>
          <el-table-column prop="range" label="数值范围"></el-table-column>
          <el-table-column prop="color" label="颜色标记" width="80">
            <template #default="scope">
              <span :style="{ color: scope.row.color, fontWeight: 'bold' }">●</span>
            </template>
          </el-table-column>
          <el-table-column prop="action" label="建议操作"></el-table-column>
        </el-table>

        <h3>🎯 应用场景</h3>
        <p>系统自动计算每个投资项目在 5 个维度的风险评分，通过雷达图呈现多维度的风险健康状态，帮助投资决策委员会更好地评估项目风险。</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import * as echarts from 'echarts'
import { InfoFilled } from '@element-plus/icons-vue'
import request from '../utils/request'

const route = useRoute()

// --- 核心响应式数据 ---
const radarRef = ref(null)
let myChart = null
const projectList = ref([]) // 存放下拉框的项目列表
const adviceList = ref([]) // 存放动态生成的防空建议

// 当前选中的项目 (如果从表格点击跳过来，优先用传过来的参数，否则默认选 1 号)
const currentProjectId = ref(Number(route.query.projectId) || 1)
const currentProjectName = ref(route.query.projectName || '正在加载...')

// 顶部综合健康状态
const healthStatus = reactive({ text: '实时监控中', color: '#67C23A' })

// 模型说明弹窗
const showModelDialog = ref(false)

// 风险等级表数据
const riskLevels = ref([
  { level: '低风险', range: '0-1000', color: '#67C23A', action: '持续监测' },
  { level: '中低风险', range: '1001-3000', color: '#E6A23C', action: '加强监测' },
  { level: '中高风险', range: '3001-6000', color: '#FB8500', action: '定期评估' },
  { level: '高风险', range: '6001-10000', color: '#F56C6C', action: '立即干预' }
])


// --- 核心方法一：拉取所有项目名单供下拉框使用 ---
const fetchProjectList = async () => {
  try {
    const res = await request.get('/project/list')
    projectList.value = res.data
    
    // 如果是刚进入页面（没传参），用列表里的第一个项目作为默认名称
    if (!route.query.projectName && projectList.value.length > 0) {
       const defaultProject = projectList.value.find(p => p.id === currentProjectId.value)
       if(defaultProject) {
           currentProjectName.value = defaultProject.projectName
       }
    }
  } catch (error) {
    console.error("获取项目列表失败", error)
  }
}

// --- 核心方法二：下拉框切换项目时触发 ---
const handleProjectChange = (newProjectId) => {
  const selected = projectList.value.find(p => p.id === newProjectId)
  if (selected) {
    currentProjectName.value = selected.projectName
    initRadarChart() // 触发重新画图和计算建议！
  }
}

// --- 核心方法三：获取数据、画雷达图、生成建议 ---
const initRadarChart = async () => {
  try {
    // 1. 向后端请求当前选中项目的雷达图数据
    const res = await request.get(`/risk/radar/${currentProjectId.value}`)
    const riskData = res.data ||[]

    // 2. 触发：根据真实数据生成文字建议
    generateAdvice(riskData)

    // 3. 准备 ECharts 需要的标准维度
    const indicators =[
      { name: '市场风险', max: 100 },
      { name: '信用风险', max: 100 },
      { name: '操作风险', max: 100 },
      { name: '财务风险', max: 100 },
      { name: '合规风险', max: 100 }
    ]

    // 4. 将后端查回来的分数匹配到这 5 个维度中 (查不到的算 0 分)
    const scoreValues = indicators.map(ind => {
      const found = riskData.find(item => item.category === ind.name)
      return found ? found.score : 0
    })

    // 5. 初始化或更新 ECharts 实例
    if (!myChart) {
      myChart = echarts.init(radarRef.value)
      window.addEventListener('resize', () => { myChart.resize() })
    }

    // 6. 极具科技感的雷达图配置项
    const option = {
      tooltip: { trigger: 'item' },
      legend: { bottom: 0, data:[`${currentProjectName.value} 风险值`] },
      radar: {
        indicator: indicators,
        shape: 'polygon',
        splitArea: { areaStyle: { color:['rgba(250,250,250,0.3)', 'rgba(200,200,200,0.1)'] } }
      },
      series:[
        {
          name: '综合风险评估',
          type: 'radar',
          data:[
            {
              value: scoreValues,
              name: `${currentProjectName.value} 风险值`,
              areaStyle: { color: 'rgba(64, 158, 255, 0.4)' }, // 科技蓝半透明填充
              lineStyle: { color: '#409EFF', width: 2 },
              itemStyle: { color: '#409EFF' }
            }
          ]
        }
      ]
    }
    
    // 如果所有分数都是 0，稍微提示一下
    if(scoreValues.every(val => val === 0)) {
        myChart.clear()
        return
    }

    myChart.setOption(option)

  } catch (error) {
    console.error("获取雷达图数据失败：", error)
  }
}

// --- 核心方法四：算法级建议生成器 ---
// --- 核心方法四：算法级建议生成器 (接着上面的补全) ---
const generateAdvice = (riskData) => {
  if (!riskData || riskData.length === 0) {
    adviceList.value = []
    healthStatus.text = '暂无数据'
    healthStatus.color = '#909399'
    return
  }
  
  // 根据分值高低，自动生成对应建议
  adviceList.value = riskData.map(item => {
    if (item.score > 80) return `🚨 <b>${item.category}</b> 高风险：当前风险极高，请立即采取应急预案！`
    if (item.score > 50) return `⚠️ <b>${item.category}</b> 中风险：建议加强日常监测与预警。`
    return `✅ <b>${item.category}</b> 低风险：整体处于可控范围。`
  })

  // 动态更新右上角状态：只要有一个风险分 > 80，就亮红灯
  const maxScore = Math.max(...riskData.map(i => i.score))
  if (maxScore > 80) {
    healthStatus.text = '预警！风险较高'
    healthStatus.color = '#F56C6C'
  } else {
    healthStatus.text = '健康状态良好'
    healthStatus.color = '#67C23A'
  }
}

// 页面加载时的终极初始化
onMounted(() => {
  fetchProjectList() // 先拿名单
  initRadarChart()   // 再画图并算建议
})
</script>

<style scoped>
.dashboard-container { margin-top: 5px; }
.summary-row { margin-bottom: 20px; }
.statistic-card { text-align: center; padding: 10px 0; }
.stat-title { font-size: 14px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 20px; font-weight: bold; }
.card-header { font-weight: bold; font-size: 16px; color: #303133; }
.advice-content { height: 350px; color: #606266; line-height: 2.2; font-size: 15px; }
.advice-item { border-bottom: 1px dashed #eee; padding-bottom: 5px; }
.notice { font-size: 12px; color: #999; margin-top: 20px; font-style: italic; }

/* 模型说明弹窗样式 */
.model-explanation {
  color: #333;
  line-height: 1.8;
}

.model-explanation h3 {
  margin-top: 15px;
  margin-bottom: 10px;
  color: #409EFF;
  font-weight: bold;
}

.model-explanation p {
  margin: 8px 0;
  font-size: 14px;
}

.model-explanation ul {
  margin-left: 20px;
}

.model-explanation li {
  margin: 6px 0;
  font-size: 14px;
}
</style>