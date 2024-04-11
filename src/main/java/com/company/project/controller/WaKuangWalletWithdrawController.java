package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.WaKuangWalletWithdraw;
import com.company.project.service.WaKuangJlService;
import com.company.project.service.WaKuangWalletService;
import com.company.project.service.WaKuangWalletWithdrawService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/wakuang/wallet/withdraw")
@Slf4j
public class WaKuangWalletWithdrawController {

    @Resource
    private WaKuangWalletWithdrawService waKuangWalletWithdrawService;

    @Resource
    private WaKuangWalletService waKuangWalletService;

    @Resource
    private WaKuangJlService waKuangJlService;


    @PostMapping("/list")
    public DataResult pageInfo(@RequestBody WaKuangWalletWithdraw vo) {
        return DataResult.success(waKuangWalletWithdrawService.pageInfo(vo)
        );
    }
    @ApiOperation("提现记录导出")
    @PostMapping("/listExport")
    public void listExport(@RequestBody WaKuangWalletWithdraw vo, HttpServletResponse response) throws IOException {
        waKuangWalletWithdrawService.listExport(vo,response);
    }


    @PostMapping("/listAdmin")
    public DataResult pageInfoAdmin(@RequestBody WaKuangWalletWithdraw vo) {
        return DataResult.success(waKuangWalletWithdrawService.pageInfoAdmin(vo)
        );
    }



    /**
     * 审核通过
     * @return
     */
    @PostMapping("/updateStatusPasss")
    @Transactional(rollbackFor = Exception.class)
    public DataResult updateStatusPasss(@RequestBody Map<String,String> map)  {
        String id = map.get("id");
        int status = Integer.parseInt(map.get("status"));
        WaKuangWalletWithdraw waKuangWalletWithdraw = waKuangWalletWithdrawService.getById(id);
        waKuangWalletWithdraw.setStatus(status);
        int sanxingStatus=1;
        // 不参与三星节点分红
        if (waKuangWalletWithdraw.getCoin().equals("Domi")) {
            sanxingStatus = 0;
        }
        waKuangWalletWithdraw.setSanxingStatus(sanxingStatus);
        boolean result = waKuangWalletWithdrawService.updateById(waKuangWalletWithdraw);
        if (result) {
            WaKuangWallet waKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName,waKuangWalletWithdraw.getUserId()).eq(WaKuangWallet::getCoin,waKuangWalletWithdraw.getCoin()));
            waKuangWallet.setDjBalance(waKuangWallet.getDjBalance().subtract(waKuangWalletWithdraw.getBalance()));
            waKuangWalletService.updateWallet(waKuangWallet);
        }
        return DataResult.success(result);
    }

    /**
     * 审核不通过
     * @return
     */
    @PostMapping("/updateStatusNoPasss")
    public DataResult updateStatusNoPasss(@RequestBody Map<String,String> map)  {
        String id = map.get("id");
        int status = Integer.parseInt(map.get("status"));
        WaKuangWalletWithdraw waKuangWalletWithdraw = waKuangWalletWithdrawService.getById(id);
        if (waKuangWalletWithdraw.getRemark()==""){
            return DataResult.fail("审核内容不能为空");
        }
        waKuangWalletWithdraw.setStatus(status);
        if (status==2){
            // 修改钱包金额
            WaKuangWallet waKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName,waKuangWalletWithdraw.getUserId()).eq(WaKuangWallet::getCoin,waKuangWalletWithdraw.getCoin()));
            waKuangWallet.setBalance(waKuangWallet.getBalance().add(waKuangWalletWithdraw.getBalance()).add(waKuangWalletWithdraw.getSxfBalance()));
            waKuangWallet.setDjBalance(waKuangWallet.getDjBalance().subtract(waKuangWalletWithdraw.getBalance()));
            waKuangWallet.setUpdateTime(new Date());
            waKuangWalletService.updateWallet(waKuangWallet);
        }else if (status==5){
            // 修改钱包金额
            WaKuangWallet waKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName,waKuangWalletWithdraw.getUserId()).eq(WaKuangWallet::getCoin,waKuangWalletWithdraw.getCoin()));
            waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().add(waKuangWalletWithdraw.getBalance()).add(waKuangWalletWithdraw.getSxfBalance()));
            waKuangWallet.setDjBalance(waKuangWallet.getDjBalance().subtract(waKuangWalletWithdraw.getBalance()));
            waKuangWallet.setUpdateTime(new Date());
            waKuangWalletService.updateWallet(waKuangWallet);
        }else if (status==8){
            // 修改钱包金额
            WaKuangWallet waKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName,waKuangWalletWithdraw.getUserId()).eq(WaKuangWallet::getCoin,waKuangWalletWithdraw.getCoin()));
            waKuangWallet.setJldhBalance(waKuangWallet.getJldhBalance().add(waKuangWalletWithdraw.getBalance()).add(waKuangWalletWithdraw.getSxfBalance()));
            waKuangWallet.setDjBalance(waKuangWallet.getDjBalance().subtract(waKuangWalletWithdraw.getBalance()));
            waKuangWallet.setUpdateTime(new Date());
            waKuangWalletService.updateWallet(waKuangWallet);
        }

        return DataResult.success(waKuangWalletWithdrawService.updateById(waKuangWalletWithdraw));
    }




    @ApiOperation("获取已提现domi金额")
    @GetMapping("/getBalanceOfDomiAndUsdt/{userId}")
    public DataResult getBalanceOfDomiAndUsdt(@PathVariable("userId")String userId){
        return DataResult.success(waKuangWalletWithdrawService.getBalanceOfDomiAndUsdt(userId));
    }

}
