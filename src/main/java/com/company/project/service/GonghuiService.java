package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.Gonghui;
import com.company.project.entity.NftWallet;
import com.company.project.entity.NftWalletDTO;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface GonghuiService extends IService<Gonghui> {

    IPage<Gonghui> pageInfo(Gonghui vo);

}
