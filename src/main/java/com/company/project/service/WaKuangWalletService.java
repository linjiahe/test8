package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangWalletService extends IService<WaKuangWallet> {


    IPage<WaKuangWallet> pageInfo(WaKuangWallet vo);

    void pageInfoExport(WaKuangWallet wakuangWallet, HttpServletResponse response) throws IOException;

    IPage<WaKuangWallet> pageInfoGroupByUser(WaKuangWallet vo);

    List<WaKuangWallet> List(WaKuangWallet vo);

    List<WaKuangWallet> ListByCoin(String coin);

    List<WaKuangWallet> ListByUser(WaKuangWallet vo);

    List<WaKuangWallet> ListByCoinLimit(String coin,int limit);

    void addWallet(WaKuangWallet vo);

    void shandui(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangShandui vo);

    void shanduiJlDh(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangShandui vo);

    void zhuanzhang(WaKuangWallet fromWallet, WaKuangWallet toWallet, WaKuangZhuanzhang vo);

    void zhuanzhangKzy(WaKuangWallet fromWallet, WaKuangWallet toWallet, WaKuangZhuanzhang vo);

    void zhuanzhangByDappJltx(WaKuangWallet fromWallet, WaKuangWallet toWallet, WaKuangZhuanzhang vo);

    void zhuanzhangByDappJldh(WaKuangWallet fromWallet, WaKuangWallet toWallet, WaKuangZhuanzhang vo);

    void zhuanzhangJl(WaKuangWallet fromWallet, WaKuangWallet toWallet, WaKuangJlZhuanzhang vo);

    void updateWallet(WaKuangWallet wallet);

    boolean addBalance(String userId, String coin, BigDecimal balance);

    boolean subBalance(String userId, String coin, BigDecimal balance);

    boolean addJlTxBalance(String userId, String coin, BigDecimal balance);

    boolean addKjBalance(String userId, String coin, BigDecimal balance);

    boolean subJlTxBalance(String userId, String coin, BigDecimal balance);

    boolean addJlDhBalance(String userId, String coin, BigDecimal balance);

    boolean subJlDhBalance(String userId, String coin, BigDecimal balance);

    boolean addZtBalance(String userId, String coin, BigDecimal balance);

    boolean subZtBalance(String userId, String coin, BigDecimal balance);

    boolean addJlBalance(String userId, String coin, BigDecimal balance);

    boolean subJlBalance(String userId, String coin, BigDecimal balance);

    boolean addZyjlBalance(String userId, String coin, BigDecimal balance);

    boolean subZyjlBalance(String userId, String coin, BigDecimal balance);

    boolean returnZyBalance(String userId, String coin, BigDecimal balance);

    boolean withdraw(WaKuangJlWithdraw waKuangJlWithdraw);

    boolean withdraw2(WaKuangJlWithdrawVO vo);

    boolean jlShandui(String userId, BigDecimal balance);

    boolean jlShandui(WaKuangShanduiVO vo);

    boolean resetTdjlBalance();

    DataResult czBep20(WaKuangWallet dto);

    boolean setType(String userId, Integer type);

    boolean setLevel(String userId, Integer level);

    void resetLevel();

    int setTotailJtjlZyBalance(String userId, BigDecimal xjzyBalance, BigDecimal xjsyBalance);

    Boolean updateLevelBatch(ArrayList<WaKuangWallet> waKuangWalletList);


}
