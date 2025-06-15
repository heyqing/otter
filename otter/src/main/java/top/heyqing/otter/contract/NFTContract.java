package top.heyqing.otter.contract;

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public interface NFTContract {
    TransactionReceipt mint(String to, String tokenURI) throws Exception;
    String tokenURI(BigInteger tokenId) throws Exception;
    String ownerOf(BigInteger tokenId) throws Exception;
    BigInteger balanceOf(String owner) throws Exception;
} 