package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.Wallet;
import com.company.project.mapper.WalletMapper;
import com.company.project.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements WalletService {

    @Resource
    private WalletMapper walletMapper;


    @Override
    public IPage<Wallet> pageInfo(Wallet vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<Wallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(Wallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(Wallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(Wallet::getUserId, vo.getUserId());
        }
        IPage<Wallet> iPage = walletMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public List<Wallet> List(Wallet vo) {
        return walletMapper.selectList(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,vo.getUserId()));
    }

    @Override
    public List<Wallet> ListByCoin(String coin) {
        return walletMapper.selectList(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getCoin,coin));
    }



    @Override
    public void addWallet(Wallet vo) {
        walletMapper.insert(vo);
    }

    @Override
    public void updateWallet(Wallet wallet) {
        walletMapper.updateById(wallet);
    }

}
