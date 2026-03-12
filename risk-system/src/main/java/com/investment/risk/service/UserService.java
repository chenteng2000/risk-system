package com.investment.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.investment.risk.entity.User;
import com.investment.risk.entity.vo.LoginResponseVO;

public interface UserService extends IService<User> {
    // 定义一个登录方法，返回 Token 和真实姓名
    LoginResponseVO login(String username, String password);
    
    // 定义一个注册方法
    void register(String username, String password);
}