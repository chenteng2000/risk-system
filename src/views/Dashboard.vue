<template>
  <div class="dashboard-container">
    <el-row>
      <el-col :span="12">
        <!-- 一个漂亮的白色卡片用来装雷达图 -->
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>📊 项目A - 风险健康度多维评估</span>
            </div>
          </template>
          <!-- 这是 ECharts 的画布容器，必须指定宽高 -->
          <div ref="radarRef" style="width: 100%; height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request' // 引入我们配置好的、会自动带Token的快递员

const radarRef = ref(null)

// 初始化雷达图的核心方法
const initRadarChart = async () => {
  try {
    // 1. 【前后端联调核心】向后端请求雷达图专属数据！
    // 这里的 1 代表查询 projectId 为 1 的项目
    const res = await request.get('/risk/radar/1') 
    const riskData = res.data // 拿到后端返回的[{category: '市场风险', score: 67.2}, ...]

    // 2. 准备开题报告第9页要求的 5 个标准维度
    const indicators = [
      { name: '市场风险', max: 100 },
      { name: '信用风险', max: 100 },
      { name: '操作风险', max: 100 },
      { name: '财务风险', max: 100 },
      { name: '合规风险', max: 100 }
    ]

    // 3. 把后端的数据匹配到这 5 个维度里去（没有数据的暂时算 0 分）
    const scoreValues = indicators.map(ind => {
      const found = riskData.find(item => item.category === ind.name)
      return found ? found.score : 0
    })

    // 4. 初始化 ECharts 实例
    const myChart = echarts.init(radarRef.value)

    // 5. 极具科技感的雷达图配置项
    const option = {
      tooltip: { trigger: 'item' },
      legend: { bottom: 0, data: ['项目A 风险值'] },
      radar: {
        indicator: indicators,
        shape: 'polygon', // 多边形雷达
        splitArea: { areaStyle: { color: ['rgba(250,250,250,0.3)', 'rgba(200,200,200,0.1)'] } }
      },
      series: [
        {
          name: '综合风险评估',
          type: 'radar',
          data: [
            {
              value: scoreValues, // 把匹配好的分数 [67.2, 0, 0, 0, 45.0] 塞进图表！
              name: '项目A 风险值',
              areaStyle: { color: 'rgba(64, 158, 255, 0.4)' }, // 漂亮的科技蓝半透明填充
              lineStyle: { color: '#409EFF', width: 2 },
              itemStyle: { color: '#409EFF' }
            }
          ]
        }
      ]
    }

    // 6. 渲染图表！
    myChart.setOption(option)

    // 让图表随着浏览器窗口缩放自动调整大小
    window.addEventListener('resize', () => { myChart.resize() })

  } catch (error) {
    console.error("获取雷达图数据失败：", error)
  }
}

// Vue 生命周期：等网页的 DOM 元素一加载完毕，立刻去画图！
onMounted(() => {
  initRadarChart()
})
</script>

<style scoped>
.dashboard-container { margin-top: 10px; }
.card-header { font-weight: bold; font-size: 16px; color: #303133; }
</style>