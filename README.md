# 投资项目风险管理系统 (Investment Risk Management System)

本项目为杭州电子科技大学信息工程学院《基于 Web 技术的投资项目风险管理模块》毕业设计成果。系统实现了从项目建档、风险量化评估到数据可视化的全流程管理。

## 🌟 核心功能
- **智能风险评估**：基于 $Score = P \times I \times W$ 风险矩阵模型，实现定性风险的定量转化。
- **数据驾驶舱**：集成 ECharts 实现多维雷达图、风险评分仪表盘、风险等级分布饼图及排行榜。
- **动态权限管理**：基于 JWT + RBAC 模型的安全访问控制与菜单动态渲染。
- **高效数据处理**：支持投资项目档案的 Excel 批量导入与导出。
- **合规审计日志**：自动记录系统内关键操作轨迹，确保金融业务可追溯。

## 🛠️ 技术栈
- **后端**: Spring Boot 3.3.0, MyBatis-Plus, Spring Security (BCrypt), JWT, MySQL 8.0, Hutool
- **前端**: Vue 3, Element Plus, ECharts, Axios, Vite

## 🚀 快速开始

### 1. 数据库准备
- 创建数据库 `investment_risk_db`。
- 运行项目根目录下 `/sql/init.sql` 脚本。

### 2. 后端启动
- 进入 `risk-system` 目录。
- 检查 `src/main/resources/application.yml` 中的数据库账号密码。
- 运行主程序 `RiskSystemApplication.java`。

### 3. 前端启动
- 进入 `risk-ui` 目录。
- 安装依赖：`npm install`
- 启动服务：`npm run dev`
- 浏览器访问：`http://localhost:5173` (默认账号：admin 密码：123456)

## 📊 业务演示建议
1. **项目体检**：在“投资项目档案”页点击“进行评估”，滑动滑块提交后观察雷达图变化。
2. **数据交互**：在“风险大屏”通过下拉框快速切换不同项目的风险看板。
3. **安全拦截**：尝试在无 Token 状态下访问后端接口，验证拦截器效果。
