package com.investment.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("busi_risk_assess")
public class BusiRiskAssess {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId; // 关联你刚才建的项目ID
    private Long riskDictId; // 关联指标库ID
    private Integer probability; // 可能性 (P)
    private Integer impact; // 影响程度 (I)
    private BigDecimal score; // 算出来的综合分值！
    private LocalDate assessDate; // 评估日期
}