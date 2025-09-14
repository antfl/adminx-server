package cn.byteout.adminx.common.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author antfl
 * @since 2025/7/22
 */
@Data
public class DailyStatResponse {

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "总操作量")
    private Integer total;

    @ApiModelProperty(value = "成功操作量")
    private Integer success;

    @ApiModelProperty(value = "失败操作量")
    private Integer failure;
}
