-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `investment_risk_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `investment_risk_db`;

-- 2. 重建所有数据表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT '密码(BCrypt加密)',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(50) NOT NULL,
  `path` varchar(200) DEFAULT '',
  `component` varchar(255) DEFAULT NULL,
  `perms` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

DROP TABLE IF EXISTS `busi_project`;
CREATE TABLE `busi_project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_name` varchar(100) NOT NULL,
  `project_code` varchar(50) NOT NULL,
  `stage` varchar(50) DEFAULT NULL,
  `investment_amount` decimal(15,2) DEFAULT NULL,
  `description` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投资项目表';

DROP TABLE IF EXISTS `busi_risk_dict`;
CREATE TABLE `busi_risk_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL,
  `risk_name` varchar(100) NOT NULL,
  `weight` decimal(4,2) DEFAULT '1.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险指标库';

DROP TABLE IF EXISTS `busi_risk_assess`;
CREATE TABLE `busi_risk_assess` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_id` bigint NOT NULL,
  `risk_dict_id` bigint NOT NULL,
  `probability` int NOT NULL,
  `impact` int NOT NULL,
  `score` decimal(8,2) NOT NULL,
  `assess_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估记录表';

-- 3. 注入系统初始化数据
-- 管理员账号 (admin/123456)
INSERT INTO `sys_user` (username, password, real_name) VALUES ('admin', '$2a$10$7J4O.o1.H3o0.q4O4O4O4eO4O4O4O4O4O4O4O4O4O4O4O4O4O4O4O', '超级管理员');

-- 动态菜单
INSERT INTO `sys_menu` (menu_name, path, component, perms) VALUES 
('风险数据大屏', '/dashboard', 'Dashboard', 'DataLine'),
('投资项目档案', '/projects', 'ProjectList', 'Folder');

-- 风险指标库
INSERT INTO `busi_risk_dict` (id, category, risk_name, weight) VALUES 
(1, '市场风险', '市场竞争激烈程度', 1.20),
(2, '合规风险', '政策变化影响', 1.50),
(3, '信用风险', '借款方履约能力', 1.00),
(4, '操作风险', '技术实现难度', 1.00),
(5, '财务风险', '现金流稳定性', 1.30);