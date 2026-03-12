package com.investment.risk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.investment.risk.common.JwtUtils;
import com.investment.risk.entity.User;
import com.investment.risk.mapper.UserMapper;
import com.investment.risk.service.UserService;
import org.springframework.stereotype.Service;

@Service // 告诉 Spring Boot 这是一个业务类，赶紧把它管理起来
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(String username, String password) {
        // 1. 去数据库里根据用户名查询有没有这个用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);

        // 2. 判断用户存不存在
        if (user == null) {
            throw new RuntimeException("用户不存在！");
        }
        // 3. 判断密码对不对 (为了不卡进度，这里先明文对比，后续引入Spring Security时再换成密文对比)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误！");
        }
        // 4. 账号密码全对，发“门禁卡”（生成 JWT 令牌）！
        return JwtUtils.generateToken(username);
    }
}