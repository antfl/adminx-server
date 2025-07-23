package com.bytescheduler.adminx.common.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/7/22
 */
@Data
public class ModuleStatResponse {

    @ApiModelProperty(value = "模块名称")
    private String name;

    @ApiModelProperty(value = "操作次数")
    private Long value;
}
