package top.heyqing.otter.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;

@Data
@Entity
@Table(name = "follows")
@EqualsAndHashCode(callSuper = true)
public class Follow extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
    
    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private User followee;
} 