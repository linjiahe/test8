package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.NewWallet;
import com.company.project.mapper.NewWalletMapper;
import com.company.project.service.NewWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class NewWalletServiceImpl extends ServiceImpl<NewWalletMapper, NewWallet> implements NewWalletService {

    @Resource
    private NewWalletMapper newWalletMapper;


    @Override
    public IPage<NewWallet> pageInfo(NewWallet vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<NewWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(NewWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(NewWallet::getUserId, vo.getUserId());
        }
        IPage<NewWallet> iPage = newWalletMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public List<NewWallet> List(NewWallet vo) {
        LambdaQueryWrapper<NewWallet> queryWrapper = Wrappers.lambdaQuery();
        if (vo.getUserId()!=null){
            queryWrapper.eq(NewWallet::getUserId,vo.getUserId());
        }
        if (vo.getInvitationCode()!=null){
            queryWrapper.eq(NewWallet::getInvitationCode,vo.getInvitationCode());
        }
        if (vo.getAddress()!=null){
            queryWrapper.eq(NewWallet::getAddress,vo.getAddress());
        }
        return newWalletMapper.selectList(queryWrapper);
    }

    @Override
    public List<NewWallet> ListByCoin(String coin) {
        return newWalletMapper.selectList(Wrappers.<NewWallet>lambdaQuery().eq(NewWallet::getCoin,coin));
    }



    @Override
    public void addWallet(NewWallet vo) {
        newWalletMapper.insert(vo);
    }

    @Override
    public void updateWallet(NewWallet wallet) {
        newWalletMapper.updateById(wallet);
    }

}
