package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.NftWallet;
import com.company.project.entity.SpHys;
import com.company.project.mapper.NftWalletMapper;
import com.company.project.mapper.SpHysMapper;
import com.company.project.service.NftWalletService;
import com.company.project.service.SpHysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class SpHysServiceImpl extends ServiceImpl<SpHysMapper, SpHys> implements SpHysService {

    @Resource
    private SpHysMapper spHysMapper;


    @Override
    public IPage<SpHys> pageInfo(SpHys vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<SpHys> queryWrapper = Wrappers.lambdaQuery();

        IPage<SpHys> iPage = spHysMapper.selectPage(page, queryWrapper);
        return iPage;
    }
}
