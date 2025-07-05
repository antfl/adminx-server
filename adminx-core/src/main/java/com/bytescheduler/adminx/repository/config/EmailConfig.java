package com.bytescheduler.adminx.repository.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfig {
    /**
     * true 开启邮箱验证，false 关闭邮箱验证
     */
    private boolean verificationEnabled;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件模板
     */
    private String contentTemplate;

    /**
     * 验证码位数
     */
    private int codeCount;

    /**
     * 过期时间 (秒)
     */
    private int expireSeconds;

    /**
     * 是否忽略大小写 (默认不忽略)
     */
    private boolean ignoreCase;

    /**
     * 验证码 (key) 的头
     */
    private String keyHead;

    /**
     * 邮件标题
     */
    private String teamName;
}
