package com.company.project.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WaKuangWalletRecharge;
import com.company.project.entity.WaKuangWalletWithdraw;
import com.company.project.mapper.WaKuangWalletRechareMapper;
import com.company.project.mapper.WalletRechareMapper;
import com.company.project.service.WaKuangWalletRechareService;
import com.company.project.service.WalletRechareService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
public class WaKuangWalletRechareServiceImpl extends ServiceImpl<WaKuangWalletRechareMapper, WaKuangWalletRecharge> implements WaKuangWalletRechareService {

    @Resource
    private WaKuangWalletRechareMapper waKuangWalletRechareMapper;

    @Override
    public IPage<WaKuangWalletRecharge> pageInfo(WaKuangWalletRecharge vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWalletRecharge> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWalletRecharge::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWalletRecharge::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangWalletRecharge::getUserId, vo.getUserId());
        }
        IPage<WaKuangWalletRecharge> iPage = waKuangWalletRechareMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public void pageInfoExport(WaKuangWalletRecharge vo, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangWalletRecharge> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWalletRecharge::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWalletRecharge::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangWalletRecharge::getUserId, vo.getUserId());
        }
        List<WaKuangWalletRecharge> waKuangWalletRecharges = waKuangWalletRechareMapper.selectList(queryWrapper);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("充值记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangWalletRecharge.class)
                .sheet("充值记录")
                .doWrite(waKuangWalletRecharges);

    }


    @Override
    public void addWallet(WaKuangWalletRecharge vo) {
        waKuangWalletRechareMapper.insert(vo);
    }

    @Override
    public void updateWallet(WaKuangWalletRecharge walletRecharge) {
        waKuangWalletRechareMapper.updateById(walletRecharge);
    }



}
