package com.bytescheduler.adminx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * jwt 密钥
     */
    private String secret;

    /**
     * 过期时间（秒）
     */
    private Long expiration;

    /**
     * Token Header
     */
    private String tokenHeader;

    /**
     * Token Prefix
     */
    private String tokenPrefix;
}
