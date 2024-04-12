package com.company.project.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangJl2;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangJl2Mapper extends BaseMapper<WaKuangJl2> {

    @Select("select count(1) from wa_kuang_jl2 where source_type=3 and user_id=#{userId} and date_format(create_time, '%Y-%m-%d') = #{date}")
    int checkZyjl(@Param("userId") String userId, @Param("date") String date);

    @Select("select sum(balance) from wa_kuang_jl2 where coin = #{coin}")
    int selectSum(@Param("coin") String coin);
    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl2 where source_type in (10,12)")
    int allSumBnbBalance();

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl2 where source_type in (8,10) and  create_time > #{startTime} and create_time <=#{endTime} and user_id = #{userId}")
    int yesterdaySumKjBalance(@Param("userId") String userId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl2 where source_type in (13)")
    int allSumLpBalance();

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl2 where source_type in (13) and  create_time > #{startTime} and create_time <=#{endTime}")
    int yesterdaySumLpBalance(@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl2 where source_type in (10,12) and  create_time > #{startTime} and create_time <=#{endTime}")
    int yesterdaySumBnbBalance(@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("SELECT IFNULL(SUM(balance),0) FROM wa_kuang_jl2 WHERE source_type=8 and coin =#{coin} AND DATEDIFF(create_time,NOW())=-1")
    BigDecimal getTodayMappingByCoin(@Param("coin") String coin);
    @Select("SELECT IFNULL(SUM(balance),0) FROM wa_kuang_jl2 WHERE coin =#{coin} AND DATEDIFF(create_time,NOW())=-1")
    BigDecimal getTodayOutputByCoin(@Param("coin")String coin);

}