package com.investment.risk.controller;

import com.investment.risk.common.Result;
import com.investment.risk.entity.dto.LoginDTO;
import com.investment.risk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user") // 这个类里所有接口的前缀都是 /user
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login") // 这是一个 POST 请求，完整地址是 /user/login
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            // 调用业务层去登录，拿到令牌
            String token = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            // 把令牌包装在漂亮的 Result 里返回给前端
            return Result.success(token);
        } catch (Exception e) {
            // 如果报错（比如密码错误），就返回错误信息
            return Result.error(e.getMessage());
        }
    }
}