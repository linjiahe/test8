package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.WaKuangJlWithdraw;
import com.company.project.entity.WaKuangWithdrawJl;
import com.company.project.service.WaKuangJlWithdrawService;
import com.company.project.service.WaKuangWalletService;
import com.company.project.service.WaKuangWithdrawJlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/wakuang/jl/withdraw")
@Slf4j
public class WaKuangJlWithdrawController {

    @Resource
    private WaKuangJlWithdrawService waKuangJlWithdrawService;

    @Resource
    private WaKuangWithdrawJlService waKuangWithdrawJlService;

    @Resource
    private WaKuangWalletService waKuangWalletService;


    @PostMapping("/list")
    public DataResult pageInfo(@RequestBody WaKuangJlWithdraw vo) {
        return DataResult.success(waKuangJlWithdrawService.pageInfo(vo)
        );
    }


    @PostMapping("/listAdmin")
    public DataResult pageInfoAdmin(@RequestBody WaKuangJlWithdraw vo) {
        return DataResult.success(waKuangJlWithdrawService.pageInfoAdmin(vo)
        );
    }


    /**
     * 审核通过
     * @param id
     * @return
     */
    @GetMapping("/updateStatusPasss/{id}")
    public DataResult updateStatusPasss(@PathVariable("id") String id)  {
        WaKuangJlWithdraw waKuangWalletWithdraw = waKuangJlWithdrawService.getById(id);
        if (waKuangWalletWithdraw.getStatus()!=0){
            return DataResult.fail("只有待审核的记录可以操作审核");
        }
        waKuangWalletWithdraw.setStatus(1);
        return DataResult.success(waKuangJlWithdrawService.updateById(waKuangWalletWithdraw));
    }

    /**
     * 审核不通过
     * @param id
     * @return
     */
    @GetMapping("/updateStatusNoPasss/{id}")
    public DataResult updateStatusNoPasss(@PathVariable("id") String id)  {
        WaKuangJlWithdraw waKuangJlWithdraw = waKuangJlWithdrawService.getById(id);
        if (waKuangJlWithdraw.getStatus()!=0){
            return DataResult.fail("只有待审核的记录可以操作审核");
        }
        if (waKuangJlWithdraw.getRemark()==""){
            return DataResult.fail("审核内容不能为空");
        }
        waKuangJlWithdraw.setStatus(2);

        List<WaKuangWithdrawJl> jlList = waKuangWithdrawJlService.list(Wrappers.<WaKuangWithdrawJl>lambdaQuery()
                .eq(WaKuangWithdrawJl::getCreateTime, waKuangJlWithdraw.getCreateTime())
        );

        boolean result = waKuangJlWithdrawService.updateById(waKuangJlWithdraw);

        // 回退奖励金额
        if (result) {
            waKuangWalletService.addJlBalance(waKuangJlWithdraw.getUserId(), "Domi", jlList.stream()
                    .filter(v -> v.getSourceType() == 1)
                    .map(WaKuangWithdrawJl::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            waKuangWalletService.addZtBalance(waKuangJlWithdraw.getUserId(), "Domi", jlList.stream()
                    .filter(v -> v.getSourceType() == 2)
                    .map(WaKuangWithdrawJl::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            waKuangWalletService.addZyjlBalance(waKuangJlWithdraw.getUserId(), "Domi", jlList.stream()
                    .filter(v -> v.getSourceType() == 3)
                    .map(WaKuangWithdrawJl::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        return DataResult.success(result);
    }

}
