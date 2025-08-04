package com.bytescheduler.adminx.common.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author byte-scheduler
 * @since 2025/8/3
 */
public class ResourceLoader {

    /**
     * 通用 Lua 脚本加载方法
     * @param path 脚本路径
     * @param resultType 返回类型（Long.class / Boolean.class）等
     * @return RedisScript 实例
     */
    public static <T> RedisScript<T> loadLuaScript(String path, Class<T> resultType) {
        String scriptContent = loadResourceContent(path);

        DefaultRedisScript<T> script = new DefaultRedisScript<>();
        script.setScriptText(scriptContent);
        script.setResultType(resultType);

        return script;
    }

    /**
     * HTML 内容加载方法
     * @param path HTML 文件路径
     * @return HTML 字符串
     */
    public static String loadHtml(String path) {
        return loadResourceContent(path);
    }

    /**
     * 资源加载
     */
    private static String loadResourceContent(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            throw new IllegalStateException("Resource file not found at: " + path);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Resource: " + path, e);
        }
    }
}