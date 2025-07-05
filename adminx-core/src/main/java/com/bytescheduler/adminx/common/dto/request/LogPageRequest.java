package com.bytescheduler.adminx.common.dto.request;

import com.bytescheduler.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author byte-scheduler
 * @since 2025/7/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogPageRequest extends PageParams {

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作类型")
    private String module;
}
