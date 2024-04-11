package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.GonghuiUsershuhui;
import com.company.project.entity.GonghuiUsershuhui;
import com.company.project.mapper.GonghuiUserShuhuiMapper;
import com.company.project.service.GonghuiUserShuhuiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class GonghuiUserShuhuiServiceImpl extends ServiceImpl<GonghuiUserShuhuiMapper, GonghuiUsershuhui> implements GonghuiUserShuhuiService {

    @Resource
    private GonghuiUserShuhuiMapper gonghuiUsershuhuiMapper;


    @Override
    public IPage<GonghuiUsershuhui> pageInfo(GonghuiUsershuhui vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<GonghuiUsershuhui> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(GonghuiUsershuhui::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getGonghuiId())) {
            queryWrapper.eq(GonghuiUsershuhui::getGonghuiId, vo.getGonghuiId());
        }
        if (!StringUtils.isEmpty(vo.getStatus())&&vo.getStatus()!=0) {
            queryWrapper.eq(GonghuiUsershuhui::getStatus, vo.getStatus());
        }
        IPage<GonghuiUsershuhui> iPage = gonghuiUsershuhuiMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public List<GonghuiUsershuhui> findList(GonghuiUsershuhui vo) {
        LambdaQueryWrapper<GonghuiUsershuhui> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(GonghuiUsershuhui::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getGonghuiId())) {
            queryWrapper.eq(GonghuiUsershuhui::getGonghuiId, vo.getGonghuiId());
        }
        if (!StringUtils.isEmpty(vo.getStatus())&&vo.getStatus()!=0) {
            queryWrapper.eq(GonghuiUsershuhui::getStatus, vo.getStatus());
        }
        return gonghuiUsershuhuiMapper.selectList(queryWrapper);
    }
}
