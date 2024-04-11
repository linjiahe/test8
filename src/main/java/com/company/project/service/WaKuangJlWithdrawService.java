package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangJlWithdraw;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangJlWithdrawService extends IService<WaKuangJlWithdraw> {

    IPage<WaKuangJlWithdraw> pageInfo(WaKuangJlWithdraw vo);

    IPage<WaKuangJlWithdraw> pageInfoAdmin(WaKuangJlWithdraw vo);

}
