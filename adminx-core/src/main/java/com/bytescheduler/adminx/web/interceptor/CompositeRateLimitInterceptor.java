package com.bytescheduler.adminx.web.interceptor;

import com.bytescheduler.adminx.common.utils.http.HttpRequestIpResolver;
import com.bytescheduler.adminx.common.utils.loader.ResourceLoader;
import com.bytescheduler.adminx.config.RateLimitConfig;
import lombok.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author byte-scheduler
 * @since 2025/8/13
 */
public class CompositeRateLimitInterceptor implements HandlerInterceptor {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";
    private static final String BLACKLIST_KEY_PREFIX = "blacklist:";
    private static final RedisScript<Boolean> RATE_LIMIT_SCRIPT =
            ResourceLoader.loadLuaScript("scripts/fixed_window_rate_limiter.lua", Boolean.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final HttpRequestIpResolver ipResolver;
    private final RateLimitConfig rateLimitConfig;

    public CompositeRateLimitInterceptor(RedisTemplate<String, String> redisTemplate,
                                         HttpRequestIpResolver ipResolver,
                                         RateLimitConfig rateLimitConfig) {
        this.redisTemplate = redisTemplate;
        this.ipResolver = ipResolver;
        this.rateLimitConfig = rateLimitConfig;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String clientIp = ipResolver.resolve(request);
        String banKey = BLACKLIST_KEY_PREFIX + clientIp;

        if (redisTemplate.hasKey(banKey)) {
            sendErrorResponse(response, 1);
            return false;
        }

        String rateLimitKey = buildRateLimitKey(clientIp, request);
        Boolean allowed  = executeRateLimitScript(rateLimitKey);

        if (allowed  != null && !allowed) {
            redisTemplate.opsForValue().set(banKey, "banned",
                    rateLimitConfig.getBanSeconds(), TimeUnit.SECONDS);
            sendErrorResponse(response, 2);
            return false;
        }

        return true;
    }

    private String buildRateLimitKey(String clientIp, HttpServletRequest request) {
        return RATE_LIMIT_KEY_PREFIX + clientIp + ":" + request.getRequestURI();
    }

    private Boolean executeRateLimitScript(String key) {
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

    private void sendErrorResponse(HttpServletResponse response, int score) throws IOException {
        response.setStatus(429);
        response.setContentType("text/html;charset=UTF-8");
        String htmlResponse = ResourceLoader.loadHtml("html/score.html").replace("score", String.valueOf(score));
        response.getWriter().write(htmlResponse);
        response.getWriter().flush();
    }
}
