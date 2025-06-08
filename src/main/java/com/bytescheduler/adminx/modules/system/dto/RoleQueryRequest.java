package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/8
 */
@Data
public class RoleQueryRequest {
    private String roleName;
    private String roleKey;
    private Integer status;
    private Integer page = 1;
    private Integer size = 10;
}
