package com.bytescheduler.adminx.modules.system.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户绑定三方账号的信息
 *
 * @author byte-scheduler
 * @since 2025/8/17
 */
@Builder
@Data
public class BindingInfo {

    /**
     * 绑定记录 ID
     */
    private Long id;

    /**
     * 三方平台标识
     */
    private String provider;

    /**
     * 三方账号昵称
     */
    private String nickname;

    /**
     * 三方账号头像
     */
    private String avatar;

    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bindTime;
}
