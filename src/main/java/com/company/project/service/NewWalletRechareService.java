package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.NewWalletRecharge;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface NewWalletRechareService extends IService<NewWalletRecharge> {


    IPage<NewWalletRecharge> pageInfo(NewWalletRecharge vo);

    void addWallet(NewWalletRecharge vo);

    void updateWallet(NewWalletRecharge walletRecharge);

}
