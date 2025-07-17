### Otter 系统框架图

```mermaid
graph TD
    A[用户界面层] --> B[业务逻辑层]
    B --> C[数据服务层]
    C --> D[基础设施层]
```

---

#### 1. **用户界面层**  

- **Web前端**：Vue3 + ECharts实现动态看板  
  - 测试覆盖率热力图（文件/方法/行三级钻取）  
  - 区块链存证验证面板（扫码即验真伪）  
- **移动端适配**：响应式设计支持手机查看报告  
- **二维码系统**：基于ZXing生成区块链存证验证码  

```mermaid
graph TD
 A((用户界面层))
        A1[Web前端] -->|Vue3+ECharts| A2[移动端适配]
        A3[报告二维码] -->|ZXing生成| A4[企业验证页面]

   
```

#### 2. **业务逻辑层（Java实现核心）**  

|      模块      |                           实现方案                           |            关键技术             |
| :------------: | :----------------------------------------------------------: | :-----------------------------: |
|  **事件监听**  |                        Webhook分发器                         |    Spring Webflux响应式编程     |
|  **AST解析**   |                        代码元数据提取                        |      JavaParser构建语法树       |
|  **测试生成**  | 四维用例生成策略：<br>1. 路径覆盖<br>2. 边界值<br>3. 异常流<br>4. 突变测试 | 规则引擎(Drools)+符号执行(KLEE) |
|  **测试执行**  |                         动态测试框架                         |    JUnit5 + Maven嵌入式调用     |
|  **覆盖分析**  |                         风险智能标记                         | JaCoCo字节码插桩 + 风险权重算法 |
| **区块链存证** |                          轻量级上链                          |      阿里云BaaS REST客户端      |
|  **报告生成**  |                          多格式输出                          |      PDF/HTML/JSON模板引擎      |

```mermaid
graph TD
       B((业务逻辑层))
        B1[事件监听模块] -->|Webhook监听| B2[核心控制器]
        B2 --> B3[AST解析引擎]
        B3 -->|JavaParser| B4[测试生成引擎]
        B4 -->|规则引擎+符号执行| B5[测试执行器]
        B5 -->|JUnit+JaCoCo| B6[覆盖分析器]
        B6 --> B7[区块链存证]
        B7 -->|阿里云BaaS| B8[报告生成]
        B8 --> B9[热力图生成]
```

#### 3. **数据服务层**  

- **AST缓存**：Redis存储语法树解析结果（TTL=1h）  
- **报告存储**：MySQL关系型存储（测试报告核心指标）  
- **区块链索引**：Elasticsearch实现秒级存证查询  
- **数据管道**：Logstash实现分析日志实时抽取  

```mermaid
 graph TD
 C((数据服务层))
        C1[元数据存储] -->|Redis| C2[AST缓存]
        C3[报告存储] -->|MySQL| C4[历史数据]
        C5[区块链索引] -->|ES| C6[快速检索]
```

#### 4. **基础设施层**  

|     组件     |     作用     |            选型理由            |
| :----------: | :----------: | :----------------------------: |
| **代码仓库** |    触发源    |     GitLab CE（开源可控）      |
|  **AI服务**  | 增强测试生成 | Facebook Infer（Java原生支持） |
|  **区块链**  |   可信存证   |      阿里云BaaS（免运维）      |
|  **CI/CD**   | 自动化流水线 |   GitHub Actions（免费额度）   |
|   **监控**   |  系统健康度  |   Prometheus+Grafana监控堆栈   |

```mermaid
graph TD
 D((基础设施层))
        D1[代码仓库] -->|GitLab/GitHub| D2[CI/CD]
        D3[AI服务] -->|Facebook Infer| D4[符号执行]
        D5[区块链节点] -->|阿里云BaaS| D6[分布式存储]
        D7[监控] -->|Prometheus| D8[日志分析]
 
```

### 关键数据流说明  
```mermaid
sequenceDiagram
    participant 开发者
    participant Git仓库
    participant 核心引擎
    participant 区块链
    participant 前端

    开发者->>Git仓库: git push代码
    Git仓库->>核心引擎: 触发Webhook事件
    核心引擎->>核心引擎: AST解析(JavaParser)
    核心引擎->>核心引擎: 测试生成(Facebook Infer)
    核心引擎->>核心引擎: 执行测试(JUnit+JaCoCo)
    alt 覆盖率>=80%
        核心引擎->>区块链: 提交报告哈希(阿里云BaaS)
        区块链-->>核心引擎: 返回交易ID
    else 覆盖率<80%
        核心引擎->>核心引擎: 生成风险热力图
    end
    核心引擎->>前端: 推送分析报告
    前端->>开发者: 可视化展示结果
    开发者->>区块链: 扫码验证报告
    区块链-->>开发者: 返回存证验证结果
```

---

### 创新点实现路径  
1. **智能测试生成**  
   ```mermaid
   graph LR
       S[源代码] --> A[AST解析]
       A --> B[控制流图生成]
       B --> C[路径分析]
       C --> D[边界值推导]
       D --> E[异常注入点]
       E --> F[组合测试用例]
   ```
   - **路径分析**：基于McCabe圈复杂度算法  
   - **边界值推导**：参数类型驱动的值域分析  

2. **区块链存证机制**  
   ```mermaid
   graph TB
       WW[报告] --> H[生成SHA-256哈希]
       H --> S[签名实验室私钥]
       S --> C[调用BaaS API]
       C --> T[获取区块链交易ID]
       T --> Q[生成验证二维码]
   ```
   - 抗篡改设计：哈希值+数字签名双重保护  

3. **风险热力图算法**  
   
   $\boxed{\text{RiskScore} = \alpha \cdot (1 - \text{Coverage}) + \beta \cdot \text{Complexity} + \gamma \cdot \text{ChangeFrequency}}$
   
   - 权重系数：α=0.6, β=0.3, γ=0.1  
   - 复杂度计算：Cyclomatic + Halstead指标  

---

### 部署拓扑  
```mermaid
graph LR
    LB[负载均衡] --> WS1[Web服务器]
    LB --> WS2[Web服务器]
    WS1 --> AS[应用服务器]
    WS2 --> AS
    AS --> DS[数据服务集群]
    DS --> DB(主数据库)
    DS --> BC[区块链网关]
    BC --> ALI[阿里云BaaS]
    
    style LB fill:#4CAF50,stroke:#388E3C
    style WS1 fill:#2196F3,stroke:#0D47A1
    style WS2 fill:#2196F3,stroke:#0D47A1
    style AS fill:#FFC107,stroke:#FFA000
    style DS fill:#9C27B0,stroke:#6A1B9A
    style DB fill:#F44336,stroke:#D32F2F
    style BC fill:#607D8B,stroke:#455A64
    style ALI fill:#00BCD4,stroke:#0097A7
```
