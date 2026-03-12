package com.investment.risk.controller;

import com.investment.risk.common.Result; // 引入我们刚才写的 Result
import com.investment.risk.entity.User;
import com.investment.risk.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test/users")
    public Result<List<User>> getAllUsers() { // 注意这里的返回值类型变了
        List<User> users = userMapper.selectList(null);
        // 用 Result.success() 包装一下查询结果
        return Result.success(users);
    }
}