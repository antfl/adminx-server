package com.bytescheduler.adminx.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author byte-scheduler
 * @since 2025/7/22
 */
@Data
public class OperationLogResponse {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "模块")
    private String module;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "耗时")
    private Long duration;
}
