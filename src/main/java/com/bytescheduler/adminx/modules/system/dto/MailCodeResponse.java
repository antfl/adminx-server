package com.bytescheduler.adminx.modules.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailCodeResponse {
    private String captchaId;
}
