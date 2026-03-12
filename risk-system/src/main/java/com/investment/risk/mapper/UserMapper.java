package com.investment.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.investment.risk.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 告诉 Spring Boot 这是一个 MyBatis 的 Mapper 接口
public interface UserMapper extends BaseMapper<User> {
    // 继承了 BaseMapper 之后，MyBatis-Plus 就自动帮你写好了所有的增删改查方法！
    // 里面空空如也，却有无穷力量！
}