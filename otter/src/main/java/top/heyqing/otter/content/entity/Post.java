package top.heyqing.otter.content.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;
import top.heyqing.otter.entity.User;

@Data
@Entity
@Table(name = "posts")
@EqualsAndHashCode(callSuper = true)
public class Post extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    @Column(name = "ipfs_hash")
    private String ipfsHash;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
} 