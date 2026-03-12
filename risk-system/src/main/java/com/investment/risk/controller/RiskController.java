package com.investment.risk.controller;

import com.investment.risk.common.Result;
import com.investment.risk.entity.BusiRiskAssess;
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

    @GetMapping("/radar/{projectId}") // {projectId} 代表从网址路径里接收项目ID
    public Result<java.util.List<com.investment.risk.entity.vo.RiskRadarVO>> getRadarData(@PathVariable Long projectId) {
        java.util.List<com.investment.risk.entity.vo.RiskRadarVO> radarData = riskAssessService.getProjectRadarData(projectId);
        return Result.success(radarData);
    }
}