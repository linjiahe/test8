package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.BusinessOrder;
import com.company.project.entity.BusinessOrderRecord;
import com.company.project.entity.WaKuangWallet;
import com.company.project.mapper.BusinessOrderMapper;
import com.company.project.mapper.BusinessOrderRecordMapper;
import com.company.project.service.WaKuangWalletService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping({"/data/order"})
public class BusinessOrderController {

    @Resource
    BusinessOrderMapper businessOrderMapper;

    @Resource
    BusinessOrderRecordMapper businessOrderRecordMapper;


    @PostMapping("/pageInfo")
    public DataResult pageInfo(@RequestBody BusinessOrder vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<BusinessOrder> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(BusinessOrder::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(BusinessOrder::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getType())) {
            queryWrapper.eq(BusinessOrder::getType, vo.getType());
        }
        IPage<BusinessOrder> iPage = businessOrderMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @PostMapping("/detail")
    public DataResult detail(@RequestBody BusinessOrder businessOrder) {
        return DataResult.success(businessOrderMapper.selectOne(Wrappers.<BusinessOrder>lambdaQuery().eq(BusinessOrder::getId, businessOrder.getId()))
        );
    }

    @PostMapping("/info/detail")
    public DataResult detail(@RequestBody BusinessOrderRecord businessOrderRecord) {
        return DataResult.success(businessOrderRecordMapper.selectOne(Wrappers.<BusinessOrderRecord>lambdaQuery().eq(BusinessOrderRecord::getId, businessOrderRecord.getId()))
        );
    }

    @PostMapping("/info/pageInfo")
    public DataResult infoPageInfo(@RequestBody BusinessOrderRecord vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<BusinessOrderRecord> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(BusinessOrderRecord::getUserId, vo.getUserId());
        }
        if (!StringUtils.isEmpty(vo.getStatus())) {
            queryWrapper.eq(BusinessOrderRecord::getStatus, vo.getStatus());
        }
        if (!StringUtils.isEmpty(vo.getType())) {
            queryWrapper.eq(BusinessOrderRecord::getType, vo.getType());
        }
        IPage<BusinessOrderRecord> iPage = businessOrderRecordMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

}