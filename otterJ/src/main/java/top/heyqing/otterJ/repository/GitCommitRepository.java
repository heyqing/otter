package top.heyqing.otterJ.repository;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName:GitCommitRepository
 * Package:top.heyqing.otterJ.repository
 * Description:
 *
 * @Date:2025/7/17
 * @Author:Heyqing
 */
@Data
public class GitCommitRepository implements Serializable {
    private static final long serialVersionUID = 3542302870942894305L;
    /**
     * 主键，自增ID
     */
    private Long id;

    /**
     * Git提交ID
     */
    private String commitId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 提交人用户名或邮箱
     */
    private String author;

    /**
     * 提交说明/备注
     */
    private String message;

    /**
     * 提交时间
     */
    private Date timestamp;

    /**
     * 变更文件数量
     */
    private Integer changedFiles;
}
