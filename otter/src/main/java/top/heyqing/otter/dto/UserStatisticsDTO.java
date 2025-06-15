package top.heyqing.otter.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户统计信息DTO
 */
@Data
public class UserStatisticsDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 登录次数
     */
    private Integer loginCount;
    
    /**
     * 发帖数量
     */
    private Integer postCount;
    
    /**
     * 评论数量
     */
    private Integer commentCount;
    
    /**
     * 粉丝数量
     */
    private Integer followerCount;
    
    /**
     * 关注数量
     */
    private Integer followingCount;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;
    
    /**
     * 最后活动时间
     */
    private LocalDateTime lastActivityAt;
    
    /**
     * 注册时间
     */
    private LocalDateTime createdAt;
} 