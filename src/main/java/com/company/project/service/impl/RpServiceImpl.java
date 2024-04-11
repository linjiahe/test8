package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.Rp;
import com.company.project.mapper.RpMapper;
import com.company.project.service.RpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class RpServiceImpl extends ServiceImpl<RpMapper, Rp> implements RpService {

    @Resource
    private RpMapper rpMapper;

    @Override
    public IPage<Rp> pageInfo(Rp vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<Rp> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(Rp::getUserId, vo.getUserId());
        }
        IPage<Rp> iPage = rpMapper.selectPage(page, queryWrapper);
        return iPage;
    }
}
