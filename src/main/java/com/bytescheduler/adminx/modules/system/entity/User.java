package com.bytescheduler.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@TableName("sys_user")
public class User {
    @Id
    @TableId(type = IdType.AUTO)
    private Long userId;
    private Long deptId;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer status;
    private String lastLoginIp;
    private Date lastLoginTime;
    private Integer isDeleted;
    private Date createTime;
    private Date updateTime;
}
