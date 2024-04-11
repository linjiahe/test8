package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.DayPrice;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface DayPriceService extends IService<DayPrice> {

    void updateWallet(DayPrice wallet);

    IPage<DayPrice> pageInfo(DayPrice vo);
}
