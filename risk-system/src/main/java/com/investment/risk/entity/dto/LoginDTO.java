package com.investment.risk.entity.dto;

import lombok.Data;

@Data // 自动生成 Get/Set 方法
public class LoginDTO {
    private String username;
    private String password;
}