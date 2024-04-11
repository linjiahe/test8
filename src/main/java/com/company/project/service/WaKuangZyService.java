package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangZy;
import com.company.project.entity.WaKuangZyDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface WaKuangZyService extends IService<WaKuangZy> {

    IPage<WaKuangZy> pageInfo(WaKuangZy vo);

    void pageInfoExport(WaKuangZy vo, HttpServletResponse response) throws IOException;

    List<WaKuangZy> findList(WaKuangZyDTO vo);

    List<WaKuangZy> zydq();

    List<WaKuangZy> zyjl();

    List<WaKuangZy> domiIdmZyjl();

    List<WaKuangZy> domiBnbZyjl();

    List<WaKuangZy> domiZyjl();

    List<WaKuangZy> usdtZyjl();

    boolean subJlBalance(String id, BigDecimal jlBalance, Integer isReturn);

    BigDecimal getZyNumByUserId(String userId);

    BigDecimal zysy(String userId);

    BigDecimal zzy(String userId);

    IPage<WaKuangJl> jlPageInfo(WaKuangJl vo);
    void jlPageInfoExport(WaKuangJl vo, HttpServletResponse response) throws IOException;
    boolean cancel(String id);

    BigDecimal getTodayPledge(String coin);

    BigDecimal getHistoryPledge(String coin);

    BigDecimal getDomiTotalPledge(String userId);


    BigDecimal getUsdtJLBalance(String userId);
}
