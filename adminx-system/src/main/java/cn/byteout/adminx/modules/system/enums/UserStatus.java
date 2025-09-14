package cn.byteout.adminx.modules.system.enums;

import lombok.Getter;

/**
 * @author antfl
 * @since 2025/8/3
 */
@Getter
public enum UserStatus {

    NORMAL(0, "正常"),
    DISABLED(1, "停用");

    private final int code;
    private final String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
