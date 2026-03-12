package com.investment.risk.interceptor;

import com.investment.risk.common.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // --- 加上这三行：放行所有的 OPTIONS 探路请求 ---
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        // ---------------------------------------------

        // 1. 从前端发来的请求头(Header)里，找一个叫 "Authorization" 的头，这就是放门禁卡的地方
        String token = request.getHeader("Authorization");

        // 2. 如果没带令牌，直接拦住！抛出异常！
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("无Token，请重新登录");
        }

        // 3. 如果带了令牌，交给验钞机查验真伪
        try {
            JwtUtils.parseToken(token); // 如果这步没报错，说明令牌是真的且没过期
            return true; // 保安放行！
        } catch (Exception e) {
            throw new RuntimeException("Token无效或已过期，请重新登录"); // 假令牌或过期令牌，拦住！
        }
    }
}