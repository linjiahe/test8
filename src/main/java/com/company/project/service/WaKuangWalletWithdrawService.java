package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangWalletWithdraw;
import com.company.project.entity.WaKuangWallet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangWalletWithdrawService extends IService<WaKuangWalletWithdraw> {


    IPage<WaKuangWalletWithdraw> pageInfo(WaKuangWalletWithdraw vo);
    void listExport(WaKuangWalletWithdraw vo, HttpServletResponse response) throws IOException;

    IPage<WaKuangWalletWithdraw> pageInfoAdmin(WaKuangWalletWithdraw vo);

    void saveTr(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet wakuangWallet);

    void saveTrByDapp(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet wakuangWallet);

    void saveTrByDappJltx(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet wakuangWallet);

    void saveTrByDappJldh(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet wakuangWallet);

    void saveTrByDappByYingshe(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet wakuangWallet);


    BigDecimal getBalanceOfDomiAndUsdt(String userId);
}
