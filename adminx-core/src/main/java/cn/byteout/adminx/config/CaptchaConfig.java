package cn.byteout.adminx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaConfig {
    /**
     * 验证码 (key) 的头
     */
    private String keyHead;

    /**
     * 图片验证码宽
     */
    private int width;

    /**
     * 图片验证码高
     */
    private int height;

    /**
     * 验证码位数
     */
    private int codeCount;

    /**
     * 影响线，越高越复杂
     */
    private int interferenceCount;

    /**
     * 过期时间 (秒)
     */
    private int expireSeconds;

    /**
     * 是否忽略大小写
     */
    private boolean ignoreCase;
}
