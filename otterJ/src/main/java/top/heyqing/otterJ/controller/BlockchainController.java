package top.heyqing.otterJ.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import top.heyqing.otterJ.common.R;

@Tag(name = "区块链存证管理", description = "报告上链与验真接口")
@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    @Operation(summary = "报告上链存证", description = "将测试报告哈希和签名上链存证")
    @PostMapping("/store")
    public R<?> storeOnChain(@RequestBody String request) {
        // TODO: 上链存证
        return null;
    }

    @Operation(summary = "报告验真", description = "根据哈希或交易ID验真报告")
    @GetMapping("/verify/{hash}")
    public R<?> verifyReport(@PathVariable String hash) {
        // TODO: 验真
        return null;
    }
} 