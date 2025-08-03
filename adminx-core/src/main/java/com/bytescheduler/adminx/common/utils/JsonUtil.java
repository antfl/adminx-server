package com.bytescheduler.adminx.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collections;
import java.util.Map;

/**
 * @author byte-scheduler
 * @since 2025/8/2
 */
public class JsonUtil {
    private static final ObjectMapper objectMapper = createObjectMapper();

    // 创建并配置 ObjectMapper 实例
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * 将 JSON 字符串解析为 Map<String, Object>
     *
     * @param json JSON 字符串
     * @return 解析后的 Map，解析失败返回空 Map
     */
    public static Map<String, Object> parseMap(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 将 JSON 字符串解析为 Map<String, String>
     *
     * @param json JSON 字符串
     * @return 解析后的 String Map，解析失败返回空 Map
     */
    public static Map<String, String> parseStringMap(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 将对象序列化为 JSON 字符串
     *
     * @param obj 要序列化的对象
     * @return JSON 字符串，序列化失败返回空字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "";
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    /**
     * 安全解析 Long 值（防止类型转换异常）
     *
     * @param map 数据 Map
     * @param key 键名
     * @return 解析后的 Long 值，解析失败返回 0L
     */
    public static Long parseLongSafely(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0L;

        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 解析 String 值
     *
     * @param map 数据 Map
     * @param key 键名
     * @return 字符串值，不存在返回空字符串
     */
    public static String parseStringSafely(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return "";
        return value.toString();
    }
}
