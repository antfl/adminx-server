package com.bytescheduler.adminx.modules.system.dto.response;

import lombok.Data;

/**
 * GitHub 用户信息
 *
 * @author byte-scheduler
 * @since 2025/8/16
 */
@Data
public class GithubUserInfo {

    /**
     * 用户名
     */
    private String login;

    /**
     * 用户 ID
     */
    private String id;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 用户昵称
     */
    private String name;
}
