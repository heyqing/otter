package top.heyqing.otter.config;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IpfsConfig {

    @Value("${ipfs.api.host}")
    private String ipfsHost;

    @Value("${ipfs.api.port}")
    private int ipfsPort;

    @Bean
    public IPFS ipfs() {
        return new IPFS("/ip4/" + ipfsHost + "/tcp/" + ipfsPort);
    }
} 