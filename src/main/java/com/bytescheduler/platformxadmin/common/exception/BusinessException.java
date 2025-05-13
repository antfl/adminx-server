package com.bytescheduler.platformxadmin.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        // 默认错误码
        this.code = 500;
    }

    // 带错误码的构造方法
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
