package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.Gonghui;
import com.company.project.entity.Gonghui;
import com.company.project.entity.NftWalletDTO;
import com.company.project.mapper.GonghuiMapper;
import com.company.project.mapper.NftWalletMapper;
import com.company.project.service.GonghuiService;
import com.company.project.service.NftWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class GonghuiServiceImpl extends ServiceImpl<GonghuiMapper, Gonghui> implements GonghuiService {

    @Resource
    private GonghuiMapper gonghuiMapper;


    @Override
    public IPage<Gonghui> pageInfo(Gonghui vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<Gonghui> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(Gonghui::getAddress, vo.getAddress());
        }
        IPage<Gonghui> iPage = gonghuiMapper.selectPage(page, queryWrapper);
        return iPage;
    }
}
