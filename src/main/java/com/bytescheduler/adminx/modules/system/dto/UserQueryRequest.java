package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/17
 */
@Data
public class UserQueryRequest {
    private Integer status;
    private Integer page = 1;
    private Integer size = 10;
}
