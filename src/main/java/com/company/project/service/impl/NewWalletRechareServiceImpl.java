package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.NewWalletRecharge;
import com.company.project.mapper.NewWalletRechareMapper;
import com.company.project.service.NewWalletRechareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class NewWalletRechareServiceImpl extends ServiceImpl<NewWalletRechareMapper, NewWalletRecharge> implements NewWalletRechareService {

    @Resource
    private NewWalletRechareMapper newWalletRechareMapper;


    @Override
    public IPage<NewWalletRecharge> pageInfo(NewWalletRecharge vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<NewWalletRecharge> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(NewWalletRecharge::getAddress, vo.getAddress());
        }
        IPage<NewWalletRecharge> iPage = newWalletRechareMapper.selectPage(page, queryWrapper);
        return iPage;
    }


    @Override
    public void addWallet(NewWalletRecharge vo) {
        newWalletRechareMapper.insert(vo);
    }

    @Override
    public void updateWallet(NewWalletRecharge walletRecharge) {
        newWalletRechareMapper.updateById(walletRecharge);
    }

}
