package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.RpRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface RpRecordMapper extends BaseMapper<RpRecord> {

    @Update("update rp_record set user_id=#{userId} where id = #{id}")
    int updateUserId(@Param("userId") String userId,@Param("id") String id);

}