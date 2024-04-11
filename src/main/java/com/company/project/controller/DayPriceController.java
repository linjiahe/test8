package com.company.project.controller;

import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.DayPrice;
import com.company.project.service.DayPriceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/day")
@Slf4j
public class DayPriceController {

    @Resource
    private DayPriceService dayPriceService;

    @PostMapping("/price-insert")
    @ApiOperation(value = "分页获取信息表接口")
//    @LogAnnotation(title = "信息管理", action = "分页获取信息列表")
    public DataResult Insert(@RequestBody DayPrice vo) {
        return DataResult.success(dayPriceService.save(vo));
    }


    @PostMapping("/price-detail")
    @ApiOperation(value = "分页获取信息表接口")
//    @LogAnnotation(title = "信息管理", action = "分页获取信息列表")
    public DataResult Detail() {
        return DataResult.success(dayPriceService.getOne(null));
    }

    @PostMapping("/price-update")
    @ApiOperation(value = "分页获取信息表接口")
//    @LogAnnotation(title = "信息管理", action = "分页获取信息列表")
    public DataResult Update(@RequestBody DayPrice vo) {
        dayPriceService.updateWallet(vo);
        return DataResult.success();
    }
}
