package com.bytescheduler.adminx.common.utils.validation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author byte-scheduler
 * @since 2025/8/3
 */
public class EmailValidator {

    // 常规邮箱域名白名单
    private static final Set<String> TRUSTED_EMAIL_DOMAINS = new HashSet<>(Arrays.asList(
            // 国际邮箱
            "gmail.com", "outlook.com", "hotmail.com",

            // 国内邮箱
            "qq.com", "163.com", "126.com", "sina.com", "sina.cn", "139.com",
            "aliyun.com", "foxmail.com", "189.cn"
    ));

    // 邮箱格式正则（RFC 5322 标准）
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    /**
     * 验证邮箱是否合法且域名在白名单中
     *
     * @param email 待验证的邮箱地址
     * @return true 表示验证通过，false 表示验证失败
     */
    public static boolean isTrustedEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // 验证基本格式
        if (!EMAIL_REGEX.matcher(email).matches()) {
            return false;
        }

        // 提取域名并转换为小写
        String domain = extractDomain(email).toLowerCase();
        return TRUSTED_EMAIL_DOMAINS.contains(domain);
    }

    /**
     * 从邮箱地址中提取域名部分
     *
     * @param email 完整的邮箱地址
     * @return 域名部分（如 "gmail.com"）
     */
    private static String extractDomain(String email) {
        int atIndex = email.lastIndexOf('@');
        if (atIndex < 0 || atIndex == email.length() - 1) {
            return "";
        }
        return email.substring(atIndex + 1);
    }
}
