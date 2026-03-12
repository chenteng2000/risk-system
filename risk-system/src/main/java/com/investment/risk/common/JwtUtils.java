package com.investment.risk.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtils {
    // 密钥，必须大于32个字符（相当于制造门禁卡的印钞机模板）
    private static final String SECRET_KEY_STR = "InvestmentRiskManagementSystemSecretKey2026";
    // 令牌过期时间：24小时
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    // 生成密钥对象
    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY_STR.getBytes(StandardCharsets.UTF_8));
    }

    // 生成 JWT 令牌
    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username) // 令牌里记录用户名
                .issuedAt(new Date()) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(getSecretKey()) // 使用密钥签名防伪造
                .compact();
            }
    // --- 请把下面这段代码加在 JwtUtils 类的最后面 ---

    // 验证并解析 JWT 令牌，提取出用户名
    public static String parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()) // 拿出我们的印钞机模板对比签名
                .build()
                .parseSignedClaims(token) // 如果篡改了、或者过期了，这里会自动报错拦截！
                .getPayload()
                .getSubject(); // 返回用户名
        }
}