package com.bytescheduler.adminx.common.utils;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public class UserContext {
    private static final ThreadLocal<Long> userHolder = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userHolder.set(userId);
    }

    public static Long getCurrentUserId() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
}
