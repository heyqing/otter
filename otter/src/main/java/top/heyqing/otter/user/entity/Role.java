package top.heyqing.otter.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;

@Data
@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
} 