package com.investment.risk.controller;

import com.investment.risk.common.Result;
import com.investment.risk.entity.SysMenu;
import com.investment.risk.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class MenuController {
    
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @GetMapping("/menu/list")
    public Result<List<SysMenu>> getMenuList() {
        // 查出数据库里配置的所有动态菜单下发给前端
        // (在真实的完整RBAC中，这里会根据当前登录用户的Token解析出角色，再查对应的菜单)
        List<SysMenu> menus = sysMenuMapper.selectList(null);
        return Result.success(menus);
    }
}