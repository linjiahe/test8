package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.Wallet;

import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WalletService extends IService<Wallet> {


    IPage<Wallet> pageInfo(Wallet vo);

    List<Wallet> List(Wallet vo);

    List<Wallet> ListByCoin(String coin);

    void addWallet(Wallet vo);

    void updateWallet(Wallet wallet);

}
