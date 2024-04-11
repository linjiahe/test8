package com.company.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.entity.Fangjian;
import com.company.project.entity.Gonghui;
import org.apache.ibatis.annotations.Update;

/**
 * 用户 Mapper
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface FangjianMapper extends BaseMapper<Fangjian> {

    @Update("update fangjian set status=3 where end_time < now() and status <> 3")
    int closeFangJian();

}