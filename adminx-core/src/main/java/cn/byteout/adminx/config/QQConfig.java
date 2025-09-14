package cn.byteout.adminx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author antfl
 * @since 2025/8/11
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfig {

    /**
     * APP ID
     */
    private String appId;

    /**
     * APP KEY
     */
    private String appKey;

    /**
     * 重定向前端地址
     */
    private String redirectUri;

    /**
     * 获取 ACCESS TOKEN 接口地址
     */
    private String accessTokenUrl;

    /**
     * 获取 OPENID 接口地址
     */
    private String openidUrl;

    /**
     * 获取用户信息接口地址
     */
    private String userInfoUrl;
}
