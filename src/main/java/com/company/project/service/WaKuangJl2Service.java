package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangJl2;
import com.company.project.entity.WaKuangJl2;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangJl2Service extends IService<WaKuangJl2> {

    IPage<WaKuangJl2> pageInfo(WaKuangJl2 vo);

    IPage<WaKuangJl2> pageInfoLes(WaKuangJl2 vo);
    void pageInfoExport(WaKuangJl2 vo, HttpServletResponse response) throws IOException;
    int checkZyjl(String userId, String date);

    BigDecimal getTodayMapping(String coin);

    BigDecimal getTodayOutput(String coin);


}
