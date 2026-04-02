package com.investment.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.investment.risk.entity.BusiRiskAssess;
import com.investment.risk.entity.vo.RiskRadarVO;
import com.investment.risk.entity.vo.RiskRankingVO;
import com.investment.risk.entity.vo.RiskLevelVO;
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

    // 自定义 SQL：查询风险评分Top5项目
    @Select("SELECT p.project_name, MAX(a.score) as max_score, d.category as risk_category " +
            "FROM busi_risk_assess a " +
            "JOIN busi_project p ON a.project_id = p.id " +
            "JOIN busi_risk_dict d ON a.risk_dict_id = d.id " +
            "GROUP BY a.project_id, p.project_name, d.category " +
            "ORDER BY max_score DESC LIMIT 5")
    List<RiskRankingVO> getTopRiskProjects();

    // 自定义 SQL：风险等级分布统计
    @Select("SELECT " +
            "  CASE WHEN avg_score > 80 THEN '高风险' " +
            "       WHEN avg_score > 50 THEN '中风险' " +
            "       ELSE '低风险' END as name, " +
            "  COUNT(*) as value " +
            "FROM (SELECT AVG(score) as avg_score FROM busi_risk_assess GROUP BY project_id) t " +
            "GROUP BY name")
    List<RiskLevelVO> getRiskLevelDistribution();
}