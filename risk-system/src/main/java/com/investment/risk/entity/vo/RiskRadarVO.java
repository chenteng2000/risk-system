package com.investment.risk.entity.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RiskRadarVO {
    private String category; // 风险维度（比如：市场风险）
    private BigDecimal score; // 该维度的综合风险总分（比如：67.2）
}