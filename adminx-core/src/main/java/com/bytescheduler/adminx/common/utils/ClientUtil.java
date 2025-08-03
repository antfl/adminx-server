package com.bytescheduler.adminx.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author byte-scheduler
 * @since 2025/8/2
 */
@Component
public class ClientUtil {

    // 可信代理头
    private static final List<String> HEADERS = Collections.unmodifiableList(Arrays.asList(
            "X-Real-IP",
            "CF-Connecting-IP",
            "Fastly-Client-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    ));

    private static final Set<String> TRUSTED_PROXIES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "192.168.0.0/16",
            "10.0.0.0/8",
            "172.16.0.0/12",
            "127.0.0.1",
            "::1"
    )));

    // 私有 IP 正则
    private static final Pattern PRIVATE_IP_PATTERN = Pattern.compile(
            "^(127\\.|192\\.168\\.|10\\.|172\\.(1[6-9]|2[0-9]|3[0-1])\\.|::1|[fF][cCdD])"
    );

    public String getClientRealIp(HttpServletRequest request) {
        // 检查可信代理头
        for (String header : HEADERS) {
            String headerValue = request.getHeader(header);
            if (StringUtils.isNotBlank(headerValue)) {
                String ip = parseFirstValidIp(headerValue);
                if (isValidPublicIp(ip)) {
                    // 找到有效公网 IP 立即返回
                    return ip;
                }
            }
        }

        // 使用 remoteAddr 作为备选
        String remoteAddr = normalizeIp(request.getRemoteAddr());

        // 验证 IP 是否可信
        return isTrustedProxy(remoteAddr) ? remoteAddr : null;
    }

    /**
     * 从头部值解析第一个有效 IP
     */
    private String parseFirstValidIp(String headerValue) {
        String[] ips = headerValue.split("\\s*,\\s*");
        for (String ip : ips) {
            String normalized = normalizeIp(ip);
            if (isValidIpFormat(normalized)) {
                return normalized;
            }
        }
        return null;
    }

    /**
     * 标准化 IP 格式
     */
    private String normalizeIp(String ip) {
        if (ip == null) return null;

        // 处理 IPv6 环回地址
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equalsIgnoreCase(ip)) {
            return "127.0.0.1";
        }

        // 处理 IPv4 映射的 IPv6 地址
        if (ip.toLowerCase().startsWith("::ffff:")) {
            return ip.substring(7);
        }

        return ip;
    }

    /**
     * 验证是否为有效公网 IP
     */
    private boolean isValidPublicIp(String ip) {
        return ip != null &&
                isValidIpFormat(ip) &&
                !isPrivateIp(ip);
    }

    /**
     * 检查是否为私有 IP
     */
    private boolean isPrivateIp(String ip) {
        return PRIVATE_IP_PATTERN.matcher(ip).find();
    }

    /**
     * 基础 IP 格式验证
     */
    private boolean isValidIpFormat(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            return inetAddress != null;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 检查 IP 是否来自可信代理
     */
    private boolean isTrustedProxy(String ip) {
        if (ip == null) return false;

        for (String cidr : TRUSTED_PROXIES) {
            if (isInCidrRange(ip, cidr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * CIDR 范围匹配（支持 IPv4 / IPv6）
     */
    private boolean isInCidrRange(String ip, String cidr) {
        try {
            String[] parts = cidr.split("/");
            String cidrIp = parts[0];
            int prefixLength = parts.length > 1 ? Integer.parseInt(parts[1]) : -1;

            // 特殊处理单个 IP
            if (prefixLength < 0) {
                return ip.equals(cidrIp);
            }

            InetAddress ipAddr = InetAddress.getByName(ip);
            InetAddress cidrAddr = InetAddress.getByName(cidrIp);

            // 类型检查
            if (ipAddr.getClass() != cidrAddr.getClass()) {
                return false;
            }

            byte[] ipBytes = ipAddr.getAddress();
            byte[] cidrBytes = cidrAddr.getAddress();
            int fullBytes = prefixLength / 8;
            int remainderBits = prefixLength % 8;

            // 检查完整字节
            for (int i = 0; i < fullBytes; i++) {
                if (ipBytes[i] != cidrBytes[i]) {
                    return false;
                }
            }

            // 检查剩余位
            if (remainderBits > 0) {
                byte mask = (byte) (0xFF << (8 - remainderBits));
                return (ipBytes[fullBytes] & mask) == (cidrBytes[fullBytes] & mask);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
