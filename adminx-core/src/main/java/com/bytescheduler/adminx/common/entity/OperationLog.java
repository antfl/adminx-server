package com.bytescheduler.adminx.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Data
@Entity
@TableName("operation_log")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String operator;

    @Column(nullable = false, length = 50)
    private String module;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "operation_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationTime;

    private Long duration;

    @Column(length = 50)
    private String ip;

    @Lob
    private String params;

    @Lob
    private String result;

    @Column(nullable = false)
    private Integer status = 1;

    @Lob
    @Column(name = "error_msg")
    private String errorMsg;
}
