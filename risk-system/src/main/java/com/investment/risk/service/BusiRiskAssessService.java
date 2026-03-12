package com.investment.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.investment.risk.entity.BusiRiskAssess;

public interface BusiRiskAssessService extends IService<BusiRiskAssess> {
    // 定义我们自己的核心算法方法
    BusiRiskAssess evaluateRisk(BusiRiskAssess assess);

    // 新增一个方法：获取雷达图数据
    java.util.List<com.investment.risk.entity.vo.RiskRadarVO> getProjectRadarData(Long projectId);
}