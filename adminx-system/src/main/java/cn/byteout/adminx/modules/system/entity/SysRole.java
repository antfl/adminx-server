package cn.byteout.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author antfl
 * @since 2025/6/8
 */
@Data
@TableName("sys_role")
public class SysRole {
    @ApiModelProperty(value = "角色 ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty(value = "角色 KEY")
    @TableField("role_key")
    private String roleKey;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "数据范围")
    @TableField("data_scope")
    private Integer dataScope;

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
    @TableField("is_deleted")
    private Integer isDeleted;
}
