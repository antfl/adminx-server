package com.bytescheduler.adminx.web.interceptor;

import com.bytescheduler.adminx.common.utils.http.HttpRequestIpResolver;
import com.bytescheduler.adminx.common.utils.loader.ResourceLoader;
import com.bytescheduler.adminx.config.RateLimitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 限流拦截器
 *
 * @author byte-scheduler
 * @since 2025/7/15
 */
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";
    private static final String BLACKLIST_KEY_PREFIX = "blacklist:";
    private static final RedisScript<Long> RATE_LIMIT_SCRIPT = ResourceLoader.loadLuaScript("scripts/rate_limit.lua", Long.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final HttpRequestIpResolver ipResolver;
    private final RateLimitConfig rateLimitConfig;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String clientIp = ipResolver.resolve(request);
        String banKey = BLACKLIST_KEY_PREFIX + clientIp;

        // 检查全局黑名单
        if (redisTemplate.hasKey(banKey)) {
            sendErrorResponse(response);
            return false;
        }

        // 执行限流检查
        String rateLimitKey = buildRateLimitKey(clientIp, request);
        Long result = executeRateLimitScript(rateLimitKey);

        // 处理限流结果
        if (result != null && result == -1L) {
            redisTemplate.opsForValue().set(banKey, "banned", rateLimitConfig.getBanSeconds(), TimeUnit.SECONDS);
            sendErrorResponse(response);
            return false;
        }

        return true;
    }

    private String buildRateLimitKey(String clientIp, HttpServletRequest request) {
        return RATE_LIMIT_KEY_PREFIX + clientIp + ":" + request.getRequestURI();
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType("text/html;charset=UTF-8");

        String htmlResponse = ResourceLoader.loadHtml("html/rate_limit.html").replace("score", "10");

        response.getWriter().write(htmlResponse);
        response.getWriter().flush();
    }

    private Long executeRateLimitScript(String key) {
        try {
            return redisTemplate.execute(
                    RATE_LIMIT_SCRIPT,
                    Collections.singletonList(key),
                    String.valueOf(rateLimitConfig.getMaxRequests()),
                    String.valueOf(rateLimitConfig.getIntervalSeconds()),
                    String.valueOf(rateLimitConfig.getBanSeconds())
            );
        } catch (Exception e) {
            return null;
        }
    }
}
