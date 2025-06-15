package top.heyqing.otter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.heyqing.otter.entity.Content;
import top.heyqing.otter.entity.User;
import top.heyqing.otter.service.ContentService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<Content> createContent(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        if (content != null) {
            return ResponseEntity.ok(contentService.createContent(user, content));
        } else if (file != null) {
            return ResponseEntity.ok(contentService.createContent(user, file));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContent(@PathVariable UUID id) {
        return ResponseEntity.ok(contentService.getContent(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<Content>> getUserContents(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(contentService.getUserContents(user, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable UUID id) {
        contentService.deleteContent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/mint")
    public ResponseEntity<String> mintNFT(@PathVariable UUID id) throws Exception {
        String nftTokenId = contentService.mintNFT(id);
        return ResponseEntity.ok(nftTokenId);
    }

    @GetMapping("/unmoderated")
    public ResponseEntity<List<Content>> getUnmoderatedContents() {
        return ResponseEntity.ok(contentService.getUnmoderatedContents());
    }

    @PostMapping("/{id}/moderate")
    public ResponseEntity<Void> moderateContent(
            @PathVariable UUID id,
            @RequestParam boolean approved,
            @RequestParam(required = false) String reason) {
        contentService.moderateContent(id, approved, reason);
        return ResponseEntity.ok().build();
    }
} 