package top.heyqing.otter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WalletAuthRequest {
    @NotBlank(message = "钱包地址不能为空")
    private String walletAddress;
    
    @NotBlank(message = "签名不能为空")
    private String signature;
    
    @NotBlank(message = "随机数不能为空")
    private String nonce;
} 