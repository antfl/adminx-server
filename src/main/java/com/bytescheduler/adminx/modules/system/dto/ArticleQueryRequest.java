package com.bytescheduler.adminx.modules.system.dto;

import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/6/21
 */
@Data
public class ArticleQueryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String title;
    private Long categoryId;
    private Long userId;
    private Integer auditStatus;
}
