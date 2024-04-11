//package com.company.project.util;
//
//import com.google.common.collect.ImmutableList;
//import org.bitcoinj.crypto.*;
//import com.company.project.entity.Wallet;
//import org.bitcoinj.wallet.DeterministicSeed;
//import org.web3j.abi.FunctionEncoder;
//import org.web3j.abi.TypeReference;
//import org.web3j.abi.datatypes.Function;
//import org.web3j.abi.datatypes.Type;
//import org.web3j.crypto.*;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//import sun.security.provider.SecureRandom;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//
//public class WalletUtil {
//
//    /**
//     * 连接web3 节点
//     */
//    private final static Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/v3/56e562d096e04d85ba6efd369fb536c8"));
//
//    /**
//     * path路径
//     */
//    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
//            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
//                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);
//
//
//
//    /**
//     * 创建钱包
//     * @throws MnemonicException.MnemonicLengthException
//     */
//    public static Wallet createWallet()  throws MnemonicException.MnemonicLengthException {
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
//        secureRandom.engineNextBytes(entropy);
//
//        //生成12位助记词
//        List<String> str = MnemonicCode.INSTANCE.toMnemonic(entropy);
//
//        //使用助记词生成钱包种子
//        byte[] seed = MnemonicCode.toSeed(str, "");
//        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
//        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
//        DeterministicKey deterministicKey = deterministicHierarchy
//                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
//        byte[] bytes = deterministicKey.getPrivKeyBytes();
//        ECKeyPair keyPair = ECKeyPair.create(bytes);
//        //通过公钥生成钱包地址
//        String address = Keys.getAddress(keyPair.getPublicKey());
//
////        System.out.println();
////        System.out.println("助记词：");
////        System.out.println(str);
////        System.out.println();
////        System.out.println("地址：");
////        System.out.println("0x"+address);
////        System.out.println();
////        System.out.println("私钥：");
////        System.out.println("0x"+keyPair.getPrivateKey().toString(16));
////        System.out.println();
////        System.out.println("公钥：");
////        System.out.println(keyPair.getPublicKey().toString(16));
//        Wallet wallet = new Wallet();
//        wallet.setAddress("0x"+address);
//        wallet.setPrivateKey("0x"+keyPair.getPrivateKey().toString(16));
////        wallet.setPublicKey(keyPair.getPublicKey().toString(16));
//        return wallet;
//    }
//
//    /**
//     * 创建钱包
//     * @throws MnemonicException.MnemonicLengthException
//     */
//    public static Wallet createWalletT(int i)  throws MnemonicException.MnemonicLengthException {
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
//        secureRandom.engineNextBytes(entropy);
//
//        //生成12位助记词
//        List<String> str = MnemonicCode.INSTANCE.toMnemonic(entropy);
//
//        //使用助记词生成钱包种子
//        byte[] seed = MnemonicCode.toSeed(str, "");
//        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
//        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
//        DeterministicKey deterministicKey = deterministicHierarchy
//                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
//        byte[] bytes = deterministicKey.getPrivKeyBytes();
//        ECKeyPair keyPair = ECKeyPair.create(bytes);
//        //通过公钥生成钱包地址
//        String address = Keys.getAddress(keyPair.getPublicKey());
//
////        System.out.println();
////        System.out.println("助记词：");
////        System.out.println(str);
////        System.out.println();
////        System.out.println("地址"+i+"：");
////        System.out.println("0x"+address);
////        System.out.println("私钥"+i+"：");
////        System.out.println("0x"+keyPair.getPrivateKey().toString(16));
////        System.out.println();
////        System.out.println("公钥：");
////        System.out.println(keyPair.getPublicKey().toString(16));
//        Wallet wallet = new Wallet();
//        wallet.setAddress("0x"+address);
//        wallet.setPrivateKey("0x"+keyPair.getPrivateKey().toString(16));
////        wallet.setPublicKey(keyPair.getPublicKey().toString(16));
//        return wallet;
//    }
//
//    /**
//     * ETH转账
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public static void signETHTransaction() throws IOException, ExecutionException, InterruptedException {
//
//        //发送方地址
//        String from = "0x5C1C271dc5008170AbC41d01BCEf6408612EB0b1";
//        //转账数量
//        String amount = "1";
//        //接收者地址
//        String to = "0x635dc6fdca5b1ab8e0298ebbc9aec600185451e2";
//        //发送方私钥
//        String privateKey = "04f02fd3a72881270c757dd6cd4757c83d6aab08833fd4dd5c2bf4860c3dc6cd";
//        //查询地址交易编号
//        BigInteger nonce = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
//        //支付的矿工费
//        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
//        BigInteger gasLimit = new BigInteger("210000");
//
//        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//        //签名交易
//        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, amountWei, "");
//        Credentials credentials = Credentials.create(privateKey);
//        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        //广播交易
//        String hash =  web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
//
//        System.out.println("hash:"+hash);
//
//    }
//
//    /**
//     * ETH代币转账
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public static void signTokenTransaction() throws IOException, ExecutionException, InterruptedException {
//
//        //发送方地址
//        String from = "0x21985541C5e43b0eB3a343c7fbe8a609ce2CB24a";
//        //转账数量
//        String amount = "1";
//        //接收者地址
//        String to = "0xc290436b3da897115493a1547B52783c50f0Bef3";
//        //发送方私钥
//        String privateKey = "0x7c9df206b49522ce955d48b05a0689ed7a6cb4967b195eabea3a20f38cfcd27d";
//        //代币合约地址
//        String coinAddress = "0x96a02A09EFcb1c0f9deEa33B01FFb991B77Db1eA";
//        //查询地址交易编号
//        BigInteger nonce = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.PENDING).send().getTransactionCount();
//        //支付的矿工费
//        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
//        BigInteger gasLimit = new BigInteger("210000");
//
//        Credentials credentials = Credentials.create(privateKey);
//        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//
//        //封装转账交易
//        Function function = new Function(
//                "transfer",
//                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to),
//                        new org.web3j.abi.datatypes.generated.Uint256(amountWei)),
//                Collections.<TypeReference<?>>emptyList());
//        String data = FunctionEncoder.encode(function);
//        //签名交易
//        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, coinAddress, data);
//        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//        //广播交易
//        String hash = web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get().getTransactionHash();
//        System.out.println("hash:"+hash);
//
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        //创建钱包
////        createWallet();
//
////        for (int i =1;i<=1001;i++){
////            createWalletT(i);
////        }
//
//        //ETH转账交易
////        signETHTransaction();
//
//        //ETH代币转账交易
////        signTokenTransaction();
//    }
//
//}
