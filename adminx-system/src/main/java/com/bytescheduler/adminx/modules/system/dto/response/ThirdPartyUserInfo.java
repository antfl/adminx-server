package com.bytescheduler.adminx.modules.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author byte-scheduler
 * @since 2025/8/21
 */
@Data
@AllArgsConstructor
public class ThirdPartyUserInfo {

    private String openId;

    private String avatarUrl;

    private String nickname;
}
