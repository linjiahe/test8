package com.company.project.controller;

import com.company.project.common.utils.DataResult;
import com.company.project.entity.WaKuangRewardRecord;
import com.company.project.service.WaKuangRewardRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/data/wakuang/rewardRecord")
@Slf4j
public class WaKuangRewardRecordController {

    @Resource
    private WaKuangRewardRecordService waKuangRewardRecordService;

    @PostMapping("/list")
    public DataResult list(@RequestBody WaKuangRewardRecord waKuangRewardRecord) {
        return DataResult.success(waKuangRewardRecordService.list(waKuangRewardRecord));
    }

    @PostMapping("/tj/{userId}")
    public DataResult tj(@PathVariable String userId) {
        return DataResult.success(waKuangRewardRecordService.tj(userId));
    }

//    @PostMapping("/pageInfo")
//    public DataResult pageInfo(@RequestBody WaKuangRewardRecord waKuangRewardRecord) {
//        return DataResult.success(waKuangRewardRecordService.pageInfo(waKuangRewardRecord));
//    }

}
