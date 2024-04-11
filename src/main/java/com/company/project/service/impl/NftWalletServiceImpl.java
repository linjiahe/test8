package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.GonghuiUser;
import com.company.project.entity.NftWallet;
import com.company.project.entity.NftWallet;
import com.company.project.entity.NftWalletDTO;
import com.company.project.mapper.NftWalletMapper;
import com.company.project.mapper.WalletMapper;
import com.company.project.service.NftWalletService;
import com.company.project.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class NftWalletServiceImpl extends ServiceImpl<NftWalletMapper, NftWallet> implements NftWalletService {

    @Resource
    private NftWalletMapper nftWalletMapper;


    @Override
    public IPage<NftWallet> pageInfo(NftWalletDTO vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<NftWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(NftWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getAddressList())) {
            queryWrapper.in(NftWallet::getAddress, vo.getAddressList());
        }
        if (!StringUtils.isEmpty(vo.getLevelList())) {
            queryWrapper.in(NftWallet::getLevel, vo.getLevelList());
        }
        if (!StringUtils.isEmpty(vo.getImgList())) {
            queryWrapper.in(NftWallet::getImg, vo.getImgList());
        }
        if (!StringUtils.isEmpty(vo.getImgIdList())) {
            queryWrapper.in(NftWallet::getImgId, vo.getImgIdList());
        }
        IPage<NftWallet> iPage = nftWalletMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public List<NftWallet> findList(NftWallet vo) {
        LambdaQueryWrapper<NftWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(NftWallet::getAddress, vo.getAddress());
        }
        return nftWalletMapper.selectList(queryWrapper);
    }
}
