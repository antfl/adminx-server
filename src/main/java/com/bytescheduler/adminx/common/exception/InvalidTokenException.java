package com.bytescheduler.adminx.common.exception;

/**
 * 无效令牌异常
 *
 * @author byte-scheduler
 * @since 2025/6/15
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
