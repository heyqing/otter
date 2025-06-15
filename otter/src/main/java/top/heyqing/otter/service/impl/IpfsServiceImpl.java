package top.heyqing.otter.service.impl;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.heyqing.otter.service.IpfsService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class IpfsServiceImpl implements IpfsService {

    @Value("${ipfs.host}")
    private String ipfsHost;

    @Value("${ipfs.port}")
    private int ipfsPort;

    private IPFS getIpfs() {
        return new IPFS("/ip4/" + ipfsHost + "/tcp/" + ipfsPort);
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        IPFS ipfs = getIpfs();
        InputStream inputStream = file.getInputStream();
        NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(inputStream);
        MerkleNode response = ipfs.add(is).get(0);
        return response.hash.toString();
    }

    @Override
    public String uploadContent(String content) throws IOException {
        IPFS ipfs = getIpfs();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(inputStream);
        MerkleNode response = ipfs.add(is).get(0);
        return response.hash.toString();
    }

    @Override
    public byte[] getFile(String hash) throws IOException {
        IPFS ipfs = getIpfs();
        Multihash filePointer = Multihash.fromBase58(hash);
        return ipfs.cat(filePointer);
    }

    @Override
    public String getContent(String hash) throws IOException {
        byte[] data = getFile(hash);
        return new String(data);
    }
} 