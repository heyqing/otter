package top.heyqing.otter.service;

import java.util.List;

public interface AiService {
    List<String> getRecommendations(String userId, int limit);
    String generateContent(String prompt);
    boolean moderateContent(String content);
} 