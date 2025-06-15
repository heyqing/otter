package top.heyqing.otter.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.heyqing.otter.entity.Content;
import top.heyqing.otter.entity.User;
import top.heyqing.otter.repository.ContentRepository;
import top.heyqing.otter.service.AiService;
import top.heyqing.otter.service.BlockchainService;
import top.heyqing.otter.service.ContentService;
import top.heyqing.otter.service.IpfsService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final IpfsService ipfsService;
    private final BlockchainService blockchainService;
    private final AiService aiService;

    @Override
    @Transactional
    public Content createContent(User user, String content) throws IOException {
        // 上传内容到IPFS
        String ipfsHash = ipfsService.uploadContent(content);
        
        // 创建内容记录
        Content contentEntity = new Content();
        contentEntity.setUser(user);
        contentEntity.setIpfsHash(ipfsHash);
        
        // 保存内容
        contentEntity = contentRepository.save(contentEntity);
        
        // 异步进行内容审核
        moderateContentAsync(contentEntity.getId());
        
        return contentEntity;
    }

    @Override
    @Transactional
    public Content createContent(User user, MultipartFile file) throws IOException {
        // 上传文件到IPFS
        String ipfsHash = ipfsService.uploadFile(file);
        
        // 创建内容记录
        Content contentEntity = new Content();
        contentEntity.setUser(user);
        contentEntity.setIpfsHash(ipfsHash);
        
        // 保存内容
        contentEntity = contentRepository.save(contentEntity);
        
        // 异步进行内容审核
        moderateContentAsync(contentEntity.getId());
        
        return contentEntity;
    }

    @Override
    public Content getContent(UUID id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found"));
    }

    @Override
    public Page<Content> getUserContents(User user, Pageable pageable) {
        return contentRepository.findByUserAndIsActiveTrue(user, pageable);
    }

    @Override
    @Transactional
    public void deleteContent(UUID id) {
        Content content = getContent(id);
        content.setIsActive(false);
        contentRepository.save(content);
    }

    @Override
    @Transactional
    public String mintNFT(UUID contentId) throws Exception {
        Content content = getContent(contentId);
        
        // 铸造NFT
        String nftTokenId = blockchainService.mintNFT(
            content.getUser().getWalletAddress(),
            content.getIpfsHash()
        );
        
        // 更新内容记录
        content.setNftTokenId(nftTokenId);
        contentRepository.save(content);
        
        return nftTokenId;
    }

    @Override
    public List<Content> getUnmoderatedContents() {
        return contentRepository.findByIsModeratedFalse();
    }

    @Override
    @Transactional
    public void moderateContent(UUID contentId, boolean approved, String reason) {
        Content content = getContent(contentId);
        content.setIsModerated(true);
        content.setIsActive(approved);
        content.setModerationResult(reason);
        contentRepository.save(content);
    }

    private void moderateContentAsync(UUID contentId) {
        // TODO: 实现异步内容审核
        // 这里可以使用Spring的异步任务或消息队列
    }
} 