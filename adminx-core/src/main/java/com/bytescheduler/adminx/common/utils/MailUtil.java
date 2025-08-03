package com.bytescheduler.adminx.common.utils;

import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.repository.config.EmailConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class MailUtil {
    private final JavaMailSender mailSender;
    private final EmailConfig emailConfig;
    private static final SecureRandom random = new SecureRandom();

    /**
     * 读取 spring.mail.username
     */
    @Value("${spring.mail.username}")
    private String from;


    public MailUtil(JavaMailSender mailSender, EmailConfig emailConfig) {
        this.mailSender = mailSender;
        this.emailConfig = emailConfig;
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 收件人邮箱
     * @param code  验证码
     */
    public void sendVerifyCode(String email, String code) {
        if (!EmailValidator.isTrustedEmail(email)) {
            throw new BusinessException("请使用常规邮箱");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(String.format(emailConfig.getTeamName(), from));
        message.setTo(email);
        message.setSubject(emailConfig.getSubject());
        message.setText(String.format(emailConfig.getContentTemplate(), code));
        mailSender.send(message);
    }

    /**
     * 生成验证码
     */
    public String generateComplexCode(int length) {
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}
