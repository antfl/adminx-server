package com.bytescheduler.adminx.enums;

/**
 * @author byte-scheduler
 * @since 2025/6/8 22:18
 */
public enum OperationType {

    INSERT("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    SELECT("查询"),
    OTHER("其他");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
