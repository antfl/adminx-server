package com.bytescheduler.adminx.annotation;

import com.bytescheduler.adminx.enums.OperationType;

import java.lang.annotation.*;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    // 操作模块
    String module() default "";

    // 操作类型
    OperationType type() default OperationType.OTHER;

    // 操作描述
    String value() default "";
}
