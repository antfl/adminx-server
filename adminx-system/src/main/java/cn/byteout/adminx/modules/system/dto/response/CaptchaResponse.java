package cn.byteout.adminx.modules.system.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaptchaResponse {
    private String captchaId;
    private String captchaImageBase64;
}
