package com.bytescheduler.adminx.repository.config;

import com.bytescheduler.adminx.security.RateLimitInterceptor;
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
        registry.addInterceptor(
                new RateLimitInterceptor(
                        redisTemplate,
                        rateLimitConfig.getMaxRequests(),
                        rateLimitConfig.getIntervalSeconds(),
                        rateLimitConfig.getBanSeconds()
                )
        ).addPathPatterns("/**");
    }
}
