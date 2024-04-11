package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.RpRecord;
import com.company.project.mapper.RpRecordMapper;
import com.company.project.service.RpRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class RpRecordServiceImpl extends ServiceImpl<RpRecordMapper, RpRecord> implements RpRecordService {

    @Resource
    private RpRecordMapper rpRecordMapper;

    @Override
    public int updateUserId(RpRecord rpRecord) {
        return rpRecordMapper.updateUserId(rpRecord.getUserId(),rpRecord.getId());
    }

    @Override
    public IPage<RpRecord> pageInfo(RpRecord vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<RpRecord> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(RpRecord::getUserId, vo.getUserId());
        }
        if (vo.getStatus()==1||vo.getStatus()==2) {
            queryWrapper.eq(RpRecord::getStatus, vo.getStatus());
        }
        queryWrapper.orderByAsc(RpRecord::getStatus);
        IPage<RpRecord> iPage = rpRecordMapper.selectPage(page, queryWrapper);
        return iPage;
    }
}
