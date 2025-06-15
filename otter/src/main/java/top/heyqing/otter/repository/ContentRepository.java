package top.heyqing.otter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.heyqing.otter.entity.Content;
import top.heyqing.otter.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentRepository extends JpaRepository<Content, UUID> {
    Page<Content> findByUserAndIsActiveTrue(User user, Pageable pageable);
    List<Content> findByIsModeratedFalse();
    boolean existsByIpfsHash(String ipfsHash);
} 