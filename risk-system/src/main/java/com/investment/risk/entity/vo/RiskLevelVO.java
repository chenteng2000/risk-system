package com.investment.risk.entity.vo;

import lombok.Data;

@Data
public class RiskLevelVO {
    private String name;  // 风险等级：高、中、低
    private Integer value; // 项目数量
}
