package com.bytescheduler.adminx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author byte-scheduler
 * @since 2025/7/15
 */
@Data
@Component
@ConfigurationProperties(prefix = "rate.limit")
public class RateLimitConfig {

    private int maxRequests;

    private long intervalSeconds;

    private long banSeconds;
}
