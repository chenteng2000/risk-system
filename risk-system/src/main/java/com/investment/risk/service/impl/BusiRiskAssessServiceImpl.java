package com.investment.risk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.investment.risk.entity.BusiRiskAssess;
import com.investment.risk.entity.BusiRiskDict;
import com.investment.risk.mapper.BusiRiskAssessMapper;
import com.investment.risk.mapper.BusiRiskDictMapper;
import com.investment.risk.service.BusiRiskAssessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BusiRiskAssessServiceImpl extends ServiceImpl<BusiRiskAssessMapper, BusiRiskAssess> implements BusiRiskAssessService {

    @Autowired
    private BusiRiskDictMapper dictMapper; // 需要引入指标库来查权重

    @Override
    public BusiRiskAssess evaluateRisk(BusiRiskAssess assess) {
        // 1. 去字典表查出这个风险指标的权重
        BusiRiskDict dict = dictMapper.selectById(assess.getRiskDictId());
        if (dict == null) {
            throw new RuntimeException("风险指标不存在！");
        }

        // 2. 核心算法开始： Score = P * I * Weight
        BigDecimal p = new BigDecimal(assess.getProbability());
        BigDecimal i = new BigDecimal(assess.getImpact());
        
        // baseScore = P * I
        BigDecimal baseScore = p.multiply(i);
        // 最终分值 = baseScore * 权重
        BigDecimal finalScore = baseScore.multiply(dict.getWeight());

        // 3. 将计算好的分值和日期塞进对象，并存入数据库
        assess.setScore(finalScore);
        assess.setAssessDate(LocalDate.now());
        this.save(assess); // 存进数据库

        // 返回带有计算结果的完整对象
        return assess;
    }

    @Override
    public java.util.List<com.investment.risk.entity.vo.RiskRadarVO> getProjectRadarData(Long projectId) {
        // 直接调用我们刚才在 Mapper 里写的超强定制 SQL
        return this.baseMapper.getRadarDataByProjectId(projectId);
    }
}