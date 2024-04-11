package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WalletRecharge;
import com.company.project.mapper.WalletRechareMapper;
import com.company.project.service.WalletRechareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class WalletRechareServiceImpl extends ServiceImpl<WalletRechareMapper, WalletRecharge> implements WalletRechareService {

    @Resource
    private WalletRechareMapper walletRechareMapper;

    @Override
    public IPage<WalletRecharge> pageInfo(WalletRecharge vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WalletRecharge> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WalletRecharge::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WalletRecharge::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WalletRecharge::getUserId, vo.getUserId());
        }
        IPage<WalletRecharge> iPage = walletRechareMapper.selectPage(page, queryWrapper);
        return iPage;
    }


    @Override
    public void addWallet(WalletRecharge vo) {
        walletRechareMapper.insert(vo);
    }

    @Override
    public void updateWallet(WalletRecharge walletRecharge) {
        walletRechareMapper.updateById(walletRecharge);
    }

}
