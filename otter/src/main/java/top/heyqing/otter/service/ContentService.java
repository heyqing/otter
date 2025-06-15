package top.heyqing.otter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import top.heyqing.otter.entity.Content;
import top.heyqing.otter.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ContentService {
    Content createContent(User user, String content) throws IOException;
    Content createContent(User user, MultipartFile file) throws IOException;
    Content getContent(UUID id);
    Page<Content> getUserContents(User user, Pageable pageable);
    void deleteContent(UUID id);
    String mintNFT(UUID contentId) throws Exception;
    List<Content> getUnmoderatedContents();
    void moderateContent(UUID contentId, boolean approved, String reason);
} 