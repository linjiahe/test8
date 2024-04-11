package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.NewWallet;
import com.company.project.entity.UsdtBsc;
import com.company.project.entity.Wallet;

import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface UsdtBscService extends IService<UsdtBsc> {

    Wallet getAddress();

    NewWallet getNewAddress();

    IPage<UsdtBsc> pageInfo(UsdtBsc vo);

    List<UsdtBsc> List(UsdtBsc vo);

    List<UsdtBsc> ListByCoin(String coin);

    void addWallet(UsdtBsc vo);

    void addList(List<UsdtBsc> vo);

    void updateWallet(UsdtBsc wallet);

}
