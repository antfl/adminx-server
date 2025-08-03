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

    public static RedisScript<Long> loadLuaScript(String path) {
        ClassPathResource resource = new ClassPathResource(path);

        if (!resource.exists()) {
            throw new IllegalStateException("Resource file not found at: " + path);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            String scriptContent = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(scriptContent);
            script.setResultType(Long.class);

            return script;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Resource: " + path, e);
        }
    }

    public static String loadHtml(String path) {
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
