package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.RpRecord;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface RpRecordService extends IService<RpRecord> {

    int updateUserId(RpRecord rpRecord);

    IPage<RpRecord> pageInfo(RpRecord rpRecord);

}
