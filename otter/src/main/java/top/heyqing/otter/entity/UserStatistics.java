package top.heyqing.otter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;

import java.time.LocalDateTime;

/**
 * 用户统计信息实体
 */
@Data
@Entity
@Table(name = "user_statistics")
@EqualsAndHashCode(callSuper = true)
public class UserStatistics extends BaseEntity {
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * 登录次数
     */
    @Column(name = "login_count")
    private Integer loginCount = 0;
    
    /**
     * 发帖数量
     */
    @Column(name = "post_count")
    private Integer postCount = 0;
    
    /**
     * 评论数量
     */
    @Column(name = "comment_count")
    private Integer commentCount = 0;
    
    /**
     * 粉丝数量
     */
    @Column(name = "follower_count")
    private Integer followerCount = 0;
    
    /**
     * 关注数量
     */
    @Column(name = "following_count")
    private Integer followingCount = 0;
    
    /**
     * 最后登录时间
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /**
     * 最后活动时间
     */
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
} 