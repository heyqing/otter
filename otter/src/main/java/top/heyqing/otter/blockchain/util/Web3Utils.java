package top.heyqing.otter.blockchain.util;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

/**
 * Web3 工具类
 */
@Component
public class Web3Utils {
    
    /**
     * 验证签名
     *
     * @param message   消息
     * @param signature 签名
     * @param address   钱包地址
     * @return 是否验证通过
     */
    public boolean verifySignature(String message, String signature, String address) {
        try {
            // 将消息转换为字节数组
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            
            // 将签名转换为字节数组
            byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
            
            // 获取签名数据
            Sign.SignatureData signatureData = Sign.SignatureData.create(
                signatureBytes[64] < 27 ? (byte) (signatureBytes[64] + 27) : signatureBytes[64],
                java.util.Arrays.copyOfRange(signatureBytes, 0, 32),
                java.util.Arrays.copyOfRange(signatureBytes, 32, 64)
            );
            
            // 恢复签名者地址
            String recoveredAddress = "0x" + Keys.getAddress(Sign.recoverFromSignature(
                signatureData.getV(),
                Sign.SignatureData.create(
                    signatureData.getV(),
                    signatureData.getR(),
                    signatureData.getS()
                ),
                messageBytes
            ));
            
            // 比较地址
            return recoveredAddress.equalsIgnoreCase(address);
        } catch (SignatureException e) {
            return false;
        }
    }
} 