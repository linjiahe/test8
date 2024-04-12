package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangJl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangJlMapper extends BaseMapper<WaKuangJl> {

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl where source_type in (1,2,3) and user_id = #{userId} and  create_time > #{startTime} and create_time <=#{endTime}")
    int sumBalanceByUserAndDate(@Param("userId") String userId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @Select("select IFNULL(sum(balance),0) from wa_kuang_jl where source_type in (1,2,3) and user_id = #{userId}")
    int sumBalanceByUser(@Param("userId") String userId);


}