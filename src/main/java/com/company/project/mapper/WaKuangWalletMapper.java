package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangWallet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangWalletMapper extends BaseMapper<WaKuangWallet> {

    @Update("update wa_kuang_wallet set tdjl_balance=0 where tdjlBalance > 0")
    int resetTdjlBalance();

    @Update("update wa_kuang_wallet set usdt_zy_balance=0,jtsy_balance=0,xjzy_balance=null,xjsy_balance=null where coin='USDT-BEP20'")
    int resetJtjlZyBalance();

    @Update("update wa_kuang_wallet set xjzy_balance=#{xjzyBalance},xjsy_balance=#{xjsyBalance} where user_name=#{userId} and coin='USDT-BEP20'")
    int setTotailJtjlZyBalance(@Param("userId") String userId, @Param("xjzyBalance") BigDecimal xjzyBalance, @Param("xjsyBalance") BigDecimal xjsyBalance);

    @Update("update wa_kuang_wallet set usdt_zy_balance=#{zyBalance},jtsy_balance=#{jtsyBalance} where user_name=#{userId} and coin='USDT-BEP20'")
    int setJtjlZyBalance(@Param("userId") String userId, @Param("zyBalance") BigDecimal zyBalance, @Param("jtsyBalance") BigDecimal jtsyBalance);

    @Update("update wa_kuang_wallet set balance=balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jltx_balance=jltx_balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addJltxBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set kj_balance=kj_balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addKjBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jldh_balance=jldh_balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addJldhBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set tdjl_balance=tdjl_balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addTdjlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set balance=balance - #{balance} where user_name=#{userId} and coin=#{coin} and balance >= #{balance}")
    int subBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jltx_balance=jltx_balance - #{balance} where user_name=#{userId} and coin=#{coin} and jltx_balance >= #{balance}")
    int subJltxBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jltx_balance=jltx_balance - #{balance} where user_name=#{userId} and coin=#{coin}")
    int subJltxBalanceByReturn(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jldh_balance=jldh_balance - #{balance} where user_name=#{userId} and coin=#{coin} and jldh_balance >= #{balance}")
    int subJldhBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set balance=balance + #{balance},zy_balance=zy_balance - #{balance} where user_name=#{userId} and coin=#{coin} and zy_balance >= #{balance}")
    int returnZyBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set zt_balance=zt_balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addZtBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set zt_balance=zt_balance - #{balance} where user_name=#{userId} and coin=#{coin} and zt_balance >= #{balance}")
    int subZtBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jl_balance=jl_balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addJlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jl_balance=jl_balance - #{balance} where user_name=#{userId} and coin=#{coin} and jl_balance >= #{balance}")
    int subJlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set zyjl_balance=zyjl_balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addZyjlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set zyjl_balance=zyjl_balance - #{balance} where user_name=#{userId} and coin=#{coin} and zyjl_balance >= #{balance}")
    int subZyjlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set zyjl_balance=zyjl_balance + #{balance},balance=balance + #{balance} where user_name=#{userId} and coin=#{coin}")
    int addLpzyBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set balance=balance + #{balance}, jl_balance= jl_balance - #{balance} where user_name=#{userId} and coin=#{coin} and jl_balance >= #{balance}")
    int withdrawJlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set balance=balance + #{balance}, zt_balance= zt_balance - #{balance} where user_name=#{userId} and coin=#{coin} and zt_balance >= #{balance}")
    int withdrawZtBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set balance=balance + #{balance}, zyjl_balance= zyjl_balance - #{balance} where user_name=#{userId} and coin=#{coin} and zyjl_balance >= #{balance}")
    int withdrawZyjlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set jl_balance= jl_balance - #{balance} where user_name=#{userId} and coin=#{coin} and jl_balance >= #{balance}")
    int shanduiJlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set zt_balance= zt_balance - #{balance} where user_name=#{userId} and coin=#{coin} and zt_balance >= #{balance}")
    int shanduiZtBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set zyjl_balance= zyjl_balance - #{balance} where user_name=#{userId} and coin=#{coin} and zyjl_balance >= #{balance}")
    int shanduiZyjlBalance(@Param("userId") String userId, @Param("coin") String coin, @Param("balance") BigDecimal balance);

    @Update("update wa_kuang_wallet set `type`= #{type} where user_name=#{userId}")
    int setType(@Param("userId") String userId, @Param("type") Integer type);

    @Update("update wa_kuang_wallet set `level`= #{level} where user_name=#{userId}")
    int setLevel(@Param("userId") String userId, @Param("level") Integer level);

    @Update("update wa_kuang_wallet set `level`= 0")
    int resetLevel();

    @Update("<script> update `wa_kuang_wallet`set level = case <foreach collection=\"list\" item=\"i\" index=\"index\"> <if test=\"i.level!=null\">when user_name=#{i.userName} then #{i.level}</if> </foreach> end , update_time = case <foreach collection=\"list\" item=\"i\" index=\"index\"> <if test=\"i.updateTime!=null\">when user_name=#{i.userName} then #{i.updateTime}</if> </foreach> end,sub_num = case <foreach collection=\"list\" item=\"i\" index=\"index\"> <if test=\"i.subNum!=null\">when user_name=#{i.userName} then #{i.subNum}</if> </foreach> end,sub_pledge = case <foreach collection=\"list\" item=\"i\" index=\"index\"> <if test=\"i.subPledge!=null\">when user_name=#{i.userName} then #{i.subPledge}</if> </foreach> end where   <foreach collection=\"list\" open=\"(\" separator=\"or\" close=\")\" item=\"i\" index=\"index\">user_name like #{i.userName}</foreach> and  <foreach collection=\"list\" open=\"(\" separator=\"or\" close=\")\" item=\"i\" index=\"index\">coin like #{i.coin}</foreach></script>")
    int updateLevelBatch(ArrayList<WaKuangWallet> waKuangWalletList);

}