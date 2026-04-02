package com.investment.risk.entity.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RiskRankingVO {
    private String projectName;   // 项目名称
    private BigDecimal maxScore;  // 最高分值
    private String riskCategory;  // 对应的风险维度
}