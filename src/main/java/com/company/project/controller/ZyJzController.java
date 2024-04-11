package com.company.project.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangZy;
import com.company.project.service.WaKuangJlService;
import com.company.project.service.WaKuangZyService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Api(tags = "质押奖励校正")
@RestController
@RequestMapping("/data/zy/jz")
@Slf4j
public class ZyJzController {

    @Resource
    private WaKuangZyService waKuangZyService;

    @Resource
    private WaKuangJlService waKuangJlService;

    @GetMapping
    @Transactional(rollbackFor = Exception.class)
    public DataResult jz() {

        List<WaKuangZy> zyList = waKuangZyService.list();

        if (CollectionUtil.isNotEmpty(zyList)) {

            List<WaKuangJl> jlList = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (WaKuangZy zy : zyList) {

                Date zyDate = zy.getCreateTime();
                BigDecimal jlBalance = BigDecimal.ZERO;

                switch (zy.getZyDay()) {
                    case 30:
                        jlBalance = zy.getZyBalance().multiply(BigDecimal.valueOf(15)).divide(BigDecimal.valueOf(30), 3, BigDecimal.ROUND_DOWN);
                        break;
                    case 90:
                        jlBalance = zy.getZyBalance().multiply(BigDecimal.valueOf(20)).divide(BigDecimal.valueOf(30), 3, BigDecimal.ROUND_DOWN);
                        break;
                    case 180:
                        jlBalance = zy.getZyBalance().multiply(BigDecimal.valueOf(25)).divide(BigDecimal.valueOf(30), 3, BigDecimal.ROUND_DOWN);
                        break;
                    case 360:
                        jlBalance = zy.getZyBalance().multiply(BigDecimal.valueOf(30)).divide(BigDecimal.valueOf(30), 3, BigDecimal.ROUND_DOWN);
                        break;
                }

                if (jlBalance.compareTo(BigDecimal.ZERO) == 1) {
                    int ind = 1;
                    while (true) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(zyDate);
                        cal.add(Calendar.DATE, ind++);

                        if (cal.getTimeInMillis() > System.currentTimeMillis()) {
                            break;
                        }

    //                    System.out.println(sdf.format(cal.getTime()));

                        if (waKuangJlService.checkZyjl(zy.getUserId(), sdf.format(cal.getTime())) == 0) {
                            WaKuangJl jl = new WaKuangJl();
                            jl.setUserId(zy.getUserId());
                            jl.setCoin("Domi");
                            jl.setBalance(jlBalance);
                            jl.setSourceType(3);
                            jl.setStatus(0);
                            jl.setCreateTime(cal.getTime());
                            jl.setUpdateTime(cal.getTime());
                            jlList.add(jl);
                        }

                        if (ind > zy.getZyDay()) {
                            break;
                        }

                    }

                }
            }

            if (CollectionUtil.isNotEmpty(jlList)) {
                waKuangJlService.saveBatch(jlList);
            }

        }
        return DataResult.success(true);
    }

}
