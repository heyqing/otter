package top.heyqing.otter.content.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;
import top.heyqing.otter.user.entity.User;

@Data
@Entity
@Table(name = "likes")
@EqualsAndHashCode(callSuper = true)
public class Like extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
} 