package cn.byteout.adminx.web.interceptor;

import cn.byteout.adminx.common.utils.http.HttpRequestIpResolver;
import cn.byteout.adminx.common.utils.loader.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import lombok.NonNull;

/**
 * 限流拦截器
 *
 * @author antfl
 * @since 2025/8/13
 */
public class BaseRateLimitInterceptor implements HandlerInterceptor {

    protected final RedisTemplate<String, String> redisTemplate;
    protected final HttpRequestIpResolver ipResolver;
    private final String keyPrefix;
    private final int maxRequests;
    private final long expireTime;
    private final RedisScript<Boolean> rateLimitScript;
    private final String errorHtmlPath;
    private final String errorScore;

    public BaseRateLimitInterceptor(RedisTemplate<String, String> redisTemplate,
                                    HttpRequestIpResolver ipResolver,
                                    String keyPrefix,
                                    int maxRequests,
                                    long expireTime,
                                    String scriptPath,
                                    String errorHtmlPath,
                                    String errorScore) {
        this.redisTemplate = redisTemplate;
        this.ipResolver = ipResolver;
        this.keyPrefix = keyPrefix;
        this.maxRequests = maxRequests;
        this.expireTime = expireTime;
        this.rateLimitScript = ResourceLoader.loadLuaScript(scriptPath, Boolean.class);
        this.errorHtmlPath = errorHtmlPath;
        this.errorScore = errorScore;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String clientIp = ipResolver.resolve(request);
        String redisKey = buildRedisKey(clientIp);

        if (!executeRateLimitScript(redisKey)) {
            sendLimitResponse(response);
            return false;
        }
        return true;
    }

    protected String buildRedisKey(String clientIp) {
        String dateKey = LocalDate.now(ZoneId.of("UTC")).format(DateTimeFormatter.BASIC_ISO_DATE);
        return keyPrefix + dateKey + ":" + clientIp;
    }

    private Boolean executeRateLimitScript(String key) {
        return redisTemplate.execute(
                rateLimitScript,
                Collections.singletonList(key),
                String.valueOf(maxRequests),
                String.valueOf(expireTime)
        );
    }

    protected void sendLimitResponse(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setContentType("text/html;charset=UTF-8");
        String htmlResponse = ResourceLoader.loadHtml(errorHtmlPath).replace("score", errorScore);
        response.getWriter().write(htmlResponse);
        response.getWriter().flush();
    }
}
