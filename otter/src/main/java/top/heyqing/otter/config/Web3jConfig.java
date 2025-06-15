package top.heqyqing.otter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    @Value("${web3.provider.url}")
    private String web3ProviderUrl;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(web3ProviderUrl));
    }
} 