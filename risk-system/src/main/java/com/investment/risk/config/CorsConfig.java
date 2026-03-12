package com.investment.risk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许系统所有的接口跨域
                .allowedOriginPatterns("*") // 允许任何前端网址来访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方式
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
