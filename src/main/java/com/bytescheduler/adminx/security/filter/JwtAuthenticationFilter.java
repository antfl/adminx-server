package com.bytescheduler.adminx.security.filter;

import com.bytescheduler.adminx.common.exception.InvalidTokenException;
import com.bytescheduler.adminx.common.exception.TokenExpiredException;
import com.bytescheduler.adminx.common.utils.JwtTokenUtil;
import com.bytescheduler.adminx.common.utils.UserContext;
import com.bytescheduler.adminx.security.config.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
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


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, JwtTokenUtil jwtTokenUtil, RedisTemplate<String, String> redisTemplate) {
        this.jwtConfig = jwtConfig;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws IOException {
        try {
            // 从请求头获取令牌
            String header = request.getHeader(jwtConfig.getTokenHeader());

            // 检查令牌是否存在且格式正确
            if (header != null && header.startsWith(jwtConfig.getTokenPrefix())) {
                // 提取实际的令牌
                String authToken = header.substring(jwtConfig.getTokenPrefix().length());
                try {
                    // 从令牌中获取用户名（可能抛出 TokenExpiredException 或 InvalidTokenException）
                    String username = jwtTokenUtil.getUsernameFromToken(authToken);

                    // 验证用户上下文是否已设置
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 从 Redis 获取存储的令牌
                        String redisToken = redisTemplate.opsForValue().get(username);

                        // 验证 Redis令牌与请求令牌是否匹配
                        if (redisToken != null && redisToken.equals(authToken)) {
                            // 验证令牌有效性
                            if (jwtTokenUtil.validateToken(authToken)) {
                                // 创建认证对象并设置到安全上下文
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(username, null, null);
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);

                                Long userId = jwtTokenUtil.getUserIdFromToken(authToken);
                                UserContext.setUserId(userId);
                            } else {
                                // 令牌无效
                                handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "无效凭证", "令牌验证失败");
                                return;
                            }
                        } else {
                            // Redis中找不到令牌或令牌不匹配
                            handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "登录失效", "您的账号已在其他地方登录，请重新登录");
                            return;
                        }
                    }
                } catch (TokenExpiredException ex) {
                    // 令牌过期处理
                    handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "登录过期", "登录已过期，请重新登录");
                    return;
                } catch (InvalidTokenException ex) {
                    // 无效令牌处理
                    handleError(response, HttpServletResponse.SC_UNAUTHORIZED, "无效凭证", "无效的登录凭证");
                    return;
                }
            }

            // 继续过滤器链
            chain.doFilter(request, response);

        } catch (Exception ex) {
            // 全局异常处理
            handleError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "服务器错误", "服务器错误");
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 统一错误处理方法
     */
    private void handleError(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 错误响应
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", status);
        errorResponse.put("error", error);
        errorResponse.put("message", message);

        // 写入响应
        try (PrintWriter writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(errorResponse));
            writer.flush();
        }
    }
}
