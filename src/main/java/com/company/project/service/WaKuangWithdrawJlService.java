package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangWithdrawJl;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangWithdrawJlService extends IService<WaKuangWithdrawJl> {

    IPage<WaKuangWithdrawJl> pageInfo(WaKuangWithdrawJl vo);

}
