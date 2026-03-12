package com.investment.risk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data // 这个注解就是 Lombok 提供的魔法，自动帮你写 get/set 方法！
@TableName("sys_user") // 告诉 MyBatis-Plus，这个类对应数据库里的 sys_user 表
public class User {
    @TableId(type = IdType.AUTO) // 告诉它这是主键，并且是自增的
    private Long id;
    private String username;
    private String password;
    private String realName;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}