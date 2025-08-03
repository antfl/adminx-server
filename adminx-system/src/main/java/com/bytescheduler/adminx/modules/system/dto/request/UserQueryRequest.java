package com.bytescheduler.adminx.modules.system.dto.request;

import com.bytescheduler.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author byte-scheduler
 * @since 2025/6/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageParams {

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;
}
