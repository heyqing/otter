package top.heyqing.otter.ipfs.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.heyqing.otter.common.BaseEntity;
import top.heyqing.otter.entity.User;

@Data
@Entity
@Table(name = "ipfs_files")
@EqualsAndHashCode(callSuper = true)
public class IpfsFile extends BaseEntity {
    
    @Column(name = "ipfs_hash", unique = true, nullable = false)
    private String ipfsHash;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;
} 