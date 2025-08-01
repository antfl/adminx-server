package com.bytescheduler.adminx.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 签名验证拦截器
 *
 * @author byte-scheduler
 * @since 2025/8/1
 */
public class SignatureInterceptor implements HandlerInterceptor {

    private final String apiSecret;
    private final long maxTimeDiff;
    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SignatureInterceptor(String apiSecret, long maxTimeDiff, RedisTemplate<String, String> redisTemplate) {
        this.apiSecret = apiSecret;
        this.maxTimeDiff = maxTimeDiff;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        String timestamp = request.getHeader("X-Timestamp");
        String nonce = request.getHeader("X-Nonce");
        String signature = request.getHeader("X-Signature");

        // 校验请求头
        if (timestamp == null || nonce == null || signature == null) {
            sendError(response, HttpStatus.BAD_REQUEST);
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long requestTime;
        try {
            requestTime = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            sendError(response, HttpStatus.BAD_REQUEST);
            return false;
        }

        // 请求有效期校验
        if (Math.abs(currentTime - requestTime) > maxTimeDiff) {
            sendError(response, HttpStatus.UNAUTHORIZED);
            return false;
        }

        // Nonce 唯一性校验
        String nonceKey = "nonce:" + nonce;
        if (redisTemplate.hasKey(nonceKey)) {
            sendError(response, HttpStatus.UNAUTHORIZED);
            return false;
        }
        // 存储 Nonce (5 分钟有效期)
        redisTemplate.opsForValue().set(nonceKey, "used", 5, TimeUnit.MINUTES);

        // 获取请求参数
        Map<String, String> urlParams = getUrlParams(request);
        Map<String, String> bodyParams = getBodyParams(request);

        // 生成服务端签名
        String serverSignature = generateSignature(urlParams, bodyParams, requestTime, nonce);

        // 签名比对
        if (!serverSignature.equals(signature)) {
            sendError(response, HttpStatus.UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private Map<String, String> getUrlParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String[] values = request.getParameterValues(name);
            if (values != null && values.length > 0) {
                params.put(name, normalizeValue(values));
            }
        }
        return params;
    }

    private Map<String, String> getBodyParams(HttpServletRequest request) {
        if (request instanceof CachedBodyHttpServletRequest) {
            CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request;
            byte[] body = cachedRequest.getCachedBody();

            if (body.length == 0) return Collections.emptyMap();

            try {
                Map<String, Object> rawMap  = objectMapper.readValue(body, Map.class);
                Map<String, String> result = new HashMap<>();
                for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                    result.put(entry.getKey(), normalizeValue(entry.getValue()));
                }
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
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(array[i] != null ? array[i].toString() : "");
            }
            return sb.toString();
        }
        if (value instanceof Collection) {
            Collection<?> coll = (Collection<?>) value;
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Object item : coll) {
                if (i++ > 0) sb.append(",");
                sb.append(item != null ? item.toString() : "");
            }
            return sb.toString();
        }
        return value.toString();
    }

    private String generateSignature(Map<String, String> urlParams, Map<String, String> bodyParams, long timestamp, String nonce) {
        // 合并参数
        Map<String, String> allParams = new HashMap<>();
        allParams.putAll(urlParams);
        allParams.putAll(bodyParams);

        // 排序参数
        List<String> sortedKeys = new ArrayList<>(allParams.keySet());
        Collections.sort(sortedKeys);

        // 构建参数字符串
        List<String> paramPairs = new ArrayList<>();
        for (String key : sortedKeys) {
            paramPairs.add(key + "=" + allParams.get(key));
        }
        String paramString = String.join("&", paramPairs);

        // 拼接基础数据
        List<String> parts = new ArrayList<>();
        if (!paramString.isEmpty()) parts.add(paramString);
        parts.add("timestamp=" + timestamp);
        parts.add("nonce=" + nonce);
        String rawData = String.join("&", parts);

        // 计算 HMAC-SHA256
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    apiSecret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            sha256.init(secretKey);
            byte[] bytes = sha256.doFinal(rawData.getBytes(StandardCharsets.UTF_8));

            // 转换为十六进制
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("签名生成失败", e);
        }
    }

    private void sendError(HttpServletResponse response, HttpStatus status) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(
                "{\"code\":" + status.value() +
                        ",\"message\":\"" + "非法请求" + "\"}"
        );
    }
}