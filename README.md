# 投资项目风险管理系统 (Investment Risk Management System)

本系统是基于 Web 技术构建的投资项目风险评估与可视化平台，旨在解决中小投资机构在风险管理中的定性评估难题。

## 1. 技术栈
* **后端**: Spring Boot 3.3.0, MyBatis-Plus, JWT, MySQL 8.0
* **前端**: Vue 3, Element Plus, ECharts, Axios

## 2. 环境部署指南
### 数据库初始化
1. 请在 MySQL 中创建数据库 `investment_risk_db`。
2. 执行仓库根目录下的 `sql/init.sql` 脚本（记得先把 SQL 文件放进去）。

### 后端运行
1. 进入 `risk-system` 目录。
2. 修改 `src/main/resources/application.yml` 中的数据库配置（修改为您的本地用户名和密码）。
3. 运行 `RiskSystemApplication.java`。

### 前端运行
1. 进入 `risk-ui` 目录。
2. 执行 `npm install`。
3. 执行 `npm run dev`。
4. 访问 `http://localhost:5173`。

## 3. 功能亮点
* **风险量化评估模型 (P×I×W)**：支持定性转定量。
* **数据可视化 (ECharts)**：多维风险雷达图直观展现。
* **企业级权限安全**：基于 JWT 的无状态安全拦截。
