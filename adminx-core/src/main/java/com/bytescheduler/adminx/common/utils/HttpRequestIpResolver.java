package com.bytescheduler.adminx.common.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 获取 IP
 *
 * @author byte-scheduler
 * @since 2025/8/2
 */
@Component
public class HttpRequestIpResolver {

    // 可信代理头
    private static final List<String> HEADERS = Collections.unmodifiableList(Arrays.asList(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    ));

    /**
     * 获取 IP
     */
    public String resolve(HttpServletRequest request) {
        String ip;
        for (String header : HEADERS) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                return parseFirstValidIp(ip);
            }
        }

        // 如果无代理情况，直接获取远端地址
        ip = request.getRemoteAddr();
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    private String parseFirstValidIp(String ip) {
        if (ip.contains(",")) {
            return Arrays.stream(ip.split(","))
                    .map(String::trim)
                    .filter(this::isValidIp)
                    .findFirst()
                    .orElse(null);
        }
        return ip;
    }
}
