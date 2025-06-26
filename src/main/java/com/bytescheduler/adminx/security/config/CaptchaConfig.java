package com.bytescheduler.adminx.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaConfig {
    private int width;
    private int height;
    private int codeCount;
    private int interferenceCount;
    private int expireSeconds;

}
