package com.bytescheduler.adminx.repository.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfig {
    private boolean verificationEnabled;
    private String from;
    private String subject;
    private String contentTemplate;
    private int codeCount;
    private int expireSeconds;
    private boolean ignoreCase;
    private String keyHead;
    private String teamName;
}
