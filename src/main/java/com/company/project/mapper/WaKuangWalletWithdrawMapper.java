package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangWalletWithdraw;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangWalletWithdrawMapper extends BaseMapper<WaKuangWalletWithdraw> {
    @Select("SELECT IFNULL(SUM(balance),0) FROM wa_kuang_wallet_withdraw WHERE `status`=1  and (coin='DOMI' or coin='USDT-BEP20')")
    BigDecimal getBalanceOfDomiAndUsdt();
}