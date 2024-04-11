package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangShandui;
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
public interface WaKuangJlMapper extends BaseMapper<WaKuangJl> {

    @Select("select count(1) from wa_kuang_jl where source_type=3 and user_id=#{userId} and date_format(create_time, '%Y-%m-%d') = #{date}")
    int checkZyjl(@Param("userId") String userId, @Param("date") String date);

    @Select("select sum(balance) from wa_kuang_jl where coin = #{coin}")
    int selectSum(@Param("coin") String coin);
    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl where source_type in (10,12)")
    int allSumBnbBalance();

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl where source_type in (13)")
    int allSumLpBalance();

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl2 where source_type in (1)")
    int allSumTjLpBalance();

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl where source_type in (13) and  create_time > #{startTime} and create_time <=#{endTime}")
    int yesterdaySumLpBalance(@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl2 where source_type in (1) and  create_time > #{startTime} and create_time <=#{endTime}")
    int yesterdaySumTjLpBalance(@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl where source_type in (10,12) and  create_time > #{startTime} and create_time <=#{endTime}")
    int yesterdaySumBnbBalance(@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("SELECT IFNULL(SUM(balance),0) FROM wa_kuang_jl WHERE source_type=8 and coin =#{coin} AND DATEDIFF(create_time,NOW())=-1")
    BigDecimal getTodayMappingByCoin(@Param("coin") String coin);
    @Select("SELECT IFNULL(SUM(balance),0) FROM wa_kuang_jl WHERE coin =#{coin} AND DATEDIFF(create_time,NOW())=-1")
    BigDecimal getTodayOutputByCoin(@Param("coin")String coin);
    @Update("update wa_kuang_jl set status = #{status},version=version+1 where id = #{id} and version = #{version};")
    int updateByVersion(@Param("id") Long id, @Param("status") int status, @Param("version") int version);

    @Select("select count(*) as count,user_id from wa_kuang_jl where create_time >= '2023-12-02 00:00:00' and create_time < '2023-12-03 00:00:00' and source_type =#{source_type} GROUP BY user_id HAVING count=2;")
    List<WaKuangJl> syncUsers(@Param("source_type")int source_type);

    @Select("select * from wa_kuang_jl where create_time >= '2023-12-02 00:00:00' and create_time < '2023-12-03 00:00:00' and source_type =#{source_type} and user_id = #{userId} limit 1")
    WaKuangJl syncUsersJl(@Param("userId")String userId,@Param("source_type")int source_type);

    @Select("select * from wa_kuang_jl where source_type = 3 and binary coin = 'Domi' limit 1;")
    WaKuangJl JlReturnTask();

    @Select("select IFNULL(SUM(balance),0) from wa_kuang_jl where user_id = #{userId} and coin = #{coin};")
    BigDecimal selectJlSumByUser(@Param("coin") String coin,@Param("userId") String userId);

    @Select("select IFNULL(sum(from_balance),0) from wa_kuang_shandui where user_id = #{userId} and from_coin = #{coin};")
    BigDecimal shanduiFromBalance(@Param("coin") String coin,@Param("userId") String userId);

    @Select("select IFNULL(sum(to_balance),0) from wa_kuang_shandui where user_id = #{userId} and to_coin = #{coin};")
    BigDecimal shanduiToBalance(@Param("coin") String coin,@Param("userId") String userId);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_zhuanzhang where from_user_id = #{userId} and coin = #{coin};")
    BigDecimal zhuanzhangFromBalance(@Param("coin") String coin,@Param("userId") String userId);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_zhuanzhang where to_user_id = #{userId} and coin = #{coin};")
    BigDecimal zhuanzhangToBalance(@Param("coin") String coin,@Param("userId") String userId);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_wallet_withdraw where user_id = #{userId} and coin = 'Domi' and STATUS in(1,4);")
    BigDecimal withdrawBalance(@Param("coin") String coin,@Param("userId") String userId);

}