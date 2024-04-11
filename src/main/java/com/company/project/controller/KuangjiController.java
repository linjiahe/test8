package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/data/kj")
@Slf4j
public class KuangjiController {
    @Resource
    private KuangjiMapper kuangjiMapper;

    @Resource
    private WaKuangWalletMapper waKuangWalletMapper;

    @Resource
    private KuangjiGailanMapper kuangjiGailanMapper;

    @Resource
    private KuangjiUserMapper kuangjiUserMapper;



    /**
     * 矿机列表
     * @param vo
     * @return
     */
    @PostMapping("/pageInfo")
    public DataResult PageInfo(@RequestBody Kuangji vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<Kuangji> queryWrapper = Wrappers.lambdaQuery();
        IPage<Kuangji> iPage = kuangjiMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

    /**
     * 矿机购买列表
     * @param vo
     * @return
     */
    @PostMapping("/user/pageInfo")
    public DataResult kjUserPageInfo(@RequestBody KuangjiUser vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<KuangjiUser> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(KuangjiUser::getUserId, vo.getUserId());
        }
        IPage<KuangjiUser> iPage = kuangjiUserMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

    /**
     * 转账记录
     * @param vo
     * @return
     */
    @PostMapping("/yingshe/pageInfo")
    public DataResult zhuanzhangPageInfo(@RequestBody WaKuangWallet vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        if (!StringUtils.isEmpty(vo.getYingsheAddress())) {
            queryWrapper.eq(WaKuangWallet::getYingsheAddress, vo.getYingsheAddress());
        }
        queryWrapper.groupBy(WaKuangWallet::getUserName);
        IPage<WaKuangWallet> iPage = waKuangWalletMapper.selectPage(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @PostMapping("/add")
    public DataResult add(@RequestBody Kuangji vo) {
        if(vo.getCount()<=0){
            return DataResult.fail("矿机数量不能为0");
        }
        vo.setStatus(0);
        vo.setSyCount(vo.getCount());
        vo.setUpdateTime(new Date());
        return DataResult.success(kuangjiMapper.insert(vo));
    }

    @PostMapping("/user/add")
    public DataResult add(@RequestBody KuangjiUser vo) {

        vo.setGdjl(new BigDecimal(4));
        vo.setBalance(new BigDecimal(2000));
        vo.setDay(1640);

        WaKuangWallet waKuangWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getYingsheAddress, vo.getUserId()).eq(WaKuangWallet::getCoin,"Domi"));
        if(waKuangWallet==null){
            return DataResult.fail("用户未绑定domi地址");
        }
        if (waKuangWallet.getJltxBalance().compareTo(vo.getBalance()) == -1) {
            return DataResult.fail("Domi余额不足");
        }



        // 扣除用户资产
        if(waKuangWalletMapper.subJltxBalance(waKuangWallet.getUserName(),"Domi",vo.getBalance())<= 0){
            return DataResult.fail("系统繁忙");
        }
        vo.setCreateTime(new Date());
        vo.setUpdateTime(new Date());
        kuangjiUserMapper.insert(vo);
        return DataResult.success();
    }

    @PostMapping("/tj/add")
    public DataResult tjAdd(@RequestBody WaKuangWallet vo) {

        List<WaKuangWallet> list = waKuangWalletMapper.selectList(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserName()));
        if(list.size()<=0){
            return DataResult.fail("用户不存在");
        }
        for (WaKuangWallet data:list
             ) {
            data.setJiedianCode(vo.getJiedianCode());
            waKuangWalletMapper.updateById(data);
        }
        return DataResult.success();
    }

    @PostMapping("/tj/jiedianSet")
    public DataResult jiedianSet(@RequestBody WaKuangWallet vo) {

        List<WaKuangWallet> list = waKuangWalletMapper.selectList(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserName()));
        if(list.size()<=0){
            return DataResult.fail("用户不存在");
        }
        for (WaKuangWallet data:list
        ) {
            data.setJiedianCode(vo.getJiedianCode());
            waKuangWalletMapper.updateById(data);
        }
        return DataResult.success();
    }

    @PostMapping("/gailan")
    public DataResult KuangjiGailan() {

        KuangjiGailan kuangjiGailan = kuangjiGailanMapper.selectOne(Wrappers.<KuangjiGailan>lambdaQuery());
        return DataResult.success(kuangjiGailan);
    }


    @PostMapping("/setGailan")
    public DataResult setKuangjiGailan(@RequestBody KuangjiGailan data) {
        KuangjiGailan kuangjiGailan = kuangjiGailanMapper.selectOne(Wrappers.<KuangjiGailan>lambdaQuery());
        if(kuangjiGailan!=null){
            kuangjiGailanMapper.deleteById(kuangjiGailan);
        }
        return DataResult.success(kuangjiGailanMapper.insert(data));
    }

    @PostMapping("/tj/jiedianSetLevel")
    public DataResult jiedianSetLevel(@RequestBody WaKuangWallet vo) {
        List<WaKuangWallet> list = waKuangWalletMapper.selectList(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserName()));
        if(list.size()<=0){
            return DataResult.fail("用户不存在");
        }
        for (WaKuangWallet data:list
        ) {
            data.setJiedianLevel(vo.getJiedianLevel());
            waKuangWalletMapper.updateById(data);
        }
        return DataResult.success();
    }

    @PostMapping("/yingshe")
    public DataResult yingshe(@RequestBody WaKuangWallet vo) {

        List<WaKuangWallet> list = waKuangWalletMapper.selectList(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserName()));
        for (WaKuangWallet data:list
        ) {
            data.setYingsheAddress(vo.getYingsheAddress());
            waKuangWalletMapper.updateById(data);
        }
        return DataResult.success();
    }


}
