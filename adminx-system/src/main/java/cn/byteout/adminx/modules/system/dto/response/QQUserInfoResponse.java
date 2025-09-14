package cn.byteout.adminx.modules.system.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * QQ 用户信息实体类
 *
 * @author antfl
 * @since 2025/8/11
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QQUserInfoResponse {

    /**
     * 用户唯一标识
     */
    private String openId;

    /**
     * 用户昵称
     */
    @JsonProperty("nickname")
    private String nickname;

    /**
     * 性别（男/女）
     */
    @JsonProperty("gender")
    private String gender;

    /**
     * QQ 头像
     */
    @JsonProperty("figureurl_qq")
    private String qqAvatar;
}
