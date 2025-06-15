package top.heyqing.otter.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.heyqing.otter.service.AiService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    @Value("${ai.url}")
    private String aiUrl;

    @Value("${ai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public List<String> getRecommendations(String userId, int limit) {
        // TODO: 实现内容推荐逻辑
        // 这里需要调用AI服务的推荐API
        return new ArrayList<>();
    }

    @Override
    public String generateContent(String prompt) {
        // TODO: 实现内容生成逻辑
        // 这里需要调用AI服务的生成API
        return "Generated content for: " + prompt;
    }

    @Override
    public boolean moderateContent(String content) {
        // TODO: 实现内容审核逻辑
        // 这里需要调用AI服务的审核API
        return true;
    }
} 