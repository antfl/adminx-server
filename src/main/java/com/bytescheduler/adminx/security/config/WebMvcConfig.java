package com.bytescheduler.adminx.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author byte-scheduler
 * @since 2025/6/7
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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
}
