package com.bytescheduler.adminx.config;

import com.bytescheduler.adminx.common.utils.http.HttpRequestIpResolver;
import com.bytescheduler.adminx.web.interceptor.*;
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
    private final SignatureConfig signatureConfig;
    private final HttpRequestIpResolver ipResolver;

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
        // 复合限流拦截器
        registry.addInterceptor(
                new CompositeRateLimitInterceptor(redisTemplate, ipResolver, rateLimitConfig)
        ).addPathPatterns("/**");

        // 签名验证拦截器
        registry.addInterceptor(
                        new SignatureInterceptor(signatureConfig.getMaxTimeDiff(), redisTemplate)
                ).addPathPatterns("/**")
                .excludePathPatterns("/files/view/**");

        // 邮件发送限流
        registry.addInterceptor(createDailyRateLimitInterceptor(
                "email_limit:",
                3,
                "30"
        )).addPathPatterns("/auth/sendMailCode/**");

        // 验证码发送限流
        registry.addInterceptor(createDailyRateLimitInterceptor(
                "code_limit:",
                100,
                "32"
        )).addPathPatterns("/auth/captcha");
    }

    private BaseRateLimitInterceptor createDailyRateLimitInterceptor(
            String prefix,
            int maxRequests,
            String errorScore) {
        return new BaseRateLimitInterceptor(
                redisTemplate,
                ipResolver,
                prefix,
                maxRequests,
                24 * 3600,
                "scripts/fixed_window_rate_limiter.lua",
                "html/score.html",
                errorScore
        );
    }
}
