package top.heyqing.otterJ.repository;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName:BlockchainRecordRepository
 * Package:top.heyqing.otterJ.repository
 * Description:
 *
 * @Date:2025/7/17
 * @Author:Heyqing
 */
@Data
public class BlockchainRecordRepository implements Serializable {
    private static final long serialVersionUID = -2670446379413327158L;
    /**
     * 主键，自增ID
     */
    private Long id;

    /**
     * 关联的测试报告ID
     */
    private Long reportId;

    /**
     * 报告对应的SHA-256哈希值
     */
    private String reportHash;

    /**
     * 报告的RSA数字签名
     */
    private String signature;

    /**
     * 区块链交易ID
     */
    private String txId;

    /**
     * 上链状态：待处理、成功、失败
     */
    private Object chainStatus;

    /**
     * 记录创建时间
     */
    private Date createdAt;

    /**
     * 二维码验真跳转URL
     */
    private String verifyUrl;
}