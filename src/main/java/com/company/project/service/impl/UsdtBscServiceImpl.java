package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.NewWallet;
import com.company.project.entity.UsdtBsc;
import com.company.project.entity.Wallet;
import com.company.project.mapper.UsdtBscMapper;
import com.company.project.service.UsdtBscService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UsdtBscServiceImpl extends ServiceImpl<UsdtBscMapper, UsdtBsc> implements UsdtBscService {

    @Resource
    private UsdtBscMapper usdtBscMapper;

    @Override
    public Wallet getAddress() {
        List<UsdtBsc> list  = usdtBscMapper.selectList(Wrappers.<UsdtBsc>lambdaQuery().eq(UsdtBsc::getStatus, 0));

        UsdtBsc usdtBsc = list.get(0);
        usdtBsc.setStatus(1);
        usdtBscMapper.updateById(usdtBsc);
        Wallet res = new Wallet();
        res.setAddress(usdtBsc.getAddress());
        res.setPrivateKey(usdtBsc.getPrivateKey());
        return res;
    }

    @Override
    public NewWallet getNewAddress() {
        List<UsdtBsc> list  = usdtBscMapper.selectList(Wrappers.<UsdtBsc>lambdaQuery().eq(UsdtBsc::getStatus, 0));

        UsdtBsc usdtBsc = list.get(0);
        usdtBsc.setStatus(1);
        usdtBscMapper.updateById(usdtBsc);
        NewWallet res = new NewWallet();
        res.setAddress(usdtBsc.getAddress());
        res.setPrivateKey(usdtBsc.getPrivateKey());
        return res;
    }


    @Override
    public IPage<UsdtBsc> pageInfo(UsdtBsc vo) {
        return null;
            }

    @Override
    public List<UsdtBsc> List(UsdtBsc vo) {
            return usdtBscMapper.selectList(null);
            }

    @Override
    public List<UsdtBsc> ListByCoin(String coin) {
            return null;
            }

    @Override
    public void addWallet(UsdtBsc vo) {
            usdtBscMapper.insert(vo);
            }

    @Override
    public void addList(List<UsdtBsc> vo) {
        for (UsdtBsc usdtBsc:vo
             ) {
            usdtBsc.setStatus(0);
            usdtBscMapper.insert(usdtBsc);
        }
    }

    @Override
    public void updateWallet(UsdtBsc usdtBsc) {
        usdtBscMapper.updateById(usdtBsc);
            }

}
