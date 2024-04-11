package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.Wallet;
import com.company.project.entity.WalletWithdraw;
import com.company.project.service.WalletService;
import com.company.project.service.WalletWithdrawService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/withdraw")
@Slf4j
public class WalletWithdrawController {

    @Resource
    private WalletWithdrawService  walletWithdrawService;

    @Resource
    private WalletService walletService;


    @PostMapping("/list")
    @ApiOperation(value = "分页获取信息表接口")
//    @LogAnnotation(title = "信息管理", action = "分页获取信息列表")
    public DataResult pageInfo(@RequestBody WalletWithdraw vo) {
        return DataResult.success(walletWithdrawService.pageInfo(vo)
        );
    }


    @PostMapping("/listAdmin")
    @ApiOperation(value = "分页获取信息表接口")
//    @LogAnnotation(title = "信息管理", action = "分页获取信息列表")
    public DataResult pageInfoAdmin(@RequestBody WalletWithdraw vo) {
        return DataResult.success(walletWithdrawService.pageInfoAdmin(vo)
        );
    }



    @PostMapping("/add")
    @LogAnnotation(title = "钱包管理", action = "新增钱包")
    public DataResult add(@RequestBody WalletWithdraw walletWithdraw) throws Exception {
        BigDecimal gasFei = new BigDecimal(0.0009);
        if (walletWithdraw.getCoin()==""){
            return DataResult.fail("币种不能为空");
        }
//        if (!(walletWithdraw.getCoin().equals("USDT-BEP20")||walletWithdraw.getCoin().equals("MECT"))){
//            return DataResult.fail("该币种不支持提现");
//        }
        if (walletWithdraw.getUserId()==""){
            return DataResult.fail("用户Id不能为空");
        }

        Wallet wallet = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,walletWithdraw.getUserId()).eq(Wallet::getCoin,walletWithdraw.getCoin()));

        if(wallet.getRpBalance()==null){
            wallet.setRpBalance(new BigDecimal(0));
        }

        if(wallet.getBalance().add(wallet.getRpBalance()).compareTo(walletWithdraw.getBalance())==-1){
            return DataResult.fail("The withdrawal amount is greater than the balance");
        }

        // 如果是币安链的，查询下gas费
        if (walletWithdraw.getCoin().equals("USDT-BEP20")){
            Wallet bnbWallet = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,walletWithdraw.getUserId()).eq(Wallet::getCoin,"BNB"));
            if(bnbWallet.getBalance().compareTo(gasFei)==-1){
                return DataResult.fail("Insufficient GAS fee, please recharge at least 0.001 BNB");
            }
        }else if (walletWithdraw.getCoin().equals("MECT")){
            Wallet bnbWallet = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,walletWithdraw.getUserId()).eq(Wallet::getCoin,"BNB"));
            if(bnbWallet.getBalance().compareTo(gasFei)==-1){
                return DataResult.fail("Insufficient GAS fee, please recharge at least 0.001 BNB");
            }
        } else if (walletWithdraw.getCoin().equals("BNB")){
            if(wallet.getBalance().compareTo(walletWithdraw.getBalance().add(gasFei))==-1){
                return DataResult.fail("Insufficient GAS fee, please recharge at least 0.001 BNB");
            }
        }else{
            return DataResult.fail("Withdrawal is not supported for this currency");
        }

        walletWithdraw.setStatus(0);
        walletWithdraw.setSxfBalance(new BigDecimal(0));
        walletWithdrawService.saveTr(walletWithdraw,wallet);
        return DataResult.success();
    }

    @GetMapping("/updateStatusPasss/{id}")
    @LogAnnotation(title = "钱包管理", action = "新增钱包")
    public DataResult updateStatusPasss(@PathVariable("id") String id)  {
        WalletWithdraw walletWithdraw = walletWithdrawService.getById(id);
        if (walletWithdraw.getStatus()!=0){
            return DataResult.fail("只有待审核的记录可以操作审核");
        }
        walletWithdraw.setStatus(1);

        Wallet wallet = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,walletWithdraw.getUserId()).eq(Wallet::getCoin,walletWithdraw.getCoin()));
        wallet.setCzBalance(wallet.getBalance().subtract(walletWithdraw.getBalance().add(walletWithdraw.getSxfBalance())));
        walletService.updateWallet(wallet);
        return DataResult.success(walletWithdrawService.updateById(walletWithdraw));
    }

    @GetMapping("/updateStatusNoPasss/{id}")
    @LogAnnotation(title = "钱包管理", action = "新增钱包")
    public DataResult updateStatusNoPasss(@PathVariable("id") String id)  {
        WalletWithdraw walletWithdraw = walletWithdrawService.getById(id);
        if (walletWithdraw.getStatus()!=0){
            return DataResult.fail("只有待审核的记录可以操作审核");
        }
        if (walletWithdraw.getRemark()==""){
            return DataResult.fail("审核内容不能为空");
        }
        walletWithdraw.setStatus(2);

        // 修改钱包金额
        Wallet wallet = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,walletWithdraw.getUserId()).eq(Wallet::getCoin,walletWithdraw.getCoin()));
        wallet.setBalance(wallet.getBalance().add(walletWithdraw.getBalance()).add(walletWithdraw.getSxfBalance()));
        wallet.setUpdateTime(new Date());
        walletService.updateWallet(wallet);
        return DataResult.success(walletWithdrawService.updateById(walletWithdraw));
    }

}
