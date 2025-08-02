package com.bytescheduler.adminx.modules.system.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author byte-scheduler
 * @since 2025/8/2
 */
@Data
public class ActiveUserResponse {

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "活跃量")
    private Integer  operationCount;
}
