package top.heyqing.otter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.heyqing.otter.common.Result;
import top.heyqing.otter.dto.*;
import top.heyqing.otter.entity.UserActivityLog;
import top.heyqing.otter.service.UserService;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    @PostMapping("/register")
    public Result<UserInfoDTO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        return Result.success(userService.register(registerDTO));
    }
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return JWT token
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }
    
    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/me")
    public Result<UserInfoDTO> getCurrentUser() {
        return Result.success(userService.getCurrentUser());
    }
    
    /**
     * 更新用户信息
     *
     * @param userInfoDTO 用户信息
     * @return 更新后的用户信息
     */
    @PutMapping("/me")
    public Result<UserInfoDTO> updateUserInfo(@Valid @RequestBody UserInfoDTO userInfoDTO) {
        return Result.success(userService.updateUserInfo(userInfoDTO));
    }
    
    /**
     * 请求密码重置
     *
     * @param email 用户邮箱
     * @return 成功信息
     */
    @PostMapping("/password/reset-request")
    public Result<Void> requestPasswordReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
        return Result.success();
    }
    
    /**
     * 重置密码
     *
     * @param resetDTO 重置信息
     * @return 成功信息
     */
    @PostMapping("/password/reset")
    public Result<Void> resetPassword(@Valid @RequestBody PasswordResetDTO resetDTO) {
        userService.resetPassword(resetDTO);
        return Result.success();
    }
    
    /**
     * 发送邮箱验证邮件
     *
     * @param email 用户邮箱
     * @return 成功信息
     */
    @PostMapping("/email/verification-request")
    public Result<Void> sendVerificationEmail(@RequestParam String email) {
        userService.sendVerificationEmail(email);
        return Result.success();
    }
    
    /**
     * 验证邮箱
     *
     * @param token 验证令牌
     * @return 成功信息
     */
    @PostMapping("/email/verify")
    public Result<Void> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return Result.success();
    }
    
    /**
     * 封禁用户
     *
     * @param userId 用户ID
     * @param reason 封禁原因
     * @param duration 封禁时长（小时）
     * @return 成功信息
     */
    @PostMapping("/{userId}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> banUser(
            @PathVariable Long userId,
            @RequestParam String reason,
            @RequestParam Integer duration) {
        userService.banUser(userId, reason, duration);
        return Result.success();
    }
    
    /**
     * 解封用户
     *
     * @param userId 用户ID
     * @return 成功信息
     */
    @PostMapping("/{userId}/unban")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> unbanUser(@PathVariable Long userId) {
        userService.unbanUser(userId);
        return Result.success();
    }
    
    /**
     * 导出用户数据
     *
     * @param userId 用户ID
     * @return 用户数据（JSON格式）
     */
    @GetMapping("/{userId}/export")
    public Result<String> exportUserData(@PathVariable Long userId) {
        return Result.success(userService.exportUserData(userId));
    }
    
    /**
     * 获取用户统计信息
     *
     * @param userId 用户ID
     * @return 用户统计信息
     */
    @GetMapping("/{userId}/statistics")
    public Result<UserStatisticsDTO> getUserStatistics(@PathVariable Long userId) {
        return Result.success(userService.getUserStatistics(userId));
    }
    
    /**
     * 获取用户操作日志
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 操作日志列表
     */
    @GetMapping("/{userId}/activity-logs")
    public Result<List<UserActivityLog>> getUserActivityLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(userService.getUserActivityLogs(userId, page, size));
    }
} 