package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.NewWalletWithdraw;
import com.company.project.mapper.NewWalletWithdrawMapper;
import com.company.project.service.NewWalletWithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class NewWalletWithdrawServiceImpl extends ServiceImpl<NewWalletWithdrawMapper, NewWalletWithdraw> implements NewWalletWithdrawService {

    @Resource
    private NewWalletWithdrawMapper newWalletWithdrawMapper;

    @Override
    public IPage<NewWalletWithdraw> pageInfo(NewWalletWithdraw vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<NewWalletWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(NewWalletWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(NewWalletWithdraw::getCoin, vo.getCoin());
        }
        IPage<NewWalletWithdraw> iPage = newWalletWithdrawMapper.selectPage(page, queryWrapper);
        return iPage;
    }
}
