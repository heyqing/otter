package top.heyqing.otterJ.repository;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ClassName:RiskScoreMetricsRepository
 * Package:top.heyqing.otterJ.repository
 * Description:
 *
 * @Date:2025/7/17
 * @Author:Heyqing
 */
@Data
public class RiskScoreMetricsRepository implements Serializable {
    private static final long serialVersionUID = -7745734996281910290L;
    /**
     * 主键，自增ID
     */
    private Long id;

    /**
     * 关联的测试报告ID
     */
    private Long reportId;

    /**
     * 代码文件路径
     */
    private String filePath;

    /**
     * 该文件的代码覆盖率
     */
    private BigDecimal coverage;

    /**
     * 该文件的圈复杂度评分
     */
    private BigDecimal complexity;

    /**
     * 该文件的变更频率（历史提交聚合值）
     */
    private BigDecimal changeFreq;

    /**
     * 最终计算出的风险评分
     */
    private BigDecimal riskScore;
}
