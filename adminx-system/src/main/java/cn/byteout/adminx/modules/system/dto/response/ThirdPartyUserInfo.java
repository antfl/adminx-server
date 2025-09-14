package cn.byteout.adminx.modules.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author antfl
 * @since 2025/8/21
 */
@Data
@AllArgsConstructor
public class ThirdPartyUserInfo {

    private String openId;

    private String avatarUrl;

    private String nickname;
}
