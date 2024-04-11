package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.Wallet;
import com.company.project.entity.WalletWithdraw;
import com.company.project.mapper.WalletMapper;
import com.company.project.mapper.WalletWithdrawMapper;
import com.company.project.service.WalletWithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class WalletWithdrawServiceImpl extends ServiceImpl<WalletWithdrawMapper, WalletWithdraw> implements WalletWithdrawService {

    @Resource
    private WalletWithdrawMapper walletWithdrawMapper;

    @Resource
    private WalletMapper walletMapper;

    @Override
    public IPage<WalletWithdraw> pageInfo(WalletWithdraw vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WalletWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WalletWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WalletWithdraw::getCoin, vo.getCoin());
        }
//        if (!StringUtils.isEmpty(vo.getStatus())) {
//            queryWrapper.eq(WalletWithdraw::getStatus, vo.getStatus());
//        }
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WalletWithdraw::getAddress, vo.getAddress());
        }
        IPage<WalletWithdraw> iPage = walletWithdrawMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public IPage<WalletWithdraw> pageInfoAdmin(WalletWithdraw vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WalletWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WalletWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WalletWithdraw::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WalletWithdraw::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WalletWithdraw::getAddress, vo.getAddress());
        }
        IPage<WalletWithdraw> iPage = walletWithdrawMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Transactional
    @Override
    public void saveTr(WalletWithdraw walletWithdraw, Wallet wallet) {
        wallet.setBalance(wallet.getBalance().subtract(walletWithdraw.getBalance().add(walletWithdraw.getSxfBalance())));
        //设置冻结金额
        wallet.setCzBalance(walletWithdraw.getBalance().add(walletWithdraw.getSxfBalance()));
        walletMapper.updateById(wallet);
        walletWithdrawMapper.insert(walletWithdraw);
    }

}
