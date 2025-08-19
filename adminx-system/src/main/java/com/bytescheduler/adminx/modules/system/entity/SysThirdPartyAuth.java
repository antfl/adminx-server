package com.bytescheduler.adminx.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户三方登录绑定关系表
 *
 * @author byte-scheduler
 * @since 2025/8/17
 */
@Data
@TableName("sys_third_party_auth")
public class SysThirdPartyAuth {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统用户 ID（未绑定时为空）
     */
    private Long userId;

    /**
     * 三方平台标识 (wechat/github/qq)
     */
    private String provider;

    /**
     * 三方平台用户唯一 ID
     */
    private String openId;

    /**
     * 跨平台统一 ID (微信生态专用)
     */
    private String unionId;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 三方账号头像 URL
     */
    private String avatarUrl;

    /**
     * 三方账号昵称
     */
    private String nickname;

    /**
     * 令牌过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;
}
