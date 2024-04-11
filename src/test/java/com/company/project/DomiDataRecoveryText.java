package com.company.project;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.WaKuangZy;
import com.company.project.mapper.WaKuangJlMapper;
import com.company.project.mapper.WaKuangWalletMapper;
import com.company.project.mapper.WaKuangZyMapper;
import com.company.project.service.WaKuangJlService;
import com.company.project.service.WaKuangWalletService;
import com.company.project.service.WaKuangZyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DomiDataRecoveryText {

    @Resource
    private WaKuangWalletService waKuangWalletService;
    @Resource
    private WaKuangZyService waKuangZyService;


    /**
     * 用于将奖励记录中Domi币种对应质押记录状态改为2，此操作只能执行一次
     */
    @Test

    public void run() {
        LambdaQueryWrapper<WaKuangWallet> lqw = new LambdaQueryWrapper<>();
        lqw.eq(WaKuangWallet::getCoin, "Domi");
        List<WaKuangWallet> waKuangWallets = waKuangWalletService.list(lqw);
        List<WaKuangZy> updateList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(waKuangWallets)) {
            waKuangWallets.forEach(waKuangWallet -> {
                //需要追回的金额
                BigDecimal balance = waKuangWallet.getYsBalance().subtract(waKuangWallet.getYsedBalance());
                LambdaQueryWrapper<WaKuangZy> zyLQW = new LambdaQueryWrapper<>();
                zyLQW.eq(WaKuangZy::getCoin, waKuangWallet.getCoin())
                        .eq(WaKuangZy::getUserId, waKuangWallet.getUserName())
                        .eq(WaKuangZy::getIsReturn, 0).orderByDesc(WaKuangZy::getCreateTime);
                List<WaKuangZy> waKuangZyList = waKuangZyService.list(zyLQW);
                if (CollectionUtil.isNotEmpty(waKuangZyList)) {
                    for (WaKuangZy waKuangZy : waKuangZyList) {
                        if (balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(waKuangZy.getZyBalance()) > -1) {
                            waKuangZy.setStatus(2);
                            updateList.add(waKuangZy);
                            balance = balance.subtract(waKuangZy.getZyBalance());
                        }
                        if (balance.compareTo(BigDecimal.ZERO) == 0)
                            continue;
                    }
                }
            });
        }
        if (updateList.size()>0)
            waKuangZyService.updateBatchById(updateList);
    }
}
