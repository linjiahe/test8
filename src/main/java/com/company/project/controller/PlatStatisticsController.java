package com.company.project.controller;

import com.company.project.common.utils.DataResult;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangZy;
import com.company.project.mapper.WaKuangJlMapper;
import com.company.project.service.WaKuangJlService;
import com.company.project.service.WaKuangZyService;
import com.company.project.vo.resp.TodayStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "平台统计")
@RestController
@RequestMapping("/data/statistic")
@Slf4j
public class PlatStatisticsController {

    @Resource
    private WaKuangJlService waKuangJlService;

    @Resource
    private WaKuangZyService waKuangZyService;


    @ApiOperation("今日统计数据")
    @GetMapping("/todayData")
    public DataResult todayStatistic() {
        TodayStatisticsVo todayStatisticsVo = new TodayStatisticsVo();
        todayStatisticsVo.setDOMITodayPledge(waKuangZyService.getTodayPledge("DOMI"));
        todayStatisticsVo.setDOMIHistoryPledge(waKuangZyService.getHistoryPledge("DOMI"));
        todayStatisticsVo.setDOMITodayMapping(waKuangJlService.getTodayMapping("DOMI"));
        todayStatisticsVo.setDMDTodayOutput(waKuangJlService.getTodayOutput("DMD"));
        todayStatisticsVo.setDMDTodayMapping(waKuangJlService.getTodayMapping("DMD"));
        return DataResult.success(todayStatisticsVo);
    }
}
