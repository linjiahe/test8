package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.NewWallet;

import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface NewWalletService extends IService<NewWallet> {


    IPage<NewWallet> pageInfo(NewWallet vo);

    List<NewWallet> List(NewWallet vo);

    List<NewWallet> ListByCoin(String coin);

    void addWallet(NewWallet vo);

    void updateWallet(NewWallet wallet);

}
