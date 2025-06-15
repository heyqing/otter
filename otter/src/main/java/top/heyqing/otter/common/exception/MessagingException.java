package top.heyqing.otter.common.exception;

/**
 * 邮件发送异常
 */
public class MessagingException extends RuntimeException {
    
    public MessagingException(String message) {
        super(message);
    }
    
    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }
} 