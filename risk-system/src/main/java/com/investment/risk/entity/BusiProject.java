package com.investment.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("busi_project")
public class BusiProject {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String projectName; // 项目名称
    private String projectCode; // 项目编号
    private String stage; // 投资阶段
    private BigDecimal investmentAmount; // 投资金额(万)
    private String description; // 项目简介(富文本)
    private Long managerId; // 负责人ID
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}