package com.investment.risk.config;

import com.investment.risk.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**") // 拦截系统里所有的请求
                // 下面这些请求是白名单，不需要门禁卡直接放行：
                .excludePathPatterns(
                        "/user/login", // 登录接口放行
                        "/doc.html", "/webjars/**", "/v3/api-docs/**" // Knife4j接口文档的资源放行
                );
    }
}