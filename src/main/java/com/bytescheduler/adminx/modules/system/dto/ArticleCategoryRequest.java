package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Data
public class ArticleCategoryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
}
