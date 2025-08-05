package com.bytescheduler.adminx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author byte-scheduler
 * @since 2025/8/1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "signature")
public class SignatureConfig {

    private String secretKey;

    private long maxTimeDiff;
}
