package top.heyqing.otter.blockchain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;
import top.heyqing.otter.user.entity.User;

@Data
@Entity
@Table(name = "blockchain_accounts")
@EqualsAndHashCode(callSuper = true)
public class BlockchainAccount extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "wallet_address", length = 100, unique = true, nullable = false)
    private String walletAddress;
} 