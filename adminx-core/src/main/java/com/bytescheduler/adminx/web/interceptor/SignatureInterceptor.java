package com.bytescheduler.adminx.web.interceptor;

import com.bytescheduler.adminx.common.utils.loader.ResourceLoader;
import com.bytescheduler.adminx.web.wrapper.CachedBodyHttpServletRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 签名验证拦截器
 *
 * @author byte-scheduler
 * @since 2025/8/1
 */
@RequiredArgsConstructor
public class SignatureInterceptor implements HandlerInterceptor {

    private static final String HMAC_SHA256 = HmacAlgorithms.HMAC_SHA_256.getName();
    private static final String NONCE_PREFIX = "nonce:";
    private static final int NONCE_TTL_MINUTES = 5;
    private static final String SIGNATURE_HEADER = "Req-Signature";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final long maxTimeDiff;
    private final String apiSecret;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        String headerStr = request.getHeader(SIGNATURE_HEADER);
        if (headerStr == null || headerStr.isEmpty()) {
            sendError(response, 0);
            return false;
        }

        String[] headerArr = headerStr.split("/");
        if (headerArr.length != 3) {
            sendError(response, 0);
            return false;
        }

        String nonce = headerArr[0];
        String timestamp = headerArr[1];
        String signature = headerArr[2];

        // 校验请求头
        if (nonce.isEmpty() || timestamp.isEmpty() || signature.isEmpty()) {
            sendError(response, 1);
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long requestTime;
        try {
            requestTime = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            sendError(response, 5);
            return false;
        }

        // 请求有效期校验
        if (Math.abs(currentTime - requestTime) > maxTimeDiff) {
            sendError(response, 10);
            return false;
        }

        // Nonce 唯一性校验
        String nonceKey = NONCE_PREFIX + nonce;
        if (redisTemplate.hasKey(nonceKey)) {
            sendError(response, 20);
            return false;
        }
        // 存储 Nonce
        redisTemplate.opsForValue().set(nonceKey, "used", NONCE_TTL_MINUTES, TimeUnit.MINUTES);

        // 获取请求参数
        Map<String, String> urlParams = getUrlParams(request);
        Map<String, String> bodyParams = getBodyParams(request);

        // 生成服务端签名
        String serverSignature = generateSignature(urlParams, bodyParams, requestTime, nonce);

        // 安全比较签名（防止时序攻击）
        if (!MessageDigest.isEqual(signature.getBytes(StandardCharsets.UTF_8), serverSignature.getBytes(StandardCharsets.UTF_8))) {
            sendError(response, 25);
            return false;
        }

        return true;
    }

    private Map<String, String> getUrlParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((name, values) -> {
            if (values != null && values.length > 0) {
                params.put(name, normalizeValue(values));
            }
        });
        return params;
    }

    private Map<String, String> getBodyParams(HttpServletRequest request) {
        if (request instanceof CachedBodyHttpServletRequest) {
            CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request;
            byte[] body = cachedRequest.getCachedBody();

            if (body.length == 0) return Collections.emptyMap();

            try {
                Map<String, Object> rawMap = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
                });
                Map<String, String> result = new HashMap<>();
                rawMap.forEach((key, value) -> result.put(key, normalizeValue(value)));
                return result;
            } catch (IOException e) {
                return Collections.emptyMap();
            }
        }
        return Collections.emptyMap();
    }

    private String normalizeValue(Object value) {
        if (value == null) return "";
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            return Arrays.stream(array)
                    .map(obj -> obj != null ? obj.toString() : "")
                    .collect(Collectors.joining(","));
        }
        if (value instanceof Collection) {
            Collection<?> coll = (Collection<?>) value;
            return coll.stream()
                    .map(obj -> obj != null ? obj.toString() : "")
                    .collect(Collectors.joining(","));
        }
        return value.toString();
    }

    private String generateSignature(Map<String, String> urlParams, Map<String, String> bodyParams, long timestamp, String nonce) {
        // 合并参数
        Map<String, String> allParams = new HashMap<>(urlParams);
        allParams.putAll(bodyParams);

        // 排序参数
        List<String> sortedKeys = new ArrayList<>(allParams.keySet());
        Collections.sort(sortedKeys);

        // 构建参数字符串
        String paramString = sortedKeys.stream()
                .map(key -> key + "=" + allParams.get(key))
                .collect(Collectors.joining("&"));

        // 拼接基础数据
        List<String> parts = new ArrayList<>();
        if (!paramString.isEmpty()) parts.add(paramString);
        parts.add("timestamp=" + timestamp);
        parts.add("nonce=" + nonce);
        String rawData = String.join("&", parts);

        // 生成十六进制签名
        return new HmacUtils(HMAC_SHA256, apiSecret.getBytes(StandardCharsets.UTF_8)).hmacHex(rawData.getBytes(StandardCharsets.UTF_8));
    }

    private void sendError(HttpServletResponse response, int score) throws IOException {
        response.setStatus(500);
        response.setContentType("text/html;charset=UTF-8");
        String htmlResponse = ResourceLoader.loadHtml("html/signature.html").replace("score", String.valueOf(score));
        response.getWriter().write(htmlResponse);
        response.getWriter().flush();
    }
}