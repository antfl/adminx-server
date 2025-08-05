package com.bytescheduler.adminx.context;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
public class UserContextHolder {
    private static final ThreadLocal<Long> context = new ThreadLocal<>();

    public static void set(Long userId) {
        context.set(userId);
    }

    public static Long get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
