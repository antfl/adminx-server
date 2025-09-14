package cn.byteout.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@TableName("sys_user")
@Accessors(chain = true)
public class SysUser {

    /**
     * 用户 ID
     */
    @Id
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户唯一标识
     */
    private String openId;

    /**
     * 用户部门 ID
     */
    private Long deptId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户秘密
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 状态（0-正常，1-停用）
     */
    private Integer status;

    /**
     * 最后登录 IP
     */
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer isDeleted;
}
