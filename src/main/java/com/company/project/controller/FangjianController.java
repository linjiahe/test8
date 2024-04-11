package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.FangjianMapper;
import com.company.project.mapper.FangjianUserMapper;
import com.company.project.mapper.GonghuiTaocanMapper;
import com.company.project.service.GonghuiJlRecordService;
import com.company.project.service.GonghuiService;
import com.company.project.service.GonghuiUserService;
import com.company.project.service.GonghuiUserShuhuiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/fangjian")
@Slf4j
public class FangjianController {

    @Resource
    private FangjianMapper fangjianMapper;

    @Resource
    private FangjianUserMapper fangjianUserMapper;


    @PostMapping("/pageInfo")
    public DataResult pageInfo(@RequestBody Fangjian vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<Fangjian> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getStatus())&&vo.getStatus()!=0&&vo.getStatus()!=99) {
            queryWrapper.eq(Fangjian::getStatus, vo.getStatus());
        }
        if (vo.getStatus()==99) {
            queryWrapper.in(Fangjian::getStatus, 1,2);
        }
        IPage<Fangjian> iPage = fangjianMapper.selectPage(page, queryWrapper);
        List<Fangjian> list= iPage.getRecords();
        for (Fangjian fangjian:list){
            Integer count =fangjianUserMapper.selectCount(Wrappers.<FangjianUser>lambdaQuery().eq(FangjianUser::getFangjianId, fangjian.getId()));
            fangjian.setJoinCount(count);
        }
        return DataResult.success(iPage);
    }

    @PostMapping("/update")
    public DataResult update(@RequestBody Fangjian vo) {
        return DataResult.success(fangjianMapper.updateById(vo)
        );
    }

    @PostMapping("/add")
    public DataResult add(@RequestBody Fangjian vo) {
        return DataResult.success(fangjianMapper.insert(vo)
        );
    }

    @PostMapping("/detail")
    public DataResult detail(@RequestBody Fangjian vo) {
        Fangjian fangjian = fangjianMapper.selectOne(Wrappers.<Fangjian>lambdaQuery().eq(Fangjian::getId, vo.getId()));
        return DataResult.success(fangjian);
    }


    @PostMapping("/user/pageInfo")
    public DataResult userPageInfo(@RequestBody FangjianUser vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<FangjianUser> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(FangjianUser::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getStatus())&&vo.getStatus()!=0) {
            queryWrapper.eq(FangjianUser::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getEndTime())&&vo.getEndTime()!=0) {
            queryWrapper.eq(FangjianUser::getEndTime, vo.getEndTime());
        }
        if (!StringUtils.isEmpty(vo.getFangjianId())) {
            queryWrapper.eq(FangjianUser::getFangjianId, vo.getFangjianId());
        }
        IPage<FangjianUser> iPage = fangjianUserMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }


    @PostMapping("/user/update")
    public DataResult userUpdate(@RequestBody FangjianUser vo) {
        return DataResult.success(fangjianUserMapper.updateById(vo)
        );
    }

    @PostMapping("/user/add")
    public DataResult userAdd(@RequestBody FangjianUser vo) {
        return DataResult.success(fangjianUserMapper.insert(vo)
        );
    }

}
