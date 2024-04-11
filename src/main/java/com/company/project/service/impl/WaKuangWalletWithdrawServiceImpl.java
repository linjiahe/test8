package com.company.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.WaKuangWalletWithdraw;
import com.company.project.mapper.WaKuangWalletMapper;
import com.company.project.mapper.WaKuangWalletWithdrawMapper;
import com.company.project.service.WaKuangWalletWithdrawService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
public class WaKuangWalletWithdrawServiceImpl extends ServiceImpl<WaKuangWalletWithdrawMapper, WaKuangWalletWithdraw> implements WaKuangWalletWithdrawService {

    @Resource
    private WaKuangWalletWithdrawMapper waKuangWalletWithdrawMapper;

    @Resource
    private WaKuangWalletMapper waKuangWalletMapper;

    @Override
    public IPage<WaKuangWalletWithdraw> pageInfo(WaKuangWalletWithdraw vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWalletWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getAddress, vo.getAddress());
        }
        IPage<WaKuangWalletWithdraw> iPage = waKuangWalletWithdrawMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public void listExport(WaKuangWalletWithdraw vo, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangWalletWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getAddress, vo.getAddress());
        }
        List<WaKuangWalletWithdraw> waKuangWalletWithdraws = waKuangWalletWithdrawMapper.selectList(queryWrapper);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("提现记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangWalletWithdraw.class)
                .sheet("提现记录")
                .doWrite(waKuangWalletWithdraws);
    }

    @Override
    public IPage<WaKuangWalletWithdraw> pageInfoAdmin(WaKuangWalletWithdraw vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWalletWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWalletWithdraw::getAddress, vo.getAddress());
        }
        IPage<WaKuangWalletWithdraw> iPage = waKuangWalletWithdrawMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Transactional
    @Override
    public void saveTr(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet waKuangWallet) {
        waKuangWallet.setBalance(waKuangWallet.getBalance().subtract(waKuangWalletWithdraw.getBalance().add(waKuangWalletWithdraw.getSxfBalance())));
        waKuangWallet.setDjBalance(waKuangWalletWithdraw.getBalance());
        waKuangWalletMapper.updateById(waKuangWallet);
        waKuangWalletWithdrawMapper.insert(waKuangWalletWithdraw);
    }

    @Override
    public void saveTrByDapp(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet waKuangWallet) {
        waKuangWallet.setBalance(waKuangWallet.getBalance().subtract(waKuangWalletWithdraw.getBalance().add(waKuangWalletWithdraw.getSxfBalance())));
        waKuangWallet.setDjBalance(waKuangWalletWithdraw.getBalance());
        waKuangWalletMapper.updateById(waKuangWallet);
        waKuangWalletWithdrawMapper.insert(waKuangWalletWithdraw);
    }

    @Override
    public void saveTrByDappJltx(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet waKuangWallet) {
        waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().subtract(waKuangWalletWithdraw.getBalance().add(waKuangWalletWithdraw.getSxfBalance())));
        waKuangWallet.setDjBalance(waKuangWalletWithdraw.getBalance());
        waKuangWalletMapper.updateById(waKuangWallet);
        waKuangWalletWithdrawMapper.insert(waKuangWalletWithdraw);
    }

    @Override
    public void saveTrByDappJldh(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet waKuangWallet) {
        waKuangWallet.setJldhBalance(waKuangWallet.getJldhBalance().subtract(waKuangWalletWithdraw.getBalance().add(waKuangWalletWithdraw.getSxfBalance())));
        waKuangWallet.setDjBalance(waKuangWalletWithdraw.getBalance());
        waKuangWalletMapper.updateById(waKuangWallet);
        waKuangWalletWithdrawMapper.insert(waKuangWalletWithdraw);
    }

    @Override
    public void saveTrByDappByYingshe(WaKuangWalletWithdraw waKuangWalletWithdraw, WaKuangWallet waKuangWallet) {
        waKuangWallet.setYsinBalance(waKuangWallet.getYsinBalance().subtract(waKuangWalletWithdraw.getBalance().add(waKuangWalletWithdraw.getSxfBalance())));
        waKuangWallet.setDjBalance(waKuangWalletWithdraw.getBalance());
        waKuangWalletMapper.updateById(waKuangWallet);
        waKuangWalletWithdrawMapper.insert(waKuangWalletWithdraw);
    }

    @Override
    public BigDecimal getBalanceOfDomiAndUsdt(String userId) {
        return waKuangWalletWithdrawMapper.getBalanceOfDomiAndUsdt();
    }

}
