package cn.byteout.adminx.modules.system.dto.request;

import cn.byteout.adminx.common.entity.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author antfl
 * @since 2025/7/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictItemPageRequest extends PageParams {

    @ApiModelProperty(value = "字典 ID", required = true)
    private Long dictId;

    @ApiModelProperty(value = "字典项标签")
    private String itemLabel;

    @ApiModelProperty(value = "字典项值")
    private String itemValue;
}
