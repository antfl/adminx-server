package com.bytescheduler.adminx.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 统一分页参数
 *
 * @author byte-scheduler
 * @since 2025/7/5
 */
@Data
public class PageParams {

    @ApiModelProperty(value = "当前页")
    @Min(1)
    private Integer current = 1;

    @ApiModelProperty(value = "每页条数")
    @Min(1)
    @Max(100)
    private Integer size = 10;
}
