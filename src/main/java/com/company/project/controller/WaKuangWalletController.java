package com.company.project.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.entity.*;
import com.company.project.mapper.WaKuangJlMapper;
import com.company.project.mapper.WaKuangWalletMapper;
import com.company.project.mapper.WaKuangZyMapper;
import com.company.project.mapper.mapper.*;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.bitcoinj.crypto.MnemonicException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/data/wakuang")
@Slf4j
public class WaKuangWalletController {

    @Resource
    private WaKuangWalletMapper waKuangWalletMapper;


    @PostMapping("/wallet/pageInfo")
    public DataResult pageInfo(@RequestBody WaKuangWallet vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        IPage<WaKuangWallet> iPage = waKuangWalletMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage
        );
    }

    @PostMapping("/wallet/pageInfoExport")
    public void pageInfoExport(@RequestBody WaKuangWallet vo,HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        List<WaKuangWallet> waKuangWallets = waKuangWalletMapper.selectList(queryWrapper);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangWallet.class)
                .sheet("记录")
                .doWrite(waKuangWallets);
    }


    @PostMapping("/wallet/pageInfoGroupByUser")
    public DataResult pageInfoGroupByUser(@RequestBody WaKuangWallet vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getInvitationCode())) {
            queryWrapper.eq(WaKuangWallet::getInvitationCode, vo.getInvitationCode());
        }

        queryWrapper.eq(WaKuangWallet::getCoin, "USDT-BEP20");

        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        queryWrapper.groupBy(WaKuangWallet::getUserName);
        IPage<WaKuangWallet> iPage = waKuangWalletMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage
        );
    }

    @PostMapping("/wallet/findUserAll")
    public DataResult findUserAll(@RequestBody WaKuangWallet vo) {
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        if (!StringUtils.isEmpty(vo.getCode())) {
            queryWrapper.eq(WaKuangWallet::getCode, vo.getCode());
        }
        if (!StringUtils.isEmpty(vo.getInvitationCode())) {
            queryWrapper.eq(WaKuangWallet::getInvitationCode, vo.getInvitationCode());
        }
        queryWrapper.groupBy(WaKuangWallet::getUserName);
        return DataResult.success(waKuangWalletMapper.selectList(queryWrapper)
        );
    }

}
