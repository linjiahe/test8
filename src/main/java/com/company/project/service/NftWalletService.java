package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.GonghuiUser;
import com.company.project.entity.NftWallet;
import com.company.project.entity.NftWalletDTO;
import com.company.project.entity.Wallet;

import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface NftWalletService extends IService<NftWallet> {


    IPage<NftWallet> pageInfo(NftWalletDTO vo);

    List<NftWallet> findList(NftWallet vo);

}
