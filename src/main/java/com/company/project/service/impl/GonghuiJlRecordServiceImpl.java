package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.GonghuiJlRecord;
import com.company.project.mapper.GonghuiJlRecordMapper;
import com.company.project.service.GonghuiJlRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class GonghuiJlRecordServiceImpl extends ServiceImpl<GonghuiJlRecordMapper, GonghuiJlRecord> implements GonghuiJlRecordService {

    @Resource
    private GonghuiJlRecordMapper jlRecordMapper;

    @Override
    public IPage<GonghuiJlRecord> pageInfo(GonghuiJlRecord vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<GonghuiJlRecord> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(GonghuiJlRecord::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(GonghuiJlRecord::getStatus, vo.getStatus());
        }
        IPage<GonghuiJlRecord> iPage = jlRecordMapper.selectPage(page, queryWrapper);
        return iPage;
    }
}
