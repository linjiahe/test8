package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.GonghuiUser;
import com.company.project.entity.GonghuiUser;
import com.company.project.entity.Wallet;
import com.company.project.mapper.GonghuiMapper;
import com.company.project.mapper.GonghuiUserMapper;
import com.company.project.service.GonghuiService;
import com.company.project.service.GonghuiUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class GonghuiUserServiceImpl extends ServiceImpl<GonghuiUserMapper, GonghuiUser> implements GonghuiUserService {

    @Resource
    private GonghuiUserMapper gonghuiUserMapper;


    @Override
    public IPage<GonghuiUser> pageInfo(GonghuiUser vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<GonghuiUser> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(GonghuiUser::getAddress, vo.getAddress());
        }
        IPage<GonghuiUser> iPage = gonghuiUserMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public List<GonghuiUser> findList(GonghuiUser vo) {
        LambdaQueryWrapper<GonghuiUser> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(GonghuiUser::getAddress, vo.getAddress());
        }
        return gonghuiUserMapper.selectList(queryWrapper);
    }
}
