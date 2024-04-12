package com.company.project.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.Kuangji;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface KuangjiMapper extends BaseMapper<Kuangji> {

    @Update("update Kuangji set sy_count=sy_count - 1 where id=#{id} and sy_count > 0")
    int subKuangji(@Param("id") Long id);
}