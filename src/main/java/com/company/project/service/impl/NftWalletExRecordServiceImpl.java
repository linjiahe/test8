package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.NftWallet;
import com.company.project.entity.NftWalletExRecord;
import com.company.project.entity.NftWalletExRecord;
import com.company.project.mapper.NftWalletExRecordMapper;
import com.company.project.mapper.NftWalletMapper;
import com.company.project.service.NftWalletExRecordService;
import com.company.project.service.NftWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class NftWalletExRecordServiceImpl extends ServiceImpl<NftWalletExRecordMapper, NftWalletExRecord> implements NftWalletExRecordService {

    @Resource
    private NftWalletExRecordMapper nftWalletExRecordMapper;


    @Override
    public IPage<NftWalletExRecord> pageInfo(NftWalletExRecord vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<NftWalletExRecord> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getStatus())&&vo.getStatus()!=-1) {
            queryWrapper.in(NftWalletExRecord::getStatus, vo.getStatus());
        }
        queryWrapper.orderByDesc(NftWalletExRecord::getCreateTime);
        IPage<NftWalletExRecord> iPage = nftWalletExRecordMapper.selectPage(page, queryWrapper);
        return iPage;
    }
}
