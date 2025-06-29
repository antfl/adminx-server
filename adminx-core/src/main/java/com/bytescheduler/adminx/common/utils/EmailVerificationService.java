package com.bytescheduler.adminx.common.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

/**
 * @author byte-scheduler
 * @since 2025/6/26
 */
public class EmailVerificationService {

    private static final String SMTP_HOST = "smtp.qq.com";
    private static final int SMTP_PORT = 587;
    // 发送者的 QQ 邮箱
    private static final String EMAIL_USER = "fl9420@qq.com";
    // QQ 邮箱授权码
    private static final String EMAIL_PASSWORD = "";

    /**
     * 生成随机验证码
     *
     * @return 6位数字验证码
     */
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 发送验证码邮件
     *
     * @param email 邮箱
     * @return 是否发送成功
     */
    public static boolean sendVerificationEmail(String email) {
        // 生成验证码
        String verificationCode = generateVerificationCode();

        // 配置邮件服务器
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // 创建会话
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USER, EMAIL_PASSWORD);
            }
        });

        try {
            // 创建邮件
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USER));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            // 设置邮件主题和内容
            message.setSubject("您的验证码");
            String content = "您的验证码是: <b>" + verificationCode + "</b><br>"
                    + "该验证码5分钟内有效，请勿泄露给他人。<br>"
                    + "如非本人操作，请忽略此邮件。";
            message.setContent(content, "text/html; charset=utf-8");

            // 发送邮件
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
