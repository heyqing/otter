package top.heyqing.otter.service;

import top.heyqing.otter.dto.WalletAuthRequest;

public interface WalletAuthService {
    String generateNonce(String walletAddress);
    boolean verifySignature(WalletAuthRequest request);
    String generateToken(String walletAddress);
} 