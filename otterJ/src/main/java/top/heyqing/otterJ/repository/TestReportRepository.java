package top.heyqing.otterJ.repository;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:TestReportRepository
 * Package:top.heyqing.otterJ.repository
 * Description:
 *
 * @Date:2025/7/17
 * @Author:Heyqing
 */
@Data
public class TestReportRepository implements Serializable {
    private static final long serialVersionUID = 5560425706965121318L;
    /**
     * 主键，自增ID
     */
    private Long id;

    /**
     * 项目名称或仓库名
     */
    private String projectName;

    /**
     * Git提交ID
     */
    private String commitId;

    /**
     * 提交人用户名或邮箱
     */
    private String submitter;

    /**
     * 代码覆盖率，百分比值
     */
    private BigDecimal coverage;

    /**
     * 测试报告生成时间
     */
    private Date generatedAt;

    /**
     * 报告存储路径（如HTML/PDF文件路径）
     */
    private String reportPath;

    /**
     * 报告内容生成的SHA-256哈希值
     */
    private String reportHash;

    /**
     * 风险评分值（0~100）
     */
    private BigDecimal riskScore;

    /**
     * 测试状态：待处理、成功、失败
     */
    private Object status;

    /**
     * 区块链交易ID（如已上链）
     */
    private String blockchainTxId;

    /**
     * 代码语言（如Java/Python）
     */
    private String language;

    /**
     * 版本标识（如v1.0.0）
     */
    private String version;

    /**
     * 记录创建时间
     */
    private Date createdAt;
}
