package top.heyqing.otter.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IpfsService {
    String uploadFile(MultipartFile file) throws IOException;
    String uploadContent(String content) throws IOException;
    byte[] getFile(String hash) throws IOException;
    String getContent(String hash) throws IOException;
} 