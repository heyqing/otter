package top.heyqing.otter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户实体
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    /**
     * 用户名
     */
    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;
    
    /**
     * 邮箱
     */
    @Column(name = "email", length = 100, unique = true)
    private String email;
    
    /**
     * 个人简介
     */
    @Column(name = "bio")
    private String bio;
    
    /**
     * 头像IPFS哈希
     */
    @Column(name = "avatar_ipfs_hash")
    private String avatarIpfsHash;
    
    /**
     * 钱包地址
     */
    @Column(name = "wallet_address", length = 42, unique = true, nullable = false)
    private String walletAddress;
    
    /**
     * 邮箱是否已验证
     */
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    /**
     * 是否被封禁
     */
    @Column(name = "is_banned")
    private Boolean isBanned = false;
    
    /**
     * 封禁原因
     */
    @Column(name = "ban_reason")
    private String banReason;
    
    /**
     * 封禁过期时间
     */
    @Column(name = "ban_expires_at")
    private LocalDateTime banExpiresAt;
    
    /**
     * 用户角色
     */
    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
    /**
     * 用户统计信息
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserStatistics statistics;
} 