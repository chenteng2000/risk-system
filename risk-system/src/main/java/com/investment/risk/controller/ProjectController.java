package com.investment.risk.controller;

import com.investment.risk.common.Result;
import com.investment.risk.entity.BusiProject;
import com.investment.risk.service.BusiProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private BusiProjectService projectService;

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
}