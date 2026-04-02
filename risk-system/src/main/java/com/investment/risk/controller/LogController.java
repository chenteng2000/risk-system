package com.investment.risk.controller;

import com.investment.risk.common.Result;
import com.investment.risk.entity.SysLog;
import com.investment.risk.service.SysLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/list")
    public Result<List<SysLog>> listLogs() {
        // 按时间倒序排列，最新的操作在最上面
        return Result.success(sysLogService.list(new QueryWrapper<SysLog>().orderByDesc("create_time")));
    }
}
