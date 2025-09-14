package cn.byteout.adminx.common.exception;

/**
 * Token 过期异常
 *
 * @author antfl
 * @since 2025/6/15
 */
public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message) {
        super(message);
    }
}
