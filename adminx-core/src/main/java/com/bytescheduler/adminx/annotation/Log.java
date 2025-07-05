package com.bytescheduler.adminx.annotation;

import com.bytescheduler.adminx.enums.OperationType;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * @return 操作模块
     */
    String module() default "";

    /**
     * @return 操作类型
     */
    OperationType type() default OperationType.OTHER;

    /**
     * @return 操作描述
     */
    String value() default "";
}
