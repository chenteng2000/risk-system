package com.investment.risk.controller;

import com.investment.risk.common.Result;
import com.investment.risk.entity.BusiProject;
import com.investment.risk.service.BusiProjectService;
import com.investment.risk.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private BusiProjectService projectService;

    @Autowired
    private SysLogService sysLogService; // 注入日志服务

    // 1. 新增投资项目 API
    @PostMapping("/add")
    public Result<String> addProject(@RequestBody BusiProject project) {
        // save() 是 MyBatis-Plus 自带的超级方法，直接把对象存入数据库！
        boolean success = projectService.save(project);
        if (success) {
            return Result.success("项目档案创建成功！");
        } else {
            return Result.error("项目档案创建失败！");
        }
    }

    // 2. 查询所有投资项目 API
    @GetMapping("/list")
    public Result<List<BusiProject>> listProjects() {
        // list() 也是自带的方法，查出表里所有数据
        List<BusiProject> list = projectService.list();
        return Result.success(list);
    }

    // 3. 删除投资项目 API
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteProject(@PathVariable Long id) {
        boolean success = projectService.removeById(id);
        if (success) {
            // --- 核心埋点逻辑 ---
            com.investment.risk.entity.SysLog log = new com.investment.risk.entity.SysLog();
            log.setUsername("管理员"); // 这里的用户名可以后期根据 Token 动态获取
            log.setOperation("删除投资项目");
            log.setMethod("ProjectController.deleteProject");
            log.setParams("ID: " + id);
            sysLogService.save(log);
            // ------------------
            return Result.success("项目档案已成功删除！");
        }
        return Result.error("删除失败");
    }
}