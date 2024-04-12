package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangWallet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangWalletMapper extends BaseMapper<WaKuangWallet> {

    @Update("update wa_kuang_wallet set balance=balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set balance=balance - #{balance} where user_name=#{userId} and coin=#{coin} and balance >= #{balance}")
    int subBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set balance=balance - #{balance},dj_balance=dj_balance+#{balance} where user_name=#{userId} and coin=#{coin} and balance >= #{balance}")
    int subBalanceAndDjBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Select("SELECT \n" +
            "wkw.id,\n" +
            "wkw.invitation_code,\n" +
            "wkw.`code`,\n" +
            "wkw.user_name,\n" +
            "wkw.coin,\n" +
            "IFNULL(SUM(IFNULL(wkzy.zy_balance,0)),0)balance,\n" +
            "wkw.address,\n" +
            "wkw.private_key\n" +
            " \n" +
            "FROM wa_kuang_wallet wkw \n" +
            "LEFT JOIN wa_kuang_zy wkzy on wkw.`code`=wkzy.user_id and wkzy.coin='USDT-BEP20'\n" +
            "WHERE wkw.coin='USDT-BEP20'\n" +
            "GROUP BY wkw.`code`")
    List<WaKuangWallet> selectPerformance();
}