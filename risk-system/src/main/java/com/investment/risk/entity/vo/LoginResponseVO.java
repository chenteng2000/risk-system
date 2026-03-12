package com.investment.risk.entity.vo;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponseVO {
    private String token;
    private String realName;
}
