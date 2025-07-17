---

# WebhookController 开发文档

## 1. 设计目标

- **接收并校验**：接收来自 GitHub/GitLab 的 Webhook 事件（如 push、merge、tag 等），校验来源合法性。
- **事件解析**：解析事件 payload，提取关键信息（项目、分支、提交人、commit id、变更文件等）。
- **数据落库**：将提交信息写入 `git_commit` 表，为后续变更频率、风险评分等提供数据基础。
- **流程触发**：异步触发后续流程（如 AST 解析、测试生成、报告生成、区块链存证等）。
- **幂等与安全**：防止重复处理，支持签名校验、权限校验等安全措施。
- **调试与监控**：日志记录、接口健康检查、事件追踪。

---

## 2. 推荐接口列表

### 2.1. 接收 Webhook 事件

- **POST /api/webhook/receive**
    - **描述**：接收并处理来自 GitHub/GitLab 的 Webhook 事件
    - **请求头**：
        - `X-GitHub-Event` / `X-Gitlab-Event`：事件类型
        - `X-Hub-Signature` / `X-Gitlab-Token`：签名/令牌（可选，安全校验）
    - **请求体**：JSON（原始 Webhook payload）
    - **响应**：
        - 200 OK：接收成功
        - 400/401：校验失败
    - **示例**：
      ```json
      {
        "ref": "refs/heads/main",
        "before": "xxxx",
        "after": "yyyy",
        "repository": { "name": "otterJ", "url": "..." },
        "commits": [
          {
            "id": "abc123",
            "author": { "name": "dev", "email": "dev@example.com" },
            "message": "fix: bug",
            "timestamp": "2024-06-01T12:00:00Z",
            "added": ["src/main/java/xx.java"],
            "modified": ["src/main/java/yy.java"],
            "removed": []
          }
        ],
        "pusher": { "name": "dev" }
      }
      ```

### 2.2. Webhook 健康检查

- **GET /api/webhook/ping**
    - **描述**：用于 Webhook 配置时的连通性测试
    - **响应**：200 OK，返回 `pong` 或服务状态

### 2.3. （可选）Webhook 事件历史查询

- **GET /api/webhook/events**
    - **描述**：分页查询已接收的 Webhook 事件（便于调试和追溯）
    - **参数**：page, size, eventType, 时间区间等
    - **响应**：事件列表

---

## 3. 主要处理流程

1. **接收请求**：监听 POST /api/webhook/receive
2. **安全校验**：校验签名/Token，防止伪造
3. **事件解析**：解析 payload，提取 commit、项目、分支、作者、变更文件等
4. **数据落库**：写入 `git_commit` 表
5. **异步触发**：调用 Service 层，异步触发 AST 解析、测试生成等后续流程
6. **日志记录**：记录事件、处理结果、异常等
7. **返回响应**：及时返回 200/400/401 等状态码

---

## 4. 伪代码/接口定义示例

```java
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    @PostMapping("/receive")
    public ResponseEntity<?> receiveWebhook(
        @RequestHeader Map<String, String> headers,
        @RequestBody String payload) {
        // 1. 校验签名/Token
        // 2. 解析事件类型
        // 3. 解析payload，提取关键信息
        // 4. 数据落库
        // 5. 异步触发后续流程
        // 6. 日志记录
        return ResponseEntity.ok("received");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/events")
    public Page<WebhookEvent> listEvents(@RequestParam int page, @RequestParam int size) {
        // 查询历史事件
    }
}
```

---

## 5. 安全与幂等建议

- 支持 GitHub/GitLab Webhook 的签名校验（如 X-Hub-Signature、Token）
- 对同一 commit id 或 delivery id 做幂等处理，防止重复入库
- 日志中脱敏敏感信息

---

## 6. 日志与监控

- 记录每次事件的 header、payload、处理结果、异常
- 关键路径打点，便于后续链路追踪和问题定位

---

## 7. 测试建议

- 使用 curl/Postman 模拟 Webhook 事件
- 覆盖正常、异常、重复、非法签名等场景

---

## 8. 文档与接口说明

- 在 Swagger/OpenAPI 文档中补充接口说明、示例请求/响应
- 标注安全校验、参数说明、异常码等

---
