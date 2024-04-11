package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.WaKuangShMapper;
import com.company.project.service.WaKuangWalletService;
import com.company.project.service.WaKuangZyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/data/wakuang/sh")
@Slf4j
public class WaKuangShController {

    @Resource
    private WaKuangShMapper waKuangShMapper;

    @Resource
    private WaKuangZyService waKuangZyService;

    @Resource
    private WaKuangWalletService waKuangWalletService;

    @PostMapping("/shuhui")
    public DataResult userUpdate(@RequestBody WaKuangZy dto) {

        WaKuangZy waKuangZy = waKuangZyService.getById(dto.getId());
        if (!waKuangZy.getCoin().equals("Domi"))
            return DataResult.fail("当前币种无法进行赎回操作");
        WaKuangWallet waKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, waKuangZy.getUserId()).eq(WaKuangWallet::getCoin, waKuangZy.getCoin()));

        BigDecimal shBlance = waKuangZy.getZyBalance().multiply(BigDecimal.valueOf(0.95));
        // 加回资产
        if (waKuangZy.getStatus()==2){
            waKuangWallet.setYsinBalance(waKuangWallet.getYsinBalance().add(shBlance));
        }else {
            waKuangWallet.setBalance(waKuangWallet.getBalance().add(shBlance));
        }
        // 删除质押记录
        waKuangZyService.removeById(dto);

        WaKuangSh waKuangSh = new WaKuangSh();
        waKuangSh.setCoin(waKuangZy.getCoin());
        waKuangSh.setUserId(waKuangZy.getUserId());
        waKuangSh.setZyId(waKuangZy.getId());
        waKuangSh.setZyBalance(shBlance);
        waKuangShMapper.insert(waKuangSh);

        waKuangWalletService.updateWallet(waKuangWallet);

        return DataResult.success();
    }

    @PostMapping("/list")
    public DataResult userPageInfo(@RequestBody WaKuangSh vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangSh> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangSh::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangSh::getUserId, vo.getUserId());
        }
        IPage<WaKuangSh> iPage = waKuangShMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
