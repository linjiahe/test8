package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangJl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangJlService extends IService<WaKuangJl> {

    IPage<WaKuangJl> pageInfo(WaKuangJl vo);
    void pageInfoExport(WaKuangJl vo, HttpServletResponse response) throws IOException;
    int checkZyjl(String userId, String date);

    BigDecimal getTodayMapping(String coin);

    BigDecimal getTodayOutput(String coin);

    int updateByVersion(Long id, int status,int version);


}
