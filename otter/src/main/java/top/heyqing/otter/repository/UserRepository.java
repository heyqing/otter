package top.heyqing.otter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.heyqing.otter.entity.UserEntity;

import java.util.Optional;

/**
 * 用户数据访问接口
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    Optional<UserEntity> findByUsername(String username);
    
    /**
     * 通过邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户对象
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * 通过钱包地址查找用户
     *
     * @param walletAddress 钱包地址
     * @return 用户对象
     */
    Optional<UserEntity> findByWalletAddress(String walletAddress);
    
    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 检查钱包地址是否存在
     *
     * @param walletAddress 钱包地址
     * @return 是否存在
     */
    boolean existsByWalletAddress(String walletAddress);
} 