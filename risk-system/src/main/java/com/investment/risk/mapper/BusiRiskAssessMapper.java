package com.investment.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.investment.risk.entity.BusiRiskAssess;
import com.investment.risk.entity.vo.RiskRadarVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface BusiRiskAssessMapper extends BaseMapper<BusiRiskAssess> {
    
    // 自定义 SQL：关联两张表，查出某个项目在各个维度的风险总分
    @Select("SELECT d.category, SUM(a.score) as score " +
            "FROM busi_risk_assess a " +
            "LEFT JOIN busi_risk_dict d ON a.risk_dict_id = d.id " +
            "WHERE a.project_id = #{projectId} " +
            "GROUP BY d.category")
    List<RiskRadarVO> getRadarDataByProjectId(@Param("projectId") Long projectId);
}