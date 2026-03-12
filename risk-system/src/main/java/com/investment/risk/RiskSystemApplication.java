package com.investment.risk;

import org.mybatis.spring.annotation.MapperScan; // 1. 引入 MapperScan 包
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.investment.risk.mapper") // 2. 就是这行魔法代码！告诉 Spring Boot 去这个包下扫描 Mapper 接口
public class RiskSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskSystemApplication.class, args);
    }

}