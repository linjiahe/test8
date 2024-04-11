package com.company.project.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangWalletWithdraw;
import com.company.project.mapper.WaKuangJlMapper;
import com.company.project.service.WaKuangJlService;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.temporal.ValueRange;
import java.util.List;

@Service
@Slf4j
public class WaKuangJlSeviceImpl extends ServiceImpl<WaKuangJlMapper, WaKuangJl> implements WaKuangJlService {

    @Resource
    private WaKuangJlMapper waKuangJlMapper;

    @Override
    public IPage<WaKuangJl> pageInfo(WaKuangJl vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangJl> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJl::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJl::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangJl::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getSourceType())) {
            queryWrapper.eq(WaKuangJl::getSourceType, vo.getSourceType());
        }
        queryWrapper.orderByDesc(WaKuangJl::getCreateTime);
        IPage<WaKuangJl> iPage = waKuangJlMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public void pageInfoExport(WaKuangJl vo, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangJl> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJl::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJl::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangJl::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getSourceType())) {
            queryWrapper.eq(WaKuangJl::getSourceType, vo.getSourceType());
        }
        queryWrapper.orderByDesc(WaKuangJl::getCreateTime);

        List<WaKuangJl> waKuangJls = waKuangJlMapper.selectList(queryWrapper);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("奖励记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangJl.class)
                .sheet("奖励记录")
                .doWrite(waKuangJls);
    }

    @Override
    public int checkZyjl(String userId, String date) {
        return waKuangJlMapper.checkZyjl(userId, date);
    }

    @Override
    public BigDecimal getTodayMapping(String coin) {
        return waKuangJlMapper.getTodayMappingByCoin(coin);
    }

    @Override
    public BigDecimal getTodayOutput(String coin) {
        return waKuangJlMapper.getTodayOutputByCoin(coin);
    }

    @Override
    public int updateByVersion(Long id, int status,int version){
        return waKuangJlMapper.updateByVersion(id, status, version);
    }

}
