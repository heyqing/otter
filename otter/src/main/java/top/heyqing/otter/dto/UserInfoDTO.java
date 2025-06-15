package top.heyqing.otter.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户信息 DTO
 */
@Data
public class UserInfoDTO {
    /**
     * 用户ID
     */
    private String id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 个人简介
     */
    private String bio;
    
    /**
     * 头像IPFS哈希
     */
    private String avatarIpfsHash;
    
    /**
     * 角色列表
     */
    private Set<String> roles;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 