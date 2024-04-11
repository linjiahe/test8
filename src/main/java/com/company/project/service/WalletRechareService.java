package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WalletRecharge;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WalletRechareService extends IService<WalletRecharge> {


    IPage<WalletRecharge> pageInfo(WalletRecharge vo);

    void addWallet(WalletRecharge vo);

    void updateWallet(WalletRecharge walletRecharge);

}
