package top.heyqing.otter.contract;

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public interface TokenContract {
    TransactionReceipt transfer(String to, BigInteger amount) throws Exception;
    BigInteger balanceOf(String owner) throws Exception;
    String name() throws Exception;
    String symbol() throws Exception;
    BigInteger totalSupply() throws Exception;
} 