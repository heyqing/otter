package top.heyqing.otter.service;

import java.math.BigInteger;

public interface BlockchainService {
    String mintNFT(String walletAddress, String contentHash) throws Exception;
    void sendTip(String fromAddress, String toAddress, BigInteger amount) throws Exception;
    BigInteger getBalance(String walletAddress) throws Exception;
    boolean verifySignature(String message, String signature, String walletAddress) throws Exception;
} 