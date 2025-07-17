-- test_report（测试报告表）
CREATE TABLE test_report
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增ID',
    project_name     VARCHAR(128) NOT NULL COMMENT '项目名称或仓库名',
    commit_id        VARCHAR(64)  NOT NULL COMMENT 'Git提交ID',
    submitter        VARCHAR(64) COMMENT '提交人用户名或邮箱',
    coverage         DECIMAL(5, 2) COMMENT '代码覆盖率，百分比值',
    generated_at     DATETIME COMMENT '测试报告生成时间',
    report_path      VARCHAR(255) COMMENT '报告存储路径（如HTML/PDF文件路径）',
    report_hash      CHAR(64)     NOT NULL UNIQUE COMMENT '报告内容生成的SHA-256哈希值',
    risk_score       DECIMAL(5, 2) COMMENT '风险评分值（0~100）',
    status           ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'PENDING' COMMENT '测试状态：待处理、成功、失败',
    blockchain_tx_id VARCHAR(128) COMMENT '区块链交易ID（如已上链）',
    language         VARCHAR(32) DEFAULT 'Java' COMMENT '代码语言（如Java/Python）',
    version          VARCHAR(32) COMMENT '版本标识（如v1.0.0）',
    created_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',

    INDEX            idx_commit (commit_id),
    INDEX            idx_hash (report_hash),
    INDEX            idx_project (project_name)
) COMMENT='测试报告主表：记录一次完整的测试与报告生成过程';

-- blockchain_record（区块链存证记录表）
CREATE TABLE blockchain_record
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增ID',
    report_id    BIGINT   NOT NULL COMMENT '关联的测试报告ID',
    report_hash  CHAR(64) NOT NULL COMMENT '报告对应的SHA-256哈希值',
    signature    TEXT COMMENT '报告的RSA数字签名',
    tx_id        VARCHAR(128) COMMENT '区块链交易ID',
    chain_status ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'PENDING' COMMENT '上链状态：待处理、成功、失败',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    verify_url   VARCHAR(255) COMMENT '二维码验真跳转URL',

    INDEX        idx_hash (report_hash),
    CONSTRAINT fk_blockchain_report FOREIGN KEY (report_id) REFERENCES test_report (id)
) COMMENT='区块链存证表：记录报告上链后的哈希、签名和链上状态';


-- git_commit（提交记录表）
CREATE TABLE git_commit
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增ID',
    commit_id     VARCHAR(64)  NOT NULL COMMENT 'Git提交ID',
    project_name  VARCHAR(128) NOT NULL COMMENT '项目名称',
    author        VARCHAR(64) COMMENT '提交人用户名或邮箱',
    message       TEXT COMMENT '提交说明/备注',
    timestamp     DATETIME COMMENT '提交时间',
    changed_files INT COMMENT '变更文件数量',

    UNIQUE KEY uq_commit_project (commit_id, project_name)
) COMMENT='Git提交记录表：用于分析变更频率和代码热度';

-- risk_score_metrics（风险评分中间值表）
CREATE TABLE risk_score_metrics
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增ID',
    report_id   BIGINT       NOT NULL COMMENT '关联的测试报告ID',
    file_path   VARCHAR(255) NOT NULL COMMENT '代码文件路径',
    coverage    DECIMAL(5, 2) COMMENT '该文件的代码覆盖率',
    complexity  DECIMAL(5, 2) COMMENT '该文件的圈复杂度评分',
    change_freq DECIMAL(5, 2) COMMENT '该文件的变更频率（历史提交聚合值）',
    risk_score  DECIMAL(5, 2) COMMENT '最终计算出的风险评分',

    INDEX       idx_report (report_id),
    CONSTRAINT fk_risk_report FOREIGN KEY (report_id) REFERENCES test_report (id)
) COMMENT='风险评分指标表：记录文件级别的评分组成因子和最终评分';
