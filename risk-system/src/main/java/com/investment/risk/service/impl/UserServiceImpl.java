package com.investment.risk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.investment.risk.common.JwtUtils;
import com.investment.risk.entity.User;
import com.investment.risk.entity.vo.LoginResponseVO;
import com.investment.risk.mapper.UserMapper;
import com.investment.risk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // 告诉 Spring Boot 这是一个业务类，赶紧把它管理起来
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseVO login(String username, String password) {
        // 1. 去数据库里根据用户名查询有没有这个用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);

        // 2. 判断用户存不存在
        if (user == null) {
            throw new RuntimeException("用户不存在！");
        }
        // 3. 判断密码对不对 (使用 BCrypt 进行密码比对)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误！");
        }
        // 4. 账号密码全对，发“门禁卡”（生成 JWT 令牌）！
        String token = JwtUtils.generateToken(username);
        // 5. 返回 Token 和真实姓名
        return new LoginResponseVO(token, user.getRealName());
    }
    @Override
    public void register(String username, String password) {
        // 1. 检查用户是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User existingUser = this.getOne(queryWrapper);
        
        if (existingUser != null) {
            throw new RuntimeException("用户已存在！");
        }
        
        // 2. 创建新用户对象
        User newUser = new User();
        newUser.setUsername(username);
        // 使用 BCrypt 加密密码
        newUser.setPassword(passwordEncoder.encode(password));
        // 设置默认真实姓名为用户名
        newUser.setRealName(username);
        
        // 3. 保存到数据库
        this.save(newUser);
    }}