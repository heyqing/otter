package top.heyqing.otter.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.heyqing.otter.dto.WalletAuthRequest;
import top.heyqing.otter.service.WalletAuthService;
import top.heyqing.otter.util.JwtUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WalletAuthServiceImpl implements WalletAuthService {

    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;
    private static final String NONCE_PREFIX = "wallet:nonce:";
    private static final long NONCE_EXPIRATION = 5; // 5分钟

    @Override
    public String generateNonce(String walletAddress) {
        String nonce = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
            NONCE_PREFIX + walletAddress,
            nonce,
            NONCE_EXPIRATION,
            TimeUnit.MINUTES
        );
        return nonce;
    }

    @Override
    public boolean verifySignature(WalletAuthRequest request) {
        String storedNonce = redisTemplate.opsForValue().get(NONCE_PREFIX + request.getWalletAddress());
        if (storedNonce == null || !storedNonce.equals(request.getNonce())) {
            return false;
        }
        
        // TODO: 实现Web3j签名验证
        // 这里需要调用Web3j的签名验证方法
        return true;
    }

    @Override
    public String generateToken(String walletAddress) {
        return jwtUtil.generateToken(walletAddress);
    }
} 