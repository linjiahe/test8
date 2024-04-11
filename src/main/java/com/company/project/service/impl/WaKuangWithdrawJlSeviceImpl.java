package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WaKuangWithdrawJl;
import com.company.project.mapper.WaKuangWithdrawJlMapper;
import com.company.project.service.WaKuangWithdrawJlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class WaKuangWithdrawJlSeviceImpl extends ServiceImpl<WaKuangWithdrawJlMapper, WaKuangWithdrawJl> implements WaKuangWithdrawJlService {

    @Resource
    private WaKuangWithdrawJlMapper waKuangWithdrawJlMapper;

    @Override
    public IPage<WaKuangWithdrawJl> pageInfo(WaKuangWithdrawJl vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWithdrawJl> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangWithdrawJl::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWithdrawJl::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getSourceType())) {
            queryWrapper.eq(WaKuangWithdrawJl::getSourceType, vo.getSourceType());
        }
        IPage<WaKuangWithdrawJl> iPage = waKuangWithdrawJlMapper.selectPage(page, queryWrapper);
        return iPage;
    }

}
