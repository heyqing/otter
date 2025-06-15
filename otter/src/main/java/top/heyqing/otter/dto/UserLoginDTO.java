package top.heyqing.otter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户登录 DTO
 */
@Data
public class UserLoginDTO {
    /**
     * 钱包地址
     */
    @NotBlank(message = "钱包地址不能为空")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "钱包地址格式不正确")
    private String walletAddress;
    
    /**
     * 签名
     */
    @NotBlank(message = "签名不能为空")
    private String signature;
    
    /**
     * 随机字符串
     */
    @NotBlank(message = "随机字符串不能为空")
    private String nonce;
} 