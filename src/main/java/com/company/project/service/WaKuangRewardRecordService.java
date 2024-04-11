package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangRewardRecord;
import com.company.project.entity.WaKuangRewardRecordTjVO;
import com.company.project.entity.WaKuangRewardRecordVO;

import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangRewardRecordService extends IService<WaKuangRewardRecord> {

    List<WaKuangRewardRecord> list(WaKuangRewardRecord vo);

    IPage<WaKuangRewardRecord> pageInfo(WaKuangRewardRecord vo);

    WaKuangRewardRecordTjVO tj(String userId);

}
