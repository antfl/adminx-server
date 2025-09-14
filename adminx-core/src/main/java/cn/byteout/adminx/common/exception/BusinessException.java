package cn.byteout.adminx.common.exception;

import lombok.Getter;

/**
 * 全局异常返回
 *
 * @author antfl
 * @since 2025/6/8
 */
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
