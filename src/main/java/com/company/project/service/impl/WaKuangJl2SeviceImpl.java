package com.company.project.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WaKuangJl2;
import com.company.project.entity.WaKuangJl2;
import com.company.project.mapper.WaKuangJl2Mapper;
import com.company.project.mapper.WaKuangJlMapper;
import com.company.project.service.WaKuangJl2Service;
import com.company.project.service.WaKuangJlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
public class WaKuangJl2SeviceImpl extends ServiceImpl<WaKuangJl2Mapper, WaKuangJl2> implements WaKuangJl2Service {

    @Resource
    private WaKuangJl2Mapper waKuangJl2Mapper;

    @Override
    public IPage<WaKuangJl2> pageInfo(WaKuangJl2 vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangJl2> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJl2::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJl2::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangJl2::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getSourceType())) {
            queryWrapper.eq(WaKuangJl2::getSourceType, vo.getSourceType());
        }
        queryWrapper.orderByDesc(WaKuangJl2::getCreateTime);
        IPage<WaKuangJl2> iPage = waKuangJl2Mapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public IPage<WaKuangJl2> pageInfoLes(WaKuangJl2 vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangJl2> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WaKuangJl2::getSourceType, 99);
        queryWrapper.orderByDesc(WaKuangJl2::getCreateTime);
        IPage<WaKuangJl2> iPage = waKuangJl2Mapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public void pageInfoExport(WaKuangJl2 vo, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangJl2> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJl2::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJl2::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangJl2::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getSourceType())) {
            queryWrapper.eq(WaKuangJl2::getSourceType, vo.getSourceType());
        }
        queryWrapper.orderByDesc(WaKuangJl2::getCreateTime);

        List<WaKuangJl2> waKuangJls = waKuangJl2Mapper.selectList(queryWrapper);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("奖励记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangJl2.class)
                .sheet("奖励记录")
                .doWrite(waKuangJls);
    }

    @Override
    public int checkZyjl(String userId, String date) {
        return waKuangJl2Mapper.checkZyjl(userId, date);
    }

    @Override
    public BigDecimal getTodayMapping(String coin) {
        return waKuangJl2Mapper.getTodayMappingByCoin(coin);
    }

    @Override
    public BigDecimal getTodayOutput(String coin) {
        return waKuangJl2Mapper.getTodayOutputByCoin(coin);
    }

}
