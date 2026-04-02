package com.investment.risk.controller;

import com.investment.risk.common.Result;
import com.investment.risk.entity.BusiRiskAssess;
import com.investment.risk.entity.vo.RiskRankingVO;
import com.investment.risk.entity.vo.RiskLevelVO;
import com.investment.risk.service.BusiRiskAssessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk")
public class RiskController {

    @Autowired
    private BusiRiskAssessService riskAssessService;

    @PostMapping("/evaluate")
    public Result<BusiRiskAssess> doEvaluate(@RequestBody BusiRiskAssess assess) {
        // 此时前端只传了 P 和 I，我们调用算法算出 Score
        BusiRiskAssess result = riskAssessService.evaluateRisk(assess);
        return Result.success(result);
    }

    @GetMapping("/ranking")
    public Result<java.util.List<RiskRankingVO>> getRanking() {
        // 直接找 Service 要，不要去管底层的 Mapper
        return Result.success(riskAssessService.getTopRanking());
    }

    @GetMapping("/radar/{projectId}") // {projectId} 代表从网址路径里接收项目ID
    public Result<java.util.List<com.investment.risk.entity.vo.RiskRadarVO>> getRadarData(@PathVariable Long projectId) {
        java.util.List<com.investment.risk.entity.vo.RiskRadarVO> radarData = riskAssessService.getProjectRadarData(projectId);
        return Result.success(radarData);
    }

    @GetMapping("/distribution")
    public Result<java.util.List<RiskLevelVO>> getDistribution() {
        // 报错点修复：直接通过 service 调用
        return Result.success(riskAssessService.getRiskDistribution());
    }
}