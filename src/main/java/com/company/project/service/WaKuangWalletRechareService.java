package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangWalletRecharge;
import com.company.project.entity.WaKuangWalletRecharge;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangWalletRechareService extends IService<WaKuangWalletRecharge> {


    IPage<WaKuangWalletRecharge> pageInfo(WaKuangWalletRecharge vo);

    void pageInfoExport(WaKuangWalletRecharge vo, HttpServletResponse response) throws IOException;

    void addWallet(WaKuangWalletRecharge vo);

    void updateWallet(WaKuangWalletRecharge walletRecharge);


}
