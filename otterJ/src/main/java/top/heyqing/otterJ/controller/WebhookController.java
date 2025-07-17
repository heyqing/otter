package top.heyqing.otterJ.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.heyqing.otterJ.common.R;

@Tag(name = "Webhook管理", description = "接收和管理Git Webhook事件")
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    @Operation(summary = "接收Webhook事件", description = "接收来自GitHub/GitLab的Webhook推送事件")
    @PostMapping("/receive")
    public R<?> receiveWebhook(@RequestHeader(required = false) String signature,
                               @RequestBody String payload) {
        // TODO: 实现事件接收与处理
        return null;
    }

    @Operation(summary = "Webhook健康检查", description = "用于Webhook配置时的连通性测试")
    @GetMapping("/ping")
    public R<String> ping() {
        // TODO: 健康检查
        return null;
    }

    @Operation(summary = "Webhook事件历史查询", description = "分页查询已接收的Webhook事件")
    @GetMapping("/events")
    public R<?> listEvents(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        // TODO: 查询事件历史
        return null;
    }
}
