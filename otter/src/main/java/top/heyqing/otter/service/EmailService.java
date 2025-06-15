package top.heyqing.otter.service;

import top.heyqing.otter.common.exception.MessagingException;

/**
 * 邮件服务接口
 */
public interface EmailService {
    
    /**
     * 发送验证邮件
     *
     * @param to 收件人邮箱
     * @param token 验证令牌
     * @param username 用户名
     * @throws MessagingException 发送邮件异常
     */
    void sendVerificationEmail(String to, String token, String username) throws MessagingException;
    
    /**
     * 发送密码重置邮件
     *
     * @param to 收件人邮箱
     * @param token 重置令牌
     * @param username 用户名
     * @throws MessagingException 发送邮件异常
     */
    void sendPasswordResetEmail(String to, String token, String username) throws MessagingException;
    
    /**
     * 发送HTML格式邮件
     *
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content HTML内容
     * @throws MessagingException 发送邮件异常
     */
    void sendHtmlEmail(String to, String subject, String content) throws MessagingException;
}
