

------

# 🌐 单体项目开发文档：AI + Web3 去中心化社交平台

------

## 目录

1. 项目概述
2. 技术选型
3. 系统架构
4. 模块设计
5. 接口设计
6. 数据库设计
7. Web3 接入说明
8. IPFS 接入说明
9. AI 模块接入说明
10. 安全与认证设计
11. 项目部署与测试
12. 扩展与升级建议

------

## 1. 项目概述

本平台是一个基于 **区块链 Web3 + AI 内容推荐与生成能力** 的去中心化社交平台。用户通过钱包认证登录，可发布内容、评论、打赏、生成 NFT，并使用 AI 获取内容推荐或生成创作建议。

------

## 2. 技术选型

| 类别         | 技术                                         |
| ------------ | -------------------------------------------- |
| 语言         | Java 17                                      |
| 框架         | Spring Boot 3.x                              |
| 数据库       | PostgreSQL 15                                |
| 缓存         | Redis                                        |
| 区块链       | Web3j（兼容以太坊）                          |
| 去中心化存储 | IPFS-Java-Client                             |
| AI 服务      | 本地部署/远程 API（可选 OpenAI、文心一言等） |
| 安全         | Spring Security + JWT                        |
| 接口文档     | Swagger 3                                    |
| 构建         | Maven                                        |
| 容器化部署   | Docker（可选）                               |

------

## 3. 系统架构

```
Spring Boot 单体架构
├── 用户认证（钱包签名）
├── 内容管理（发布、评论、点赞）
├── IPFS 内容存储
├── 区块链操作（存证、NFT）
├── AI 模块（推荐、生成）
├── 安全认证模块（JWT）
├── 管理后台接口（预留）
```

------

## 4. 模块设计

| 模块         | 说明                             |
| ------------ | -------------------------------- |
| `user`       | 钱包登录、用户信息管理           |
| `content`    | 发布动态、评论、点赞             |
| `ai`         | AI 推荐和文本生成                |
| `blockchain` | Web3 交互（合约、NFT、打赏）     |
| `ipfs`       | 去中心化存储管理                 |
| `security`   | 登录、鉴权、权限控制             |
| `common`     | 公共工具类、统一返回、异常处理等 |

------

## 5. 接口设计（部分）

### 5.1 钱包登录

- `GET /api/auth/nonce` 获取登录随机字符串
- `POST /api/auth/login` 提交签名登录（返回 JWT）

### 5.2 内容操作

- `POST /api/content` 发布动态（内容上传 IPFS，返回 CID）
- `GET /api/feed` 获取推荐内容流（调用 AI 推荐服务）
- `POST /api/comment` 发布评论
- `POST /api/like/{postId}` 点赞

### 5.3 区块链

- `POST /api/nft/mint` 将指定动态铸造为 NFT
- `POST /api/tip` 对某个用户内容进行打赏（ERC-20）

------

## 6. 数据库设计（PostgreSQL）

### 6.1 用户表 `users`

| 字段           | 类型        | 描述           |
| -------------- | ----------- | -------------- |
| id             | UUID        | 主键           |
| wallet_address | VARCHAR(42) | 钱包地址       |
| nickname       | VARCHAR     | 昵称           |
| avatar_url     | TEXT        | 头像 IPFS 链接 |
| created_at     | TIMESTAMP   | 注册时间       |

### 6.2 内容表 `posts`

| 字段       | 类型      | 描述          |
| ---------- | --------- | ------------- |
| id         | UUID      | 主键          |
| user_id    | UUID      | 发布者        |
| ipfs_hash  | TEXT      | 内容 IPFS CID |
| preview    | TEXT      | 内容摘要      |
| created_at | TIMESTAMP | 发布时间      |

### 6.3 评论表 `comments`

| 字段       | 类型      | 描述     |
| ---------- | --------- | -------- |
| id         | UUID      | 主键     |
| post_id    | UUID      | 关联动态 |
| user_id    | UUID      | 评论者   |
| content    | TEXT      | 内容     |
| created_at | TIMESTAMP | 时间     |

------

## 7. Web3 接入说明（Web3j）

- 引入依赖：

```xml
<dependency>
  <groupId>org.web3j</groupId>
  <artifactId>core</artifactId>
  <version>4.8.7</version>
</dependency>
```

- 示例代码：发送交易、存储 IPFS hash

```java
TransactionReceipt receipt = contract.storeHash(ipfsHash).send();
```

------

## 8. IPFS 接入说明

- 可选方案：
  - 本地 IPFS 节点（推荐）
  - Infura/Pinata（无需部署节点）
- Java 上传示例：

```java
String cid = ipfsClient.add(new NamedStreamable.ByteArrayWrapper(content.getBytes()));
```

------

## 9. AI 模块接入说明

- 推荐使用 REST API 接入已有大模型：
  - OpenAI、Claude、文心一言、GLM、ChatGLM 等
- Feign 示例调用：

```java
@FeignClient(name = "aiService", url = "${ai.url}")
public interface AiClient {
    @PostMapping("/recommend")
    List<Post> recommendForUser(@RequestBody RecommendRequest req);
}
```

------

## 10. 安全认证设计

- 钱包登录机制：
  1. 用户请求 nonce
  2. 前端用钱包签名 nonce
  3. 后端验证签名生成 JWT
- 使用 `Spring Security + JWT` 实现

```java
UsernamePasswordAuthenticationToken token = 
  new UsernamePasswordAuthenticationToken(walletAddress, null, authorities);
SecurityContextHolder.getContext().setAuthentication(token);
```

------

## 11. 项目部署与测试

### 本地开发

```bash
./mvnw spring-boot:run
```

### 打包部署

```bash
mvn clean package
java -jar target/social-platform.jar
```

### Docker 部署（可选）

```Dockerfile
FROM openjdk:17
COPY target/social-platform.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

------

