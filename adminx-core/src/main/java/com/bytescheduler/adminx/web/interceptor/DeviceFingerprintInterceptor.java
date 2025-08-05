package com.bytescheduler.adminx.web.interceptor;

import com.bytescheduler.adminx.common.utils.http.HttpRequestIpResolver;
import com.bytescheduler.adminx.common.utils.json.JsonUtil;
import com.bytescheduler.adminx.common.utils.loader.ResourceLoader;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author byte-scheduler
 * @since 2025/8/2
 */
public class DeviceFingerprintInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, String> redisTemplate;
    private final HttpRequestIpResolver ipResolver;

    // 签名密钥
    private static final String SIGN_SECRET = "8D7F2A9C4E5B1F3A6C9D2E8F7B1A5C3D";

    // 黑名单键前缀
    private static final String BLACKLIST_PREFIX = "FP_BLACKLIST:";

    // 指纹数据键前缀
    private static final String FINGERPRINT_PREFIX = "FP_DATA:";

    // 防重放存储
    private static final String NONCE_PREFIX = "FP_NONCE:";

    // 异常计数键前缀
    private static final String SUSPICIOUS_PREFIX = "FP_SUSPICIOUS:";

    // 指纹有效期（分钟）
    private static final int FP_EXPIRE_MINUTES = 30;

    // IP 哈希盐值
    private static final String SALT = "7E3A5F8C2B1D9E4F";

    public DeviceFingerprintInterceptor(RedisTemplate<String, String> redisTemplate, HttpRequestIpResolver ipResolver) {
        this.redisTemplate = redisTemplate;
        this.ipResolver = ipResolver;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        // 获取客户端 IP
        String clientIp = ipResolver.resolve(request);

        // 从请求头获取设备指纹相关信息
        String headerStr = request.getHeader("Req-Device-Fingerprint");
        // 如果没有指纹信息，直接拦截
        if (headerStr == null || headerStr.isEmpty()) {
            handleBlocking(response, 5);
            return false;
        }

        String[] headerArr = headerStr.split("/");
        if (headerArr.length != 5) {
            handleBlocking(response, 0);
            return false;
        }

        try {
            Map<String, Object> fingerprint = new HashMap<>();
            fingerprint.put("visitorId", headerArr[0]);
            fingerprint.put("confidence", headerArr[1]);
            fingerprint.put("timestamp", headerArr[2]);
            fingerprint.put("nonce", headerArr[3]);
            fingerprint.put("signature", headerArr[4]);

            String visitorId = fingerprint.get("visitorId").toString();
            long timestamp = Long.parseLong(fingerprint.get("timestamp").toString());
            String nonce = fingerprint.get("nonce").toString();
            String signature = fingerprint.get("signature").toString();

            // 检查黑名单（根据 IP 和 visitorId 双重检查）
            if (isBlocked(clientIp) || isBlocked(visitorId)) {
                handleBlocking(response, 15);
                return false;
            }

            // 验证时间戳（防止重放攻击）
            if (Math.abs(System.currentTimeMillis() - timestamp) > 5 * 60 * 1000) {
                handleBlocking(response, 20);
                return false;
            }

            // 防重放攻击检查
            if (isNonceReused(visitorId, nonce)) {
                recordSuspiciousActivity(clientIp, visitorId, "REPLAY_ATTACK");
                handleBlocking(response, 25);
                return false;
            }

            // 签名验证
            if (!validateSignature(fingerprint, signature)) {
                recordSuspiciousActivity(clientIp, visitorId, "SIGNATURE_MISMATCH");
                handleBlocking(response, 30);
                return false;
            }

            // 获取 Redis 中存储的指纹记录
            String storedKey = FINGERPRINT_PREFIX + visitorId;
            String storedData = redisTemplate.opsForValue().get(storedKey);

            if (storedData != null) {
                // 验证 IP 一致性（防止会话劫持）
                Map<String, String> storedMap = JsonUtil.parseStringMap(storedData);
                String storedIpHash = storedMap.get("ipHash");
                String currentIpHash = DigestUtils.sha256Hex(clientIp + SALT);

                if (!currentIpHash.equals(storedIpHash)) {
                    recordSuspiciousActivity(clientIp, visitorId, "IP_CHANGE");
                    handleBlocking(response, 35);
                    return false;
                }

                storedMap.put("ip", clientIp);
                storedMap.put("ipHash", currentIpHash);
                redisTemplate.opsForValue().set(storedKey, JsonUtil.toJson(storedMap));
            } else {
                // 首次验证：创建指纹记录
                Map<String, String> newRecord = new IdentityHashMap<>();
                newRecord.put("ip", clientIp);
                newRecord.put("ipHash", DigestUtils.sha256Hex(clientIp + SALT));
                newRecord.put("firstSeen", String.valueOf(System.currentTimeMillis()));

                redisTemplate.opsForValue().set(
                        storedKey,
                        JsonUtil.toJson(newRecord),
                        FP_EXPIRE_MINUTES,
                        TimeUnit.MINUTES
                );
            }

            // 更新最后访问时间
            redisTemplate.expire(storedKey, FP_EXPIRE_MINUTES, TimeUnit.MINUTES);

            redisTemplate.opsForValue().set(
                    NONCE_PREFIX + visitorId + ":" + nonce,
                    "1",
                    5, TimeUnit.MINUTES
            );

            // 将指纹信息存入请求属性，供后续使用
            request.setAttribute("DEVICE_FINGERPRINT", fingerprint);

            return true;

        } catch (Exception ex) {
            handleBlocking(response, 40);
            return false;
        }
    }

    /**
     * 防重放攻击检查
     */
    private boolean isNonceReused(String visitorId, String nonce) {
        String nonceKey = NONCE_PREFIX + visitorId + ":" + nonce;
        return redisTemplate.hasKey(nonceKey);
    }

    /**
     * 验证数字签名
     */
    private boolean validateSignature(Map<String, Object> data, String receivedSignature) {
        try {
            // 按固定顺序拼接关键字段
            String payload = data.get("visitorId") + "|" + data.get("timestamp") + "|" + data.get("nonce");

            // 使用 HmacSHA256 生成签名
            String computedSignature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, SIGN_SECRET).hmacHex(payload);

            // 安全比较防止时序攻击
            return MessageDigest.isEqual(
                    computedSignature.getBytes(StandardCharsets.UTF_8),
                    receivedSignature.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否在黑名单中
     */
    private boolean isBlocked(String identifier) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + identifier);
    }

    /**
     * 记录可疑活动
     */
    private void recordSuspiciousActivity(String ip, String visitorId, String reason) {
        // 使用模糊键名
        String ipKey = SUSPICIOUS_PREFIX + "IP:" + DigestUtils.md5Hex(ip + SALT);
        String visitorKey = SUSPICIOUS_PREFIX + "VID:" + visitorId;

        // 分级计数
        Long ipCount = redisTemplate.opsForValue().increment(ipKey, 1);
        Long visitorCount = redisTemplate.opsForValue().increment(visitorKey, 1);

        redisTemplate.expire(ipKey, 1, TimeUnit.HOURS);
        redisTemplate.expire(visitorKey, 1, TimeUnit.HOURS);

        // 分级封锁
        if (ipCount != null && ipCount >= 5) {
            // 1天或30天
            int blockHours = ipCount < 10 ? 24 : 720;
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + DigestUtils.md5Hex(ip + SALT),
                    reason + "|" + System.currentTimeMillis(),
                    blockHours, TimeUnit.HOURS
            );
        }

        if (visitorCount != null && visitorCount >= 3) {
            // 1天或30天
            int blockHours = visitorCount < 5 ? 24 : 720;
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + visitorId,
                    reason + "|" + System.currentTimeMillis(),
                    blockHours, TimeUnit.HOURS
            );
        }
    }

    /**
     * 处理拦截响应
     */
    private void handleBlocking(HttpServletResponse response, int score) throws IOException {

        response.setStatus(500);
        response.setContentType("text/html;charset=UTF-8");

        String htmlResponse = ResourceLoader.loadHtml("html/device-fingerprint.html").replace("score", String.valueOf(score));

        response.getWriter().write(htmlResponse);
        response.getWriter().flush();
    }
}
