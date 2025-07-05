package com.bytescheduler.adminx.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一结果返回
 *
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success() {
        return new Result<>(200, "成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    public static <T> Result<T> failed() {
        return new Result<>(500, "失败", null);
    }

    public static <T> Result<T> failed(String msg) {
        return new Result<>(500, msg, null);
    }
}