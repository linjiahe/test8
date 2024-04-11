package com.company.project.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.QRCodeUtil;
import com.company.project.entity.*;
import com.company.project.mapper.VersionMapper;
import com.company.project.service.DayPriceService;
import com.company.project.service.UsdtBscService;
import com.company.project.service.WalletRechareService;
import com.company.project.service.WalletService;
import com.company.project.util.WalletUtilBsc;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.crypto.MnemonicException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data")
@Slf4j
public class WalletController {

    @Resource
    private WalletService walletService;

    @Resource
    private DayPriceService dayPriceService;

    @Resource
    private WalletRechareService walletRechareService;

    @Resource
    private VersionMapper versionMapper;


    @PostMapping("/wallet")
    @ApiOperation(value = "分页获取信息表接口")
//    @LogAnnotation(title = "信息管理", action = "分页获取信息列表")
    public DataResult pageInfo(@RequestBody Wallet vo) {
        return DataResult.success(walletService.pageInfo(vo)
        );
    }

    @PostMapping("/wallet/recharge")
    @ApiOperation(value = "分页获取信息表接口")
//    @LogAnnotation(title = "信息管理", action = "分页获取信息列表")
    public DataResult rechargePageInfo(@RequestBody WalletRecharge vo) {
        return DataResult.success(walletRechareService.pageInfo(vo)
        );
    }

    @PostMapping("/wallet/list")
    public DataResult List(@RequestBody Wallet vo) {
        List<Wallet> list = walletService.List(vo);
        List<WalletDTO> reslist = new ArrayList<>();
        DayPrice dayPrice = dayPriceService.getOne(null);
        for (Wallet wallet:list
             ) {
            BigDecimal price = new BigDecimal(0.00);
            if (wallet.getCoin().equals("BNB")){
                price = dayPrice.getBnbBalance();
            }
            if (wallet.getCoin().equals("BTC")){
                price = dayPrice.getBtcBalance();
            }
            if (wallet.getCoin().equals("ETH")){
                price = dayPrice.getEthBalance();
            }
            if (wallet.getCoin().equals("USDT-BEP20")){
                price = dayPrice.getUsdtbep20Balance();
            }
            if (wallet.getCoin().equals("USDT-ERC20")){
                price = dayPrice.getUsdterc20Balance();
            }
            if (wallet.getCoin().equals("MECT")){
                price = dayPrice.getMectBalance();
            }

            if(wallet.getRpBalance()==null){
                wallet.setRpBalance(new BigDecimal(0));
            }

            WalletDTO walletDTO = new WalletDTO();
            walletDTO.setAddress(wallet.getAddress());
            walletDTO.setCoin(wallet.getCoin());
            walletDTO.setBalance(wallet.getBalance().add(wallet.getRpBalance()));
            walletDTO.setPrice(price);
            walletDTO.setUserId(wallet.getUserId());
            reslist.add(walletDTO);
        }

        return DataResult.success(reslist);
    }

    @PostMapping("/wallet/version")
    public DataResult Version() {
        return DataResult.success(versionMapper.selectList(Wrappers.<Version>lambdaQuery().orderByDesc(Version::getVersion)).get(0));
    }

    @PostMapping("/wallet/versionAdd")
    public DataResult VersionAdd(@RequestBody Version version) {
        versionMapper.insert(version);
        return DataResult.success(versionMapper.selectList(null));
    }


    @PostMapping("/wallet/sync")
    @LogAnnotation(title = "钱包管理", action = "新增钱包")
    public DataResult sync(@RequestBody Map<String,String> map) throws Exception {

        List<Wallet> list = walletService.list(Wrappers.<Wallet>lambdaQuery().groupBy(Wallet::getUserId));


        for(Wallet items:list){
            // 查出是否不一致，不一致就修改
            Wallet updateItem = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,items.getUserId()).eq(Wallet::getCoin,"BNB"));
            List<Wallet> updateRes = walletService.list(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,items.getUserId()));
            if (updateRes.size()<5||updateItem==null){
                continue;
            }
            String addressUpdate = updateItem.getAddress();
            for (Wallet item:updateRes){
                if (item.getCoin().equals("USDT-BEP20")){
                    if (!addressUpdate.equals(item.getAddress())){
                        item.setAddress(addressUpdate);
                        walletService.updateWallet(item);
                    }
                }
                if (item.getCoin().equals("MECT")){
                    if (!addressUpdate.equals(item.getAddress())){
                        item.setAddress(addressUpdate);
                        walletService.updateWallet(item);
                    }
                }
            }
        }

        return DataResult.success();
    }


    @PostMapping("/wallet/add")
    @LogAnnotation(title = "钱包管理", action = "新增钱包")
    public DataResult add(@RequestBody Map<String,String> map) throws Exception {
        String userId = map.get("userId");
        if (userId==null||userId.equals("")){
            return DataResult.fail("userId不能为空");
        }

        // 目前的币种数量
        int coinCount = 5;
        List<Wallet> res = walletService.list(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,userId));
        if (res.size()>=coinCount){
            return DataResult.success();
        }

        Wallet walletBnb = new Wallet();
        walletBnb = WalletUtilBsc.createWallet();
        // 生成的二维码的路径及名称
        String destPath = "/www/wwwroot/mect/metaspatial.io/qcImages/"+walletBnb.getAddress()+".png";
        //生成二维码
        QRCodeUtil.encode(walletBnb.getAddress(), destPath);
        walletBnb.setUserId(userId);
        walletBnb.setCoin("BNB");
        walletBnb.setBalance(new BigDecimal(0.00));
        walletService.addWallet(walletBnb);

        String bnbAddress = walletBnb.getAddress();
        String bnbPrivate = walletBnb.getPrivateKey();

        Wallet walletBsc = new Wallet();
        walletBsc.setAddress(bnbAddress);
        walletBsc.setPrivateKey(bnbPrivate);
        walletBsc.setUserId(userId);
        walletBsc.setCoin("USDT-BEP20");
        walletBsc.setBalance(new BigDecimal(0.00));
        walletService.addWallet(walletBsc);

        Wallet walletEth = new Wallet();
        try {
            walletEth = WalletUtilBsc.createWallet();
        } catch (MnemonicException.MnemonicLengthException e) {
            e.printStackTrace();
        }
        String ethAddress = walletEth.getAddress();
        String ethPrivate = walletEth.getPrivateKey();
        // 生成的二维码的路径及名称
        destPath = "/www/wwwroot/mect/metaspatial.io/qcImages/"+ethAddress+".png";
        //生成二维码
        QRCodeUtil.encode(ethAddress, destPath);
        walletEth.setUserId(userId);
        walletEth.setCoin("ETH");
        walletEth.setBalance(new BigDecimal(0.00));
        walletService.addWallet(walletEth);


        Wallet walletErc20 = new Wallet();
        walletErc20.setAddress(ethAddress);
        walletErc20.setPrivateKey(ethPrivate);
        walletErc20.setUserId(userId);
        walletErc20.setCoin("USDT-ERC20");
        walletErc20.setBalance(new BigDecimal(0.00));
        walletService.addWallet(walletErc20);

        Wallet walletMECT = new Wallet();
        walletMECT.setAddress(bnbAddress);
        walletMECT.setPrivateKey(bnbPrivate);
        walletMECT.setUserId(userId);
        walletMECT.setCoin("MECT");
        walletMECT.setBalance(new BigDecimal(0.00));
        walletService.addWallet(walletMECT);

        return DataResult.success();
    }


//    @PostMapping("/importExcel")
//    public void importExcel(MultipartFile file) throws Exception {
//        InputStream inputStream = file.getInputStream();
//        //new ExcelListener(userService) 传入注入的service
//        EasyExcel.read(inputStream, UsdtBsc.class, new ExcelListener(usdtBscService)).sheet(0).doRead();
//    }
}
