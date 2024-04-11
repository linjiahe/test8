package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WaKuangJlWithdraw;
import com.company.project.mapper.WaKuangWalletMapper;
import com.company.project.mapper.WaKuangJlWithdrawMapper;
import com.company.project.service.WaKuangJlWithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class WaKuangJlWithdrawServiceImpl extends ServiceImpl<WaKuangJlWithdrawMapper, WaKuangJlWithdraw> implements WaKuangJlWithdrawService {

    @Resource
    private WaKuangJlWithdrawMapper waKuangJlWithdrawMapper;

    @Resource
    private WaKuangWalletMapper waKuangWalletMapper;

    @Override
    public IPage<WaKuangJlWithdraw> pageInfo(WaKuangJlWithdraw vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangJlWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJlWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJlWithdraw::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangJlWithdraw::getAddress, vo.getAddress());
        }
        IPage<WaKuangJlWithdraw> iPage = waKuangJlWithdrawMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public IPage<WaKuangJlWithdraw> pageInfoAdmin(WaKuangJlWithdraw vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangJlWithdraw> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJlWithdraw::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJlWithdraw::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangJlWithdraw::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangJlWithdraw::getAddress, vo.getAddress());
        }
        IPage<WaKuangJlWithdraw> iPage = waKuangJlWithdrawMapper.selectPage(page, queryWrapper);
        return iPage;
    }

}
