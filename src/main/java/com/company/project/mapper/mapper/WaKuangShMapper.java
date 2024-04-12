package com.company.project.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.WaKuangSh;
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
public interface WaKuangShMapper extends BaseMapper<WaKuangSh> {

    @Update("update wa_kuang_zy set jl_balance = jl_balance - #{jlBalance},is_return = #{isReturn} where id=#{id} and jl_balance >= #{jlBalance}")
    int subJlBalance(@Param("id") String id, @Param("jlBalance") BigDecimal jlBalance, @Param("isReturn") Integer isReturn);

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
     * 取消质押
     */
    @Update("update wa_kuang_zy set is_return=1 where id = #{id}")
    int cancel(@Param("id") String id);

}