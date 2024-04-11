package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.Wallet;
import com.company.project.entity.WalletWithdraw;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WalletWithdrawService extends IService<WalletWithdraw> {


    IPage<WalletWithdraw> pageInfo(WalletWithdraw vo);

    IPage<WalletWithdraw> pageInfoAdmin(WalletWithdraw vo);

    void saveTr(WalletWithdraw walletWithdraw, Wallet wallet);
}
