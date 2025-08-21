package com.bytescheduler.adminx.modules.system.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * GitHub 用户信息
 *
 * @author byte-scheduler
 * @since 2025/8/16
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserInfo {

    /**
     * 用户名
     */
    @JsonProperty("login")
    private String login;

    /**
     * 用户 ID
     */
    @JsonProperty("id")
    private String id;

    /**
     * 头像
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;

    /**
     * 用户昵称
     */
    @JsonProperty("name")
    private String name;
}
