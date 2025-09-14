package cn.byteout.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author antfl
 * @since 2025/6/18
 */
@Data
@TableName("sys_dict_item")
@ApiModel(value = "字典项对象", description = "字典值表")
public class SysDictItem {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "字典ID", required = true)
    private Long dictId;

    @ApiModelProperty(value = "字典项标签", required = true)
    private String itemLabel;

    @ApiModelProperty(value = "字典项值", required = true)
    private String itemValue;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态（0-停用，1-正常）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty(value = "删除标志（0-未删除，1-已删除）")
    private Integer isDeleted;
}
