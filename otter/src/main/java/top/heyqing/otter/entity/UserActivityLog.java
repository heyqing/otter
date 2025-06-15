package top.heyqing.otter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;

/**
 * 用户操作日志实体
 */
@Data
@Entity
@Table(name = "user_activity_logs")
@EqualsAndHashCode(callSuper = true)
public class UserActivityLog extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * 操作类型
     */
    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserAction action;
    
    /**
     * IP地址
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    /**
     * 用户代理（浏览器信息）
     */
    @Column(name = "user_agent")
    private String userAgent;
    
    /**
     * 详细操作信息（JSON格式）
     */
    @Column(name = "details", columnDefinition = "jsonb")
    private String details;
    
    /**
     * 用户操作类型枚举
     */
    public enum UserAction {
        LOGIN,              // 登录
        LOGOUT,             // 登出
        REGISTER,           // 注册
        UPDATE_PROFILE,     // 更新资料
        CHANGE_PASSWORD,    // 修改密码
        RESET_PASSWORD,     // 重置密码
        VERIFY_EMAIL,       // 验证邮箱
        CREATE_POST,        // 发布帖子
        DELETE_POST,        // 删除帖子
        CREATE_COMMENT,     // 发表评论
        DELETE_COMMENT,     // 删除评论
        FOLLOW_USER,        // 关注用户
        UNFOLLOW_USER,      // 取消关注
        UPLOAD_AVATAR,      // 上传头像
        EXPORT_DATA         // 导出数据
    }
} 