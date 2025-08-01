package com.bytescheduler.adminx.repository.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author byte-scheduler
 * @since 2025/7/27
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    private String uploadDir;

    private int maxFileSize;

    private int expiration;
}
