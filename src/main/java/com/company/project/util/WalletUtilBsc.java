package com.company.project.util;

import com.company.project.common.job.task.wakuang.GuijiUtil;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.Wallet;
import com.google.common.collect.ImmutableList;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import sun.security.provider.SecureRandom;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class WalletUtilBsc {

    /**
     * 连接web3 节点
     */
//    private final static Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/56e562d096e04d85ba6efd369fb536c8"));
    private final static Web3j web3j = Web3j.build(new HttpService("https://bsc-dataseed.binance.org"));

    /**
     * path路径
     */
    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

    /**
     * 获取ERC-20 token指定地址余额
     *
     * @param address         查询地址
     * @param contractAddress 合约地址
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static BigDecimal getERC20Balance( String address, String contractAddress) throws ExecutionException, InterruptedException {
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address fromAddress = new Address(address);
        inputParameters.add(fromAddress);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(address, contractAddress, data);

        EthCall ethCall;
        BigDecimal balanceValue = BigDecimal.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            if(results != null && results.size()>0){
                BigDecimal divideApiBalacne = new BigDecimal("1000000000000000000");
                balanceValue = new BigDecimal(results.get(0).getValue().toString());
                return balanceValue.divide(divideApiBalacne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }

    /**
     * 取余额
     *
     *
     * @param address 传入查询的地址
     *
     * @return String 余额
     * @throws IOException
     */
    public static BigDecimal getBalance(String address) throws IOException {

        EthGetBalance ethGetBlance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        // 格式转换 WEI(币种单位) --> ETHER
        String balance = Convert.fromWei(new BigDecimal(ethGetBlance.getBalance()), Convert.Unit.ETHER).toPlainString();

        return new BigDecimal(balance);
    }

    /**
     * 基础转账
     * @return
     * @throws Exception
     */
    public static void transfer(String to, String value, String privateKey) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, credentials, to, new BigDecimal(value), Convert.Unit.ETHER).send();
        System.out.println(transactionReceipt);
    }


    /**
     * token转账
     * @param from
     * @param to
     * @param value
     * @param privateKey
     * @param contractAddress
     * @param decimal
     * @return
     */
    public static String tokenDeal(String from, String to, String value, String privateKey, String contractAddress, int decimal) {
        try {
            //转账的凭证，需要传入私钥
            Credentials credentials = Credentials.create(privateKey);
            //获取交易笔数
            BigInteger nonce;
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send();
            if (ethGetTransactionCount == null) {
                return null;
            }
            nonce = ethGetTransactionCount.getTransactionCount();
            //手续费
            BigInteger gasPrice;
            EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
            if (ethGasPrice == null) {
                return null;
            }
            gasPrice = ethGasPrice.getGasPrice();
            //注意手续费的设置，这块很容易遇到问题
            BigInteger gasLimit = BigInteger.valueOf(60000L);

            BigInteger val = new BigDecimal(value).multiply(new BigDecimal("10").pow(decimal)).toBigInteger();// 单位换算
            Function function = new Function(
                    "transfer",
                    Arrays.asList(new Address(to), new Uint256(val)),
                    Collections.singletonList(new TypeReference<Type>() {
                    }));
            //创建交易对象
            String encodedFunction = FunctionEncoder.encode(function);
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit,
                    contractAddress, encodedFunction);

            //进行签名操作
            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signMessage);
            //发起交易
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            System.out.println("提现getError："+ethSendTransaction.getError());
            System.out.println("提现getResult："+ethSendTransaction.getResult());
            String hash = ethSendTransaction.getTransactionHash();
            return hash;
        } catch (Exception ex) {
            //报错应进行错误处理
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * token转账
     * @param from
     * @param to
     * @param value
     * @param privateKey
     * @param contractAddress
     * @param decimal
     * @return
     */
    public static String tokenDealGasLimit(String from, String to, String value, String privateKey, String contractAddress, int decimal,Long gasLimitDto) {
        try {
            //转账的凭证，需要传入私钥
            Credentials credentials = Credentials.create(privateKey);
            //获取交易笔数
            BigInteger nonce;
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send();
            if (ethGetTransactionCount == null) {
                return null;
            }
            nonce = ethGetTransactionCount.getTransactionCount();
            //手续费
            BigInteger gasPrice;
            EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
            if (ethGasPrice == null) {
                return null;
            }
            gasPrice = ethGasPrice.getGasPrice();
            //注意手续费的设置，这块很容易遇到问题
            BigInteger gasLimit = BigInteger.valueOf(gasLimitDto);

            BigInteger val = new BigDecimal(value).multiply(new BigDecimal("10").pow(decimal)).toBigInteger();// 单位换算
            Function function = new Function(
                    "transfer",
                    Arrays.asList(new Address(to), new Uint256(val)),
                    Collections.singletonList(new TypeReference<Type>() {
                    }));
            //创建交易对象
            String encodedFunction = FunctionEncoder.encode(function);
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit,
                    contractAddress, encodedFunction);

            //进行签名操作
            byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signMessage);
            //发起交易
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            String hash = ethSendTransaction.getTransactionHash();
            return hash;
        } catch (Exception ex) {
            //报错应进行错误处理
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 创建钱包
     * @throws MnemonicException.MnemonicLengthException
     */
    public static Wallet createWallet()  throws MnemonicException.MnemonicLengthException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
        secureRandom.engineNextBytes(entropy);

        //生成12位助记词
        List<String> str = MnemonicCode.INSTANCE.toMnemonic(entropy);

        //使用助记词生成钱包种子
        byte[] seed = MnemonicCode.toSeed(str, "");
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
        DeterministicKey deterministicKey = deterministicHierarchy
                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
        byte[] bytes = deterministicKey.getPrivKeyBytes();
        ECKeyPair keyPair = ECKeyPair.create(bytes);
        //通过公钥生成钱包地址
        String address = Keys.getAddress(keyPair.getPublicKey());

//        System.out.println();
//        System.out.println("助记词：");
//        System.out.println(str);
//        System.out.println();
//        System.out.println("地址：");
//        System.out.println("0x"+address);
//        System.out.println();
//        System.out.println("私钥：");
//        System.out.println("0x"+keyPair.getPrivateKey().toString(16));
//        System.out.println();
//        System.out.println("公钥：");
//        System.out.println(keyPair.getPublicKey().toString(16));
        Wallet wallet = new Wallet();
        wallet.setAddress("0x"+address);
        wallet.setPrivateKey("0x"+keyPair.getPrivateKey().toString(16));
//        wallet.setPublicKey(keyPair.getPublicKey().toString(16));
        return wallet;
    }

    /**
     * 创建钱包
     * @throws MnemonicException.MnemonicLengthException
     */
    public static WaKuangWallet createWaKuangWallet()  throws MnemonicException.MnemonicLengthException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
        secureRandom.engineNextBytes(entropy);

        //生成12位助记词
        List<String> str = MnemonicCode.INSTANCE.toMnemonic(entropy);

        //使用助记词生成钱包种子
        byte[] seed = MnemonicCode.toSeed(str, "");
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
        DeterministicKey deterministicKey = deterministicHierarchy
                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
        byte[] bytes = deterministicKey.getPrivKeyBytes();
        ECKeyPair keyPair = ECKeyPair.create(bytes);
        //通过公钥生成钱包地址
        String address = Keys.getAddress(keyPair.getPublicKey());

//        System.out.println();
//        System.out.println("助记词：");
//        System.out.println(str);
//        System.out.println();
//        System.out.println("地址：");
//        System.out.println("0x"+address);
//        System.out.println();
//        System.out.println("私钥：");
//        System.out.println("0x"+keyPair.getPrivateKey().toString(16));
//        System.out.println();
//        System.out.println("公钥：");
//        System.out.println(keyPair.getPublicKey().toString(16));
        WaKuangWallet wallet = new WaKuangWallet();
        wallet.setAddress("0x"+address);
        wallet.setPrivateKey("0x"+keyPair.getPrivateKey().toString(16));
//        wallet.setPublicKey(keyPair.getPublicKey().toString(16));
        return wallet;
    }

}
