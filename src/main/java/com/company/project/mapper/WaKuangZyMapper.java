package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangZy;
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
public interface WaKuangZyMapper extends BaseMapper<WaKuangZy> {

    /**
     * 质押到期
     */
    @Select("select * from wa_kuang_zy where is_return=0 and DATE_ADD(create_time, INTERVAL zy_day DAY) <= now()")
    List<WaKuangZy> zydq();

    @Select("select * from wa_kuang_zy where is_return=0 and DATE_ADD(create_time, INTERVAL zy_day DAY) <= now() and coin = #{coin}")
    List<WaKuangZy> zydqByCoin(@Param("coin") String coin);

    /**
     * LP质押奖励
     */
    @Select("select zy.* from wa_kuang_zy zy where zy.is_return=0 and zy.coin = 'Cake-LP' " +
            "and DATE_ADD(zy.create_time, INTERVAL zy.zy_day DAY) > now() " +
            "and DATE_ADD(zy.create_time, INTERVAL 1 DAY) < now() " +
            "and date_format(zy.create_time, '%H%i%S') < date_format(now(), '%H%i%S') "
            + "and (select count(1) from wa_kuang_jl jl where jl.source_type=3 and jl.user_id=zy.user_id and to_days(jl.create_time) = to_days(now()) and zy.id=jl.zy_id) = 0"
    )
    List<WaKuangZy> zyjl();

    /**
     * domi质押奖励 g
     */
    @Select("select zy.* from wa_kuang_zy zy where zy.is_return=0 and zy.coin = 'DOMI+IDM'")
    List<WaKuangZy> domiIdmZyjl();

    @Select("select sum(zy_balance) from wa_kuang_zy where create_time > #{startTime} and create_time <=#{endTime} and coin = #{coin}")
    int yesterdaySumZyBalance(@Param("coin") String coin,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select ifnull(sum(zy_balance),0) from wa_kuang_zy where  coin = #{coin} and is_return=0")
    int SumZyBalance(@Param("coin") String coin);

    @Select("select ifnull(sum(zy_balance),0) from wa_kuang_zy where  create_time > #{startTime} and create_time <=#{endTime} and coin = #{coin} and coin = #{coin}")
    int SumZyBalanceByDate(@Param("coin") String coin,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select sum(zy_balance1) from wa_kuang_zy where  coin = #{coin} and is_return=0")
    int SumZyBalance1(@Param("coin") String coin);


    @Select("select sum(zy_balance1) from wa_kuang_zy where create_time > #{startTime} and create_time <=#{endTime} and coin = #{coin}")
    int yesterdaySumZyBalance1(@Param("coin") String coin,@Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * domi质押奖励
     */
    @Select("select zy.* from wa_kuang_zy zy where zy.is_return=0 and zy.coin = 'DOMI+BNB'")
    List<WaKuangZy> domiBnbZyjl();

    @Select("select zy.* from wa_kuang_zy zy where zy.is_return=0 and zy.coin = #{coin}")
    List<WaKuangZy> ZyListByCoin(@Param("coin") String coin);

    @Select("select * from wa_kuang_zy where is_return=0 and create_time > #{startTime} and create_time <=#{endTime} and coin = #{coin}")
    List<WaKuangZy> ZyListByCoinDate(@Param("coin") String coin,@Param("startTime") String startTime,@Param("endTime") String endTime);


    /**
     * domi质押奖励
     */
    @Select("select zy.* from wa_kuang_zy zy where zy.is_return=0 and zy.coin = 'Domi'")
    List<WaKuangZy> domiZyjl();

    /**
     * USDT质押奖励
     */
    @Select("select zy.* from wa_kuang_zy zy where zy.coin = 'USDT-BEP20' and zy.jl_balance > 0 and is_return=0 ")
    List<WaKuangZy> usdtZyjl();

    @Update("update wa_kuang_zy set jl_balance = jl_balance - #{jlBalance},is_return = #{isReturn} where id=#{id} and jl_balance >= #{jlBalance}")
    int subJlBalance(@Param("id") String id, @Param("jlBalance") BigDecimal jlBalance, @Param("isReturn") Integer isReturn);

    @Update("update wa_kuang_zy set jl_balance = 0,is_return = 1 where id=#{id}")
    int subJlBalance2(@Param("id") String id);

    @Update("update wa_kuang_zy set jl_balance = jl_balance - #{jlBalance} where id=#{id} and jl_balance >= #{jlBalance}")
    int subJlBalance3(@Param("id") String id, @Param("jlBalance") BigDecimal jlBalance);

    /**
     * 获取用户已质押个数
     */
    @Select("select ifnull(sum(zy_balance),0) from wa_kuang_zy where is_return=0 and coin='Cake-LP' and user_id=#{userId}")
    BigDecimal getZyNumByUserId(@Param("userId") String userId);

    /**
     * 查用户USDT质押收益
     */
    @Select("select ifnull(sum(" +
            "zy.zy_balance * (case when zy.zy_balance > 10000 then 0.015 when zy.zy_balance > 5000 then 0.013 when zy.zy_balance > 2000 then 0.012 when zy.zy_balance > 1000 then 0.011 when zy.zy_balance >= 100 then 0.01 else 0 end)" +
            "),0) from wa_kuang_zy zy where zy.user_id = #{userId} and zy.is_return=0 and zy.coin = 'USDT-BEP20'")
    BigDecimal zysy(@Param("userId") String userId);

    /**
     * 个人总质押
     */
    @Select("select ifnull(sum(zy.zy_balance),0) from wa_kuang_zy zy where zy.user_id = #{userId} and zy.is_return=0 and zy.coin = 'USDT-BEP20'")
    BigDecimal zzy(@Param("userId") String userId);

    /**
     * 团队奖励用户列表
     */
    @Select("select zy.user_id from wa_kuang_zy zy where zy.is_return=0 and zy.jl_balance > 0 and zy.coin = 'USDT-BEP20'" +
            " and (select sum(zy_balance) from wa_kuang_zy b where b.user_id=zy.user_id and b.jl_balance > 0 and b.coin = 'USDT-BEP20') >= 500" +
            " group by zy.user_id")
    List<String> tdjl();

    /**
     * 质押奖励余额大于0的用户ID
     */
    @Select("select  zy.user_id from wa_kuang_zy zy where  zy.jl_balance > 0 and zy.coin = 'USDT-BEP20'and zy.is_return=0 group by zy.user_id")
    List<String> tdjlids();

    /**
     * 取消质押
     */
    @Update("update wa_kuang_zy set is_return=1 where id = #{id}")
    int cancel(@Param("id") String id);

    @Select("select ifnull(SUM(zy.jl_balance),0)jlBalance,ifnull(zy.id,0)id from wa_kuang_zy zy where zy.user_id=#{userName}  and zy.coin = #{coin} and zy.is_return=0")
    WaKuangZy selectByUserIdAndCoin(@Param("userName") String userName, @Param("coin") String coin);


    List<WaKuangZy> selectNotReturnByUserIdsAndCoin(@Param("list") List<WaKuangJl> list, @Param("coin") String coin);

    @Select("SELECT IFNULL(SUM(zy_balance),0) FROM wa_kuang_zy WHERE coin=#{coin} AND DATEDIFF(create_time,NOW())=-1")
    BigDecimal getTodayPledgeByCoin(@Param("coin") String coin);

    @Select("SELECT IFNULL(SUM(zy_balance),0) FROM wa_kuang_zy WHERE coin=#{coin}")
    BigDecimal getHistoryPledgeByCoin(@Param("coin") String coin);


    @Select("SELECT IFNULL(SUM(wkz.zy_balance),0) FROM wa_kuang_zy wkz WHERE wkz.user_id=#{userId} and wkz.coin='Domi'")
    BigDecimal getDomiTotalPledge(@Param("userId") String userId);
    @Select("SELECT IFNULL(SUM(wkz.jl_balance),0) FROM wa_kuang_zy wkz WHERE  wkz.coin='USDT-BEP20' and wkz.is_return=0")
    BigDecimal getUsdtJLBalance();
}