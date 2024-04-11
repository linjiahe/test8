package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.BnbPrice;
import com.company.project.entity.Kuangji;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface KuangjiMapper extends BaseMapper<Kuangji> {

    @Select("select ifnull(sum(zy_balance),0) from wa_kuang_zy where  create_time > #{startTime} and create_time <=#{endTime} and coin = #{coin} and coin = #{coin}")
    List<Kuangji> selectListByDate(@Param("coin") String coin, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Update("update Kuangji set sy_count=sy_count - 1 where id=#{id} and sy_count > 0")
    int subKuangji(@Param("id") Long id);

}