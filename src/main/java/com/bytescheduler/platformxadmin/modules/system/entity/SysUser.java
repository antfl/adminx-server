package com.bytescheduler.platformxadmin.modules.system.entity;

import lombok.Data;
import java.util.Date;

@Data
public class SysUser {
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
