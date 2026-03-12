package com.investment.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.investment.risk.entity.User;

public interface UserService extends IService<User> {
    // 定义一个登录方法，返回生成的 Token 字符串
    String login(String username, String password);
}