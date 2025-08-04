package com.bytescheduler.adminx.common.utils;

import com.bytescheduler.adminx.common.exception.InvalidTokenException;
import com.bytescheduler.adminx.common.exception.TokenExpiredException;
import com.bytescheduler.adminx.repository.config.JwtConfig;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 令牌工具类，提供令牌的生成、解析和验证功能
 *
 * @author byte-scheduler
 * @since 2025/8/4
 */
@RequiredArgsConstructor
@Component
public class JwtTokenUtil {
    private final JwtConfig jwtConfig;

    // 用户 ID 字段声明
    private static final String USER_ID_CLAIM = "userId";

    public String generateToken(String username, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration() * 1000);

        return Jwts.builder()
                .claim(USER_ID_CLAIM, userId)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            // 令牌过期
            throw new TokenExpiredException("登录已过期，请重新登录");
        } catch (Exception ex) {
            // 无效令牌
            throw new InvalidTokenException("无效的登录凭证");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getExpirationFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.get(USER_ID_CLAIM).toString());
    }
}