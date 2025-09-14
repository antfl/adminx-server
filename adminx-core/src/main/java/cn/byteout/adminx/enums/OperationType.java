package cn.byteout.adminx.enums;

import lombok.Getter;

/**
 * @author antfl
 * @since 2025/6/8 22:18
 */
@Getter
public enum OperationType {

    INSERT("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    SELECT("查询"),
    OTHER("其他"),
    USER_LOGIN("用户登录"),
    USER_REGISTER("用户注册");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }
}
