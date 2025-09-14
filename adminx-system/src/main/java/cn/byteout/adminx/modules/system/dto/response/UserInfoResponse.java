package cn.byteout.adminx.modules.system.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author antfl
 * @since 2025/6/23
 */
@Data
public class UserInfoResponse {

    @ApiModelProperty(value = "用户 ID")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "头像")
    private String avatar;
}
