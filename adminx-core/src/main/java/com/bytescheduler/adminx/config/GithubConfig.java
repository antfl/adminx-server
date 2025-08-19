package com.bytescheduler.adminx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author byte-scheduler
 * @since 2025/8/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "github")
public class GithubConfig {

    /**
     * GitHub APP ID
     */
    private String clientId;

    /**
     * APP 密钥
     */
    private String clientSecret;

    /**
     * 前端回调地址
     */
    private String redirectUri;

    /**
     * GitHub 授权地址
     */
    private String authorizationUri;

    /**
     * GitHub 获取 TOKEN 地址
     */
    private String tokenUri;

    /**
     * GitHub 获取用户信息地址
     */
    private String userInfoUri;
}
