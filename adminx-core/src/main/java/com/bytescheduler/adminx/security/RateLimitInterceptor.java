package com.bytescheduler.adminx.security;

import com.bytescheduler.adminx.common.utils.ClientUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
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
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";
    private static final String BLACKLIST_KEY_PREFIX = "blacklist:";
    private static final String TOO_MANY_REQUESTS_MSG = "请求过于频繁，请稍后再试";

    private final RedisTemplate<String, String> redisTemplate;
    private final ClientUtil clientUtil;
    private final int maxRequests;
    private final long intervalSeconds;
    private final long banTimeSeconds;

    // Lua 脚本
    private static final DefaultRedisScript<Long> RATE_LIMIT_SCRIPT = new DefaultRedisScript<>(
            "local key = KEYS[1]\n" +
                    "local max = tonumber(ARGV[1])\n" +
                    "local window = tonumber(ARGV[2])\n" +
                    "local banTime = tonumber(ARGV[3])\n" +
                    "\n" +
                    "local current = redis.call('GET', key)\n" +
                    "if current and tonumber(current) > max then\n" +
                    "    return -1 \n" +
                    "end\n" +
                    "\n" +
                    "local count = redis.call('INCR', key)\n" +
                    "if count == 1 then\n" +
                    "    redis.call('EXPIRE', key, window)\n" +
                    "end\n" +
                    "\n" +
                    "if count > max then\n" +
                    "    return -1\n" +
                    "end\n" +
                    "return count",
            Long.class
    );

    public RateLimitInterceptor(
            RedisTemplate<String, String> redisTemplate,
            ClientUtil clientUtil,
            int maxRequests,
            long intervalSeconds,
            long banTimeSeconds
    ) {
        this.redisTemplate = redisTemplate;
        this.clientUtil = clientUtil;
        this.maxRequests = maxRequests;
        this.intervalSeconds = intervalSeconds;
        this.banTimeSeconds = banTimeSeconds;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String clientIp = clientUtil.getClientRealIp(request);
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
            redisTemplate.opsForValue().set(banKey, "banned", banTimeSeconds, TimeUnit.SECONDS);
            sendErrorResponse(response);
            return false;
        }

        return true;
    }

    private String buildRateLimitKey(String clientIp, HttpServletRequest request) {
        return RATE_LIMIT_KEY_PREFIX + clientIp + ":" + request.getRequestURI();
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.sendError(429, RateLimitInterceptor.TOO_MANY_REQUESTS_MSG);
    }

    private Long executeRateLimitScript(String key) {
        try {
            return redisTemplate.execute(
                    RATE_LIMIT_SCRIPT,
                    Collections.singletonList(key),
                    String.valueOf(maxRequests),
                    String.valueOf(intervalSeconds),
                    String.valueOf(banTimeSeconds)
            );
        } catch (Exception e) {
            return null;
        }
    }
}
