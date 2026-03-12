package com.investment.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("busi_risk_dict")
public class BusiRiskDict {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String category;
    private String riskName;
    private BigDecimal weight; // 权重
}