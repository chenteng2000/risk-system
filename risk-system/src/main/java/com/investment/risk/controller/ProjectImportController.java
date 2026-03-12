package com.investment.risk.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.investment.risk.common.Result;
import com.investment.risk.entity.BusiProject;
import com.investment.risk.service.BusiProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectImportController {

    @Autowired
    private BusiProjectService projectService;

    @PostMapping("/import")
    public Result<?> importExcel(MultipartFile file) throws Exception {
        // 1. 利用 Hutool 的 ExcelReader 快速读取
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        // 2. 把 Excel 的每一行转成我们的 BusiProject 对象列表
        List<BusiProject> list = reader.readAll(BusiProject.class);
        // 3. 批量存入数据库
        projectService.saveBatch(list);
        return Result.success("导入成功！共计 " + list.size() + " 条数据");
    }
}