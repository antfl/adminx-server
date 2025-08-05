package com.bytescheduler.adminx.repository.config;

import com.bytescheduler.adminx.common.utils.HttpRequestIpResolver;
//import com.bytescheduler.adminx.security.DeviceFingerprintInterceptor;
import com.bytescheduler.adminx.security.CodeRateLimitInterceptor;
import com.bytescheduler.adminx.security.EmailRateLimitInterceptor;
import com.bytescheduler.adminx.security.RateLimitInterceptor;
import com.bytescheduler.adminx.security.SignatureInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitConfig rateLimitConfig;
    private final HttpRequestIpResolver ipResolver;
    private final SignatureConfig signatureConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // knife4j 资源映射
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // 添加上下文路径支持
        registry.addResourceHandler("/api/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 限流拦截器
        registry.addInterceptor(
                new RateLimitInterceptor(
                        redisTemplate,
                        ipResolver,
                        rateLimitConfig
                )
        ).addPathPatterns("/**");

        // 设备指纹拦截
//        registry.addInterceptor(
//                new DeviceFingerprintInterceptor(
//                        redisTemplate,
//                        ipResolver
//                )
//        ).addPathPatterns("/**");

        // 签名验证拦截器
        registry.addInterceptor(
                new SignatureInterceptor(
                        signatureConfig.getMaxTimeDiff(),
                        signatureConfig.getSecretKey(),
                        redisTemplate
                )
        ).addPathPatterns("/**").excludePathPatterns("/files/view/**");

        // 邮件发送拦截器
        registry.addInterceptor(
                new EmailRateLimitInterceptor(
                        redisTemplate,
                        ipResolver
                )
        ).addPathPatterns("/auth/sendMailCode/**");

        // 验证码发送拦截器
        registry.addInterceptor(
                new CodeRateLimitInterceptor(
                        redisTemplate,
                        ipResolver
                )
        ).addPathPatterns("/auth/captcha");
    }
}
