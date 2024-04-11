package com.company.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.exception.BusinessException;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangShandui;
import com.company.project.entity.WaKuangZy;
import com.company.project.entity.WaKuangZyDTO;
import com.company.project.mapper.WaKuangJlMapper;
import com.company.project.mapper.WaKuangWalletMapper;
import com.company.project.mapper.WaKuangZyMapper;
import com.company.project.service.WaKuangZyService;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class WaKuangZyServiceImpl extends ServiceImpl<WaKuangZyMapper, WaKuangZy> implements WaKuangZyService {

    @Resource
    private WaKuangZyMapper waKuangZyMapper;

    @Resource
    private WaKuangJlMapper waKuangJlMapper;

    @Resource
    private WaKuangWalletMapper waKuangWalletMapper;

    @Override
    public IPage<WaKuangZy> pageInfo(WaKuangZy vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangZy> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangZy::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangZy::getUserId, vo.getUserId());
        }
        IPage<WaKuangZy> iPage = waKuangZyMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public void pageInfoExport(WaKuangZy vo, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangZy> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangZy::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangZy::getUserId, vo.getUserId());
        }
        List<WaKuangZy> waKuangZyList = waKuangZyMapper.selectList(queryWrapper);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("质押记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangZy.class)
                .sheet("质押记录")
                .doWrite(waKuangZyList);



    }

    @Override
    public IPage<WaKuangJl> jlPageInfo(WaKuangJl vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangJl> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJl::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJl::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangJl::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getSourceType())) {
            queryWrapper.eq(WaKuangJl::getSourceType, vo.getSourceType());
        }
        IPage<WaKuangJl> iPage = waKuangJlMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public void jlPageInfoExport(WaKuangJl vo, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangJl> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangJl::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangJl::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(WaKuangJl::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getSourceType())) {
            queryWrapper.eq(WaKuangJl::getSourceType, vo.getSourceType());
        }
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
    public List<WaKuangZy> findList(WaKuangZyDTO vo) {
        LambdaQueryWrapper<WaKuangZy> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangZy::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangZy::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getIsReturn())) {
            queryWrapper.eq(WaKuangZy::getIsReturn, vo.getIsReturn());
        }
        if (!StringUtils.isEmpty(vo.getUserIds())) {
            queryWrapper.in(WaKuangZy::getUserId, vo.getUserIds());
        }
        return waKuangZyMapper.selectList(queryWrapper);
    }

    @Override
    public List<WaKuangZy> zydq() {
        return waKuangZyMapper.zydq();
    }

    @Override
    public List<WaKuangZy> zyjl() {
        return waKuangZyMapper.zyjl();
    }

    @Override
    public List<WaKuangZy> domiIdmZyjl() {
        return waKuangZyMapper.domiIdmZyjl();
    }

    @Override
    public List<WaKuangZy> domiBnbZyjl() {
        return waKuangZyMapper.domiBnbZyjl();
    }

    @Override
    public List<WaKuangZy> domiZyjl() {
        return waKuangZyMapper.domiZyjl();
    }

    @Override
    public List<WaKuangZy> usdtZyjl() {
        return waKuangZyMapper.usdtZyjl();
    }

    @Override
    public boolean subJlBalance(String id, BigDecimal jlBalance, Integer isReturn) {
        return waKuangZyMapper.subJlBalance(id, jlBalance, isReturn) > 0;
    }

    @Override
    public BigDecimal getZyNumByUserId(String userId) {
        return waKuangZyMapper.getZyNumByUserId(userId);
    }

    @Override
    public BigDecimal zysy(String userId) {
        return waKuangZyMapper.zysy(userId);
    }

    @Override
    public BigDecimal zzy(String userId) {
        return waKuangZyMapper.zzy(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(String id) {
        WaKuangZy zy = waKuangZyMapper.selectById(id);
        if (Objects.nonNull(zy) && waKuangWalletMapper.addBalance(zy.getUserId(), zy.getCoin(), zy.getZyBalance()) > 0) {
            return waKuangZyMapper.cancel(id) > 0;
        }
        return false;
    }

    /**
     * 获取今日质押数量
     *
     * @param coin
     * @return
     */
    @Override
    public BigDecimal getTodayPledge(String coin) {
        return waKuangZyMapper.getTodayPledgeByCoin(coin);
    }

    /**
     * 获取历史质押数量
     *
     * @param coin
     * @return
     */
    @Override
    public BigDecimal getHistoryPledge(String coin) {
        return waKuangZyMapper.getHistoryPledgeByCoin(coin);
    }

    @Override
    public BigDecimal getDomiTotalPledge(String userId) {
        if (StrUtil.isBlank(userId)) throw new BusinessException("userId为空");
        return waKuangZyMapper.getDomiTotalPledge(userId);
    }

    @Override
    public BigDecimal getUsdtJLBalance(String userId) {
        return waKuangZyMapper.getUsdtJLBalance();
    }

}
