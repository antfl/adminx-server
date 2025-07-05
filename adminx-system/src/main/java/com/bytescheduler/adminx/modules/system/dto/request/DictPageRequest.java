package com.bytescheduler.adminx.modules.system.dto.request;

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
public class DictPageRequest extends PageParams {

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典编码")
    private String dictCode;
}
