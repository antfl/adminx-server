package com.bytescheduler.adminx.web.interceptor;

import com.bytescheduler.adminx.common.utils.http.HttpRequestIpResolver;
import com.bytescheduler.adminx.common.utils.loader.ResourceLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * 验证码发送拦截器
 *
 * @author byte-scheduler
 * @since 2025/8/3
 */
@Component
@RequiredArgsConstructor
public class CodeRateLimitInterceptor implements HandlerInterceptor {

    private static final String EMAIL_RATE_LIMIT_KEY_PREFIX = "code_limit:";
    private static final int MAX_DAILY_EMAILS = 20;
    private static final long KEY_EXPIRE_HOURS = 24;
    private static final RedisScript<Boolean> EMAIL_LIMIT_SCRIPT = ResourceLoader.loadLuaScript("scripts/email_limit.lua", Boolean.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final HttpRequestIpResolver ipResolver;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String clientIp = ipResolver.resolve(request);
        String dateKey = getCurrentDateKey();
        String redisKey = EMAIL_RATE_LIMIT_KEY_PREFIX + dateKey + ":" + clientIp;

        // 使用 Lua 脚本保证原子操作
        Boolean allowed = executeRateLimitScript(redisKey);

        if (!allowed) {
            // 当脚本执行失败或达到限制时
            sendLimitExceededResponse(response);
            return false;
        }

        return true;
    }

    private Boolean executeRateLimitScript(String key) {
        return redisTemplate.execute(
                EMAIL_LIMIT_SCRIPT,
                Collections.singletonList(key),
                String.valueOf(MAX_DAILY_EMAILS),
                String.valueOf(KEY_EXPIRE_HOURS * 3600)
        );
    }

    private String getCurrentDateKey() {
        return LocalDate.now(ZoneId.of("UTC")).format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    private void sendLimitExceededResponse(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setContentType("text/html;charset=UTF-8");
        String htmlResponse = ResourceLoader.loadHtml("html/signature.html").replace("score", "59.99");
        response.getWriter().write(htmlResponse);
        response.getWriter().flush();
    }
}
