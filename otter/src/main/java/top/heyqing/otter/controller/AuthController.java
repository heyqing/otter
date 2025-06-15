package top.heyqing.otter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.heyqing.otter.dto.WalletAuthRequest;
import top.heyqing.otter.service.UserService;
import top.heyqing.otter.service.WalletAuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final WalletAuthService walletAuthService;
    private final UserService userService;

    @GetMapping("/nonce")
    public ResponseEntity<Map<String, String>> getNonce(@RequestParam String walletAddress) {
        String nonce = walletAuthService.generateNonce(walletAddress);
        Map<String, String> response = new HashMap<>();
        response.put("nonce", nonce);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody WalletAuthRequest request) {
        if (!walletAuthService.verifySignature(request)) {
            return ResponseEntity.badRequest().build();
        }

        // 如果用户不存在，自动注册
        userService.getUserByWalletAddress(request.getWalletAddress())
                .orElseGet(() -> userService.createUser(request.getWalletAddress()));

        String token = walletAuthService.generateToken(request.getWalletAddress());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
} 