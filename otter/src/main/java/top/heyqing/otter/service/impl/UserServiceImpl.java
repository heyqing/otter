package top.heyqing.otter.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.heyqing.otter.annotation.LogUserActivity;
import top.heyqing.otter.blockchain.util.Web3Utils;
import top.heyqing.otter.common.exception.BusinessException;
import top.heyqing.otter.dto.*;
import top.heyqing.otter.entity.*;
import top.heyqing.otter.repository.*;
import top.heyqing.otter.security.JwtTokenProvider;
import top.heyqing.otter.service.EmailService;
import top.heyqing.otter.service.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserStatisticsRepository statisticsRepository;
    private final UserActivityLogRepository activityLogRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final Web3Utils web3Utils;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    
    @Override
    @Transactional
    @LogUserActivity("用户注册")
    public UserInfoDTO register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否已存在（如果提供了邮箱）
        if (registerDTO.getEmail() != null && userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }
        
        // 检查钱包地址是否已存在
        if (userRepository.existsByWalletAddress(registerDTO.getWalletAddress())) {
            throw new BusinessException("钱包地址已存在");
        }
        
        // 验证签名
        if (!web3Utils.verifySignature(registerDTO.getNonce(), registerDTO.getSignature(), registerDTO.getWalletAddress())) {
            throw new BusinessException("签名验证失败");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setWalletAddress(registerDTO.getWalletAddress());
        
        // 设置默认角色
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new BusinessException("默认角色不存在"));
        user.setRoles(new HashSet<>(Set.of(userRole)));
        
        // 创建用户统计信息
        UserStatistics statistics = new UserStatistics();
        statistics.setUser(user);
        user.setStatistics(statistics);
        
        // 保存用户
        user = userRepository.save(user);
        
        // 如果提供了邮箱，发送验证邮件
        if (user.getEmail() != null) {
            try {
                sendVerificationEmail(user.getEmail());
            } catch (MessagingException e) {
                // 记录错误但不影响注册流程
                e.printStackTrace();
            }
        }
        
        return convertToDTO(user);
    }
    
    @Override
    @LogUserActivity("用户登录")
    public String login(UserLoginDTO loginDTO) {
        // 查找用户
        User user = userRepository.findByWalletAddress(loginDTO.getWalletAddress())
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 检查用户是否被封禁
        if (user.getIsBanned()) {
            if (user.getBanExpiresAt() != null && user.getBanExpiresAt().isAfter(LocalDateTime.now())) {
                throw new BusinessException("账号已被封禁，解封时间：" + user.getBanExpiresAt());
            } else {
                // 封禁已过期，解除封禁
                user.setIsBanned(false);
                user.setBanReason(null);
                user.setBanExpiresAt(null);
                userRepository.save(user);
            }
        }
        
        // 验证签名
        if (!web3Utils.verifySignature(loginDTO.getNonce(), loginDTO.getSignature(), loginDTO.getWalletAddress())) {
            throw new BusinessException("签名验证失败");
        }
        
        // 创建认证信息
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            null,
            user.getRoles().stream()
                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList())
        );
        
        // 设置认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 更新用户统计信息
        UserStatistics statistics = user.getStatistics();
        statistics.setLoginCount(statistics.getLoginCount() + 1);
        statistics.setLastLoginAt(LocalDateTime.now());
        statistics.setLastActivityAt(LocalDateTime.now());
        statisticsRepository.save(statistics);
        
        // 生成 JWT token
        return jwtTokenProvider.generateToken(authentication);
    }
    
    @Override
    public UserInfoDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        return convertToDTO(user);
    }
    
    @Override
    @Transactional
    @LogUserActivity("更新用户信息")
    public UserInfoDTO updateUserInfo(UserInfoDTO userInfoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 更新用户信息
        user.setBio(userInfoDTO.getBio());
        user.setAvatarIpfsHash(userInfoDTO.getAvatarIpfsHash());
        
        user = userRepository.save(user);
        
        // 更新最后活动时间
        UserStatistics statistics = user.getStatistics();
        statistics.setLastActivityAt(LocalDateTime.now());
        statisticsRepository.save(statistics);
        
        return convertToDTO(user);
    }
    
    @Override
    @LogUserActivity("请求密码重置")
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 生成重置令牌
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(24));
        passwordResetTokenRepository.save(resetToken);
        
        // 发送重置邮件
        try {
            emailService.sendPasswordResetEmail(email, token, user.getUsername());
        } catch (MessagingException e) {
            throw new BusinessException("发送重置邮件失败");
        }
    }
    
    @Override
    @Transactional
    @LogUserActivity("重置密码")
    public void resetPassword(PasswordResetDTO resetDTO) {
        // 验证令牌
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(resetDTO.getToken())
            .orElseThrow(() -> new BusinessException("无效的重置令牌"));
        
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("重置令牌已过期");
        }
        
        // 更新密码
        User user = resetToken.getUser();
        // TODO: 实现密码更新逻辑
        
        // 删除已使用的令牌
        passwordResetTokenRepository.delete(resetToken);
    }
    
    @Override
    @LogUserActivity("发送邮箱验证邮件")
    public void sendVerificationEmail(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 生成验证令牌
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(24));
        emailVerificationTokenRepository.save(verificationToken);
        
        // 发送验证邮件
        emailService.sendVerificationEmail(email, token, user.getUsername());
    }
    
    @Override
    @Transactional
    @LogUserActivity("验证邮箱")
    public void verifyEmail(String token) {
        // 验证令牌
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new BusinessException("无效的验证令牌"));
        
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("验证令牌已过期");
        }
        
        // 更新用户邮箱验证状态
        User user = verificationToken.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
        
        // 删除已使用的令牌
        emailVerificationTokenRepository.delete(verificationToken);
    }
    
    @Override
    @Transactional
    @LogUserActivity("封禁用户")
    public void banUser(Long userId, String reason, Integer duration) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        user.setIsBanned(true);
        user.setBanReason(reason);
        user.setBanExpiresAt(duration > 0 ? LocalDateTime.now().plusHours(duration) : null);
        userRepository.save(user);
    }
    
    @Override
    @Transactional
    @LogUserActivity("解封用户")
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        user.setIsBanned(false);
        user.setBanReason(null);
        user.setBanExpiresAt(null);
        userRepository.save(user);
    }
    
    @Override
    @LogUserActivity("导出用户数据")
    public String exportUserData(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // TODO: 实现用户数据导出逻辑，返回JSON格式的数据
        return "{}";
    }
    
    @Override
    public UserStatisticsDTO getUserStatistics(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        UserStatistics statistics = user.getStatistics();
        UserStatisticsDTO dto = new UserStatisticsDTO();
        dto.setUserId(userId);
        dto.setLoginCount(statistics.getLoginCount());
        dto.setPostCount(statistics.getPostCount());
        dto.setCommentCount(statistics.getCommentCount());
        dto.setFollowerCount(statistics.getFollowerCount());
        dto.setFollowingCount(statistics.getFollowingCount());
        dto.setLastLoginAt(statistics.getLastLoginAt());
        dto.setLastActivityAt(statistics.getLastActivityAt());
        dto.setCreatedAt(user.getCreatedAt());
        
        return dto;
    }
    
    @Override
    public List<UserActivityLog> getUserActivityLogs(Long userId, Integer page, Integer size) {
        Page<UserActivityLog> logs = activityLogRepository.findByUserIdOrderByCreatedAtDesc(
            userId,
            PageRequest.of(page, size)
        );
        return logs.getContent();
    }
    
    /**
     * 将用户实体转换为 DTO
     */
    private UserInfoDTO convertToDTO(User user) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(user.getId().toString());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setAvatarIpfsHash(user.getAvatarIpfsHash());
        dto.setWalletAddress(user.getWalletAddress());
        dto.setIsVerified(user.getIsVerified());
        dto.setRoles(user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet()));
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
} 