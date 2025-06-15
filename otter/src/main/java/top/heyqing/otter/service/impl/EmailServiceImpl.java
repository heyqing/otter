package top.heyqing.otter.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import top.heyqing.otter.common.exception.MessagingException;
import top.heyqing.otter.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * 邮件服务实现类
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;
    
    @Override
    public void sendVerificationEmail(String to, String token, String username) throws MessagingException {
        String subject = "验证您的邮箱 - Otter";
        String content = String.format("""
            <html>
            <body>
                <h2>您好, %s</h2>
                <p>请点击下面的链接验证您的邮箱:</p>
                <p><a href="%s/verify-email?token=%s">验证邮箱</a></p>
                <p>此链接24小时内有效。</p>
                <p>如果这不是您的操作,请忽略此邮件。</p>
            </body>
            </html>
            """, username, frontendUrl, token);
            
        sendHtmlEmail(to, subject, content);
    }
    
    @Override
    public void sendPasswordResetEmail(String to, String token, String username) throws MessagingException {
        String subject = "重置密码 - Otter";
        String content = String.format("""
            <html>
            <body>
                <h2>您好, %s</h2>
                <p>请点击下面的链接重置您的密码:</p>
                <p><a href="%s/reset-password?token=%s">重置密码</a></p>
                <p>此链接24小时内有效。</p>
                <p>如果这不是您的操作,请忽略此邮件。</p>
            </body>
            </html>
            """, username, frontendUrl, token);
            
        sendHtmlEmail(to, subject, content);
    }
    
    @Override
    public void sendHtmlEmail(String to, String subject, String content) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new MessagingException("发送邮件失败: " + e.getMessage(), e);
        }
    }
} 