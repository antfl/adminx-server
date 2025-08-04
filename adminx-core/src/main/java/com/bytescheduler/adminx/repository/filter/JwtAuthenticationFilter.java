package com.bytescheduler.adminx.repository.filter;

import com.bytescheduler.adminx.common.exception.InvalidTokenException;
import com.bytescheduler.adminx.common.exception.TokenExpiredException;
import com.bytescheduler.adminx.common.utils.JwtTokenUtil;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.repository.config.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JWT 认证过滤器，用于处理基于令牌的用户认证。
 * 该过滤器会拦截请求，从请求头中提取 JWT 令牌并进行验证，包括：
 * 1. 令牌格式校验
 * 2. 令牌有效性验证（过期、篡改等）
 * 3. 与 Redis 存储的令牌比对（防止异地登录）
 * 验证通过后将用户认证信息存入安全上下文，并设置用户 ID 到线程本地存储。
 * 若验证失败则返回标准化的错误响应。
 *
 * @author byte-scheduler
 * @since 2025/8/4
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws IOException {
        try {
            String authToken = extractToken(request);
            if (authToken != null) {
                authenticateToken(request, response, authToken);
            }
            // 继续过滤器链（无论是否有令牌）
            chain.doFilter(request, response);
        } catch (Exception ex) {
            handleError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "服务器错误", "服务器内部错误");
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 从请求头中提取 JWT 令牌
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getTokenHeader());
        if (header != null && header.startsWith(jwtConfig.getTokenPrefix())) {
            return header.substring(jwtConfig.getTokenPrefix().length());
        }
        return null;
    }

    /**
     * 用户认证
     */
    private void authenticateToken(HttpServletRequest request,
                                   HttpServletResponse response,
                                   String authToken) throws IOException {
        try {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                // 无用户名或已认证则跳过
                return;
            }

            String redisToken = redisTemplate.opsForValue().get(username);
            if (redisToken == null) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "登录失效", "您的账号已在其他地方登录，请重新登录");
                return;
            }

            if (!redisToken.equals(authToken)) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "登录失效", "您的账号已在其他地方登录，请重新登录");
                return;
            }

            if (!jwtTokenUtil.validateToken(authToken)) {
                handleError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "无效凭证", "令牌验证失败");
                return;
            }

            setSecurityContext(request, username, authToken);
        } catch (TokenExpiredException ex) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "登录过期", "登录已过期，请重新登录");
        } catch (InvalidTokenException ex) {
            handleError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "无效凭证", "无效的登录凭证");
        }
    }

    /**
     * 设置安全上下文和用户上下文
     */
    private void setSecurityContext(HttpServletRequest request,
                                    String username,
                                    String authToken) {
        // 创建认证对象
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 设置安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 设置用户 ID 到线程本地
        Long userId = jwtTokenUtil.getUserIdFromToken(authToken);
        UserContext.setUserId(userId);
    }

    /**
     * 统一错误响应处理
     */
    private void handleError(HttpServletResponse response, int status,
                             String error, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", status);
        errorResponse.put("error", error);
        errorResponse.put("message", message);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(errorResponse));
            writer.flush();
        }
    }
}