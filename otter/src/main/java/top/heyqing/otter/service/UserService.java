package top.heyqing.otter.service;

import top.heyqing.otter.dto.*;
import top.heyqing.otter.entity.UserActivityLog;
import top.heyqing.otter.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    UserInfoDTO register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return JWT token
     */
    String login(UserLoginDTO loginDTO);
    
    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    UserInfoDTO getCurrentUser();
    
    /**
     * 更新用户信息
     *
     * @param userInfoDTO 用户信息
     * @return 更新后的用户信息
     */
    UserInfoDTO updateUserInfo(UserInfoDTO userInfoDTO);
    
    /**
     * 请求密码重置
     *
     * @param email 用户邮箱
     */
    void requestPasswordReset(String email);
    
    /**
     * 重置密码
     *
     * @param resetDTO 重置信息
     */
    void resetPassword(PasswordResetDTO resetDTO);
    
    /**
     * 发送邮箱验证邮件
     *
     * @param email 用户邮箱
     */
    void sendVerificationEmail(String email);
    
    /**
     * 验证邮箱
     *
     * @param token 验证令牌
     */
    void verifyEmail(String token);
    
    /**
     * 封禁用户
     *
     * @param userId 用户ID
     * @param reason 封禁原因
     * @param duration 封禁时长（小时）
     */
    void banUser(Long userId, String reason, Integer duration);
    
    /**
     * 解封用户
     *
     * @param userId 用户ID
     */
    void unbanUser(Long userId);
    
    /**
     * 导出用户数据
     *
     * @param userId 用户ID
     * @return 用户数据（JSON格式）
     */
    String exportUserData(Long userId);
    
    /**
     * 获取用户统计信息
     *
     * @param userId 用户ID
     * @return 用户统计信息
     */
    UserStatisticsDTO getUserStatistics(Long userId);
    
    /**
     * 获取用户操作日志
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 操作日志列表
     */
    List<UserActivityLog> getUserActivityLogs(Long userId, Integer page, Integer size);

    User createUser(String walletAddress);
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByWalletAddress(String walletAddress);
    User updateUser(User user);
    void deleteUser(UUID id);
} 