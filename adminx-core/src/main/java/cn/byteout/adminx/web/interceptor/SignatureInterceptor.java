package cn.byteout.adminx.web.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import cn.byteout.adminx.common.utils.loader.ResourceLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 签名验证拦截器
 *
 * @author antfl
 * @since 2025/8/1
 */
@RequiredArgsConstructor
public class SignatureInterceptor implements HandlerInterceptor {
    private static final String NONCE_PREFIX = "nonce:";
    private static final int NONCE_TTL_MINUTES = 5;
    private static final String SIGNATURE_HEADER = "Req-Signature";

    private final long maxTimeDiff;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String headerStr = request.getHeader(SIGNATURE_HEADER);
        if (StringUtils.isEmpty(headerStr)) {
            sendError(response, 0);
            return false;
        }

        String[] headerArr = headerStr.split("/", 3);
        if (headerArr.length != 3) {
            sendError(response, 5);
            return false;
        }

        String nonce = headerArr[0];
        String timestamp = headerArr[1];
        String signature = headerArr[2];

        if (StringUtils.isEmpty(nonce) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(signature)) {
            sendError(response, 10);
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long requestTime;
        try {
            requestTime = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            sendError(response, 11);
            return false;
        }

        if (Math.abs(currentTime - requestTime) > maxTimeDiff) {
            sendError(response, 13);
            return false;
        }

        String nonceKey = NONCE_PREFIX + nonce;
        if (redisTemplate.hasKey(nonceKey)) {
            sendError(response, 20);
            return false;
        }

        redisTemplate.opsForValue().set(nonceKey, "used", NONCE_TTL_MINUTES, TimeUnit.MINUTES);
        return true;
    }

    private void sendError(HttpServletResponse response, int score) throws IOException {
        response.setStatus(403);
        response.setContentType("text/html;charset=UTF-8");
        String htmlResponse = ResourceLoader.loadHtml("html/score.html").replace("score", String.valueOf(score));
        response.getWriter().write(htmlResponse);
        response.getWriter().flush();
    }
}