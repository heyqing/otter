
---

## 💡 开发阶段 1：核心基础设施搭建

### ✅ 开发原则

* 先搭建底座（基础架构、数据库、中间件）
* 再打通链路（Webhook→AST缓存→测试报告→区块链）
* 最后增强可观测性与部署能力（监控、日志、容器化）

---

### 🧱 1. 项目初始化与CI/CD搭建

| 项           | 内容                                                        |
| ----------- | --------------------------------------------------------- |
| **仓库结构**    | 建立主仓库，分为 `/backend`（Java） `/frontend`（预留） `/infra`（部署与配置） |
| **开发配置**    | 统一JDK版本（推荐 JDK17），配置Maven，启用Checkstyle或Spotless统一格式       |
| **CI/CD流程** | 使用 GitHub Actions，实现 `代码拉取 → 编译 → 单测 → 打包 → Docker镜像构建`   |
| **分支策略**    | main（发布） / dev（主开发） / feature-xxx（功能分支）                   |

---

### 🧮 2. 数据库与缓存结构设计

| 类别       | 技术            | 内容                                                                                                                                      |
| -------- | ------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| **数据库**  | MySQL         | - 设计并创建以下核心表：<br>① `test_report`：测试报告<br>② `blockchain_record`：链上存证记录<br>③ `git_commit`：提交信息与变更频率<br>④ `risk_score_metrics`：复杂度、频率、评分因子 |
| **缓存**   | Redis         | - 配置AST结果缓存<br>- 设定Key命名规范如：`ast:hash:xxx`，TTL=1h                                                                                       |
| **搜索索引** | Elasticsearch | - 建立 `report_index` 和 `blockchain_index`，用于区块链存证快速查询<br>- 预设mapping（包含哈希、报告ID、签名等字段）                                                    |

---

### 🧩 3. 后端架构骨架与接口预埋

| 模块        | 内容                                                                                                                                                              |
| --------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **架构分层**  | Controller / Service / Repository / ExternalClient（封装区块链、Git等服务）                                                                                                |
| **基础接口**  | 预先定义以下接口结构：<br>① `POST /api/webhook/receive`：Git事件接收<br>② `GET /api/report/{id}`：报告详情<br>③ `POST /api/blockchain/store`：报告上链<br>④ `GET /api/verify/{hash}`：报告验真 |
| **API文档** | 使用 Swagger/OpenAPI 自动生成文档                                                                                                                                       |
| **安全预留**  | 接口支持 JWT 鉴权与权限控制（RBAC，后续实现）                                                                                                                                     |

---

### 🔗 4. 区块链存证模块集成（阿里云 BaaS）

| 步骤        | 内容                                                                                               |
| --------- | ------------------------------------------------------------------------------------------------ |
| **接入准备**  | - 获取阿里云 BaaS 开发凭据<br>- 搭建测试链或使用沙箱环境                                                              |
| **服务封装**  | - 封装存证方法：`store(hash, signature)`<br>- 封装查询方法：`query(txId)`<br>- 封装验真流程：SHA-256 哈希 → RSA 签名 → 上链 |
| **数据同步**  | - 存证成功后，将交易 ID 与状态写入 `blockchain_record` 表和 ES 索引                                                |
| **二维码预留** | - 接入 ZXing 库，生成报告验真二维码（内容为 `/verify/{hash}` URL）                                                 |

---

### 🔍 5. Git Webhook监听服务开发

| 步骤       | 内容                                               |
| -------- | ------------------------------------------------ |
| **监听实现** | 使用 Spring Webflux 编写异步接收器，支持 GitHub/GitLab       |
| **事件解析** | 提取提交人、仓库URL、变更文件、提交时间等字段                         |
| **记录提交** | 将每次提交写入 `git_commit` 表，便于计算变更频率                  |
| **测试验证** | 使用 curl 或 Postman 模拟 GitHub webhook 事件测试接口是否触发成功 |

---

### 📊 6. 监控与日志系统建设

| 项目       | 内容                                                                  |
| -------- | ------------------------------------------------------------------- |
| **监控**   | 使用 Prometheus 采集应用运行指标（JVM、线程、接口响应等）<br>使用 Grafana 提供可视化仪表盘         |
| **健康检查** | 暴露 `/actuator/health` 接口用于存活检测                                      |
| **日志**   | 配置 Logback 区分 INFO / ERROR / AUDIT 等级<br>日志格式化为 JSON，写入文件或 Logstash |
| **审计日志** | 对关键操作（如存证、验真、测试报告生成）写入审计日志，便于未来合规审计                                 |

---

### 📦 7. 容器化与部署配置

| 内容                 | 说明                                                     |
| ------------------ | ------------------------------------------------------ |
| **Dockerfile**     | 编写标准 Java 构建镜像 Dockerfile，含 JDK、App、健康检查脚本等            |
| **docker-compose** | 支持本地快速启动 Redis、MySQL、ES、后端应用等                          |
| **Kubernetes准备**   | 编写 deployment.yaml、service.yaml（预留未来集群部署）              |
| **环境变量管理**         | Spring Profiles 区分 dev/test/prod，敏感信息通过 `.env` 或密钥注入管理 |

---

### ✅ 阶段验收标准

| 模块    | 验收目标                           |
| ----- | ------------------------------ |
| CI/CD | 每次 push 可自动构建 + 单元测试 + 生成镜像    |
| 数据库   | 所有核心表结构创建完毕并测试通过               |
| 接口    | API 可正常访问，Webhook 接收事件成功       |
| 区块链   | 可完成一次报告哈希上链并写入数据库与ES           |
| 缓存    | Redis AST缓存接口正常读写，TTL有效        |
| 日志    | 日志输出至控制台 + JSON格式 + 按等级分类      |
| 监控    | Prometheus 能采集数据，Grafana 可视化展示 |
| 容器    | 使用 docker-compose 一键启动所有服务     |

---

## 📌 开发顺序（推荐执行流）

1. 🟢 仓库初始化 + CI/CD 搭建
2. 🟢 MySQL 建模与建表 + Redis/ES 配置
3. 🟢 后端架构与API骨架搭建
4. 🟢 区块链存证服务封装 + 测试
5. 🟢 Webhook监听与Git事件接入
6. 🟢 日志与审计系统配置
7. 🟢 Prometheus/Grafana 接入
8. 🟢 Docker 化与一键部署配置
9. 🟢 阶段性集成测试与接口验收

---

```bash
otterJ/
├── .gitignore
├── pom.xml
├── otterDev.md
├── stage1.md
├── sql/
│   └── otter.sql                # 数据库建表脚本
├── infra/
│   ├── infra.md                 # 基础设施说明
│   ├── Dockerfile               # 后端服务Docker镜像构建
│   ├── docker-compose.yml       # 一键启动MySQL/Redis/ES/后端
│   └── k8s/                     # K8s部署清单（deployment/service等）
├── .github/
│   └── workflows/
│       └── ci.yml               # GitHub Actions CI/CD流程
├── src/
│   └── main/
│       ├── java/
│       │   └── top/
│       │       └── heyqing/
│       │           └── otterJ/
│       │               ├── OtterJApplication.java      # 启动类
│       │               ├── controller/                 # 控制层（API接口）
│       │               │   ├── WebhookController.java
│       │               │   ├── ReportController.java
│       │               │   └── BlockchainController.java
│       │               ├── service/                    # 业务逻辑层
│       │               │   ├── WebhookService.java
│       │               │   ├── ReportService.java
│       │               │   └── BlockchainService.java
│       │               ├── repository/                 # 数据访问层
│       │               │   ├── TestReportRepository.java
│       │               │   ├── BlockchainRecordRepository.java
│       │               │   ├── GitCommitRepository.java
│       │               │   └── RiskScoreMetricsRepository.java
│       │               ├── externalClient/             # 外部服务集成
│       │               │   ├── AliyunBaasClient.java
│       │               │   └── GitClient.java
│       │               └── config/                     # 配置类
│       │                   ├── SwaggerConfig.java
│       │                   ├── RedisConfig.java
│       │                   └── SecurityConfig.java
│       └── resources/
│           ├── application.yml                         # Spring Boot主配置
│           ├── logback-spring.xml                      # 日志配置（可选）
│           └── static/                                 # 静态资源（如二维码模板等）
└── test/
    └── java/
        └── top/
            └── heyqing/
                └── otterJ/
                    ├── controller/
                    ├── service/
                    └── repository/
```