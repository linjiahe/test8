package com.company.project.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.WaKuangJlZhuanZhangMapper;
import com.company.project.mapper.WaKuangShanduiMapper;
import com.company.project.mapper.WaKuangWalletMapper;
import com.company.project.mapper.WaKuangZhuanZhangMapper;
import com.company.project.service.*;
import java.net.URLEncoder;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WaKuangWalletServiceImpl extends ServiceImpl<WaKuangWalletMapper, WaKuangWallet> implements WaKuangWalletService {

    @Resource
    private WaKuangWalletMapper waKuangWalletMapper;

    @Resource
    private WaKuangWalletService waKuangWalletService;

    @Resource
    private WaKuangRewardRecordService waKuangRewardRecordService;

    @Resource
    private WaKuangShanduiMapper waKuangShanduiMapper;

    @Resource
    private WaKuangZhuanZhangMapper waKuangZhuanZhangMapper;

    @Resource
    private WaKuangJlZhuanZhangMapper waKuangJlZhuanZhangMapper;

    @Resource
    private WaKuangJlService waKuangJlService;

    @Resource
    private WaKuangJlWithdrawService waKuangJlWithdrawService;

    @Resource
    private WaKuangWithdrawJlService waKuangWithdrawJlService;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public IPage<WaKuangWallet> pageInfo(WaKuangWallet vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getYingsheAddress())) {
            queryWrapper.eq(WaKuangWallet::getYingsheAddress, vo.getYingsheAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        if (!StringUtils.isEmpty(vo.getUserPwd())) {
            queryWrapper.eq(WaKuangWallet::getUserPwd, vo.getUserPwd());
        }
        IPage<WaKuangWallet> iPage = waKuangWalletMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public void pageInfoExport(WaKuangWallet vo, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        if (!StringUtils.isEmpty(vo.getUserPwd())) {
            queryWrapper.eq(WaKuangWallet::getUserPwd, vo.getUserPwd());
        }
        List<WaKuangWallet> waKuangWallets = waKuangWalletMapper.selectList(queryWrapper);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WaKuangWallet.class)
                .sheet("记录")
                .doWrite(waKuangWallets);


    }

    @Override
    public IPage<WaKuangWallet> pageInfoGroupByUser(WaKuangWallet vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getAddress())) {
            queryWrapper.eq(WaKuangWallet::getAddress, vo.getAddress());
        }
        if (!StringUtils.isEmpty(vo.getInvitationCode())) {
            queryWrapper.eq(WaKuangWallet::getInvitationCode, vo.getInvitationCode());
        }

            queryWrapper.eq(WaKuangWallet::getCoin, "USDT-BEP20");

        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        if (!StringUtils.isEmpty(vo.getUserPwd())) {
            queryWrapper.eq(WaKuangWallet::getUserPwd, vo.getUserPwd());
        }
        queryWrapper.groupBy(WaKuangWallet::getUserName);
        IPage<WaKuangWallet> iPage = waKuangWalletMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public List<WaKuangWallet> List(WaKuangWallet vo) {
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        if (!StringUtils.isEmpty(vo.getCode())) {
            queryWrapper.eq(WaKuangWallet::getCode, vo.getCode());
        }
        if (!StringUtils.isEmpty(vo.getInvitationCode())) {
            queryWrapper.eq(WaKuangWallet::getInvitationCode, vo.getInvitationCode());
        }
        return waKuangWalletMapper.selectList(queryWrapper);
    }

    @Override
    public List<WaKuangWallet> ListByUser(WaKuangWallet vo) {
        LambdaQueryWrapper<WaKuangWallet> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getCoin())) {
            queryWrapper.eq(WaKuangWallet::getCoin, vo.getCoin());
        }
        if (!StringUtils.isEmpty(vo.getUserName())) {
            queryWrapper.eq(WaKuangWallet::getUserName, vo.getUserName());
        }
        if (!StringUtils.isEmpty(vo.getCode())) {
            queryWrapper.eq(WaKuangWallet::getCode, vo.getCode());
        }
        if (!StringUtils.isEmpty(vo.getInvitationCode())) {
            queryWrapper.eq(WaKuangWallet::getInvitationCode, vo.getInvitationCode());
        }
        queryWrapper.groupBy(WaKuangWallet::getUserName);
        return waKuangWalletMapper.selectList(queryWrapper);
    }

    @Override
    public List<WaKuangWallet> ListByCoin(String coin) {
        return waKuangWalletMapper.selectList(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin,coin));
    }

    @Override
    public List<WaKuangWallet> ListByCoinLimit(String coin,int limit) {
        return waKuangWalletMapper.selectList(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin,coin).orderByAsc(WaKuangWallet::getCreateTime).last("limit "+limit+",50"));
    }

    @Override
    public void addWallet(WaKuangWallet vo) {
        waKuangWalletMapper.insert(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void shandui(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangShandui vo) {
        if (waKuangWalletMapper.subBalance(vo.getUserId(), vo.getFromCoin(), vo.getFromBalance()) > 0) {

            waKuangWalletMapper.addBalance(vo.getUserId(), vo.getToCoin(), vo.getToBalance());
            waKuangShanduiMapper.insert(vo);

            // 如果兑换了DOMI,奖励百分之20
            if(toWallet.getCoin().toUpperCase().equals("DOMI")){

                // 查出上级的钱包
                WaKuangWallet parentWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, "Domi")
                        .eq(WaKuangWallet::getCode, fromWallet.getInvitationCode()).last("limit 1"));

                if(parentWallet!=null){
                    if (parentWallet.getBalance().compareTo(BigDecimal.ZERO) > 0&&parentWallet.getBalance().compareTo(BigDecimal.ZERO)>0) {
                        BigDecimal jlBalnace = new BigDecimal(0);
                        // 如果父级的金额小于当前用户的金额，拿小的
                        if (parentWallet.getBalance().compareTo(vo.getToBalance())==-1){
                            jlBalnace =parentWallet.getBalance().multiply(BigDecimal.valueOf(20)).divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_DOWN);
                        }else{
                            jlBalnace =vo.getToBalance().multiply(BigDecimal.valueOf(20)).divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_DOWN);
                        }
                        WaKuangJl jl = new WaKuangJl();
                        jl.setUserId(parentWallet.getUserName());
                        jl.setFromUserId(fromWallet.getUserName());
                        jl.setBalance(jlBalnace.divide(BigDecimal.valueOf(2)));
                        jl.setCoin("Domi");
                        jl.setSourceType(2);
                        jl.setStatus(0);
                        jl.setCreateTime(new Date());
                        jl.setUpdateTime(new Date());
                        waKuangJlService.save(jl);

                        waKuangWalletMapper.addBalance(parentWallet.getUserName(), "Domi", jlBalnace.divide(BigDecimal.valueOf(2)));
                    }
                }

                executorService.submit(() -> {
                    System.out.println("团队奖励 - 开始");
                    tdjl(fromWallet, vo);
                    waKuangWalletMapper.addTdjlBalance(vo.getUserId(), vo.getToCoin(), vo.getToBalance());
                    System.out.println("团队奖励 - 结束");
                });
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void shanduiJlDh(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangShandui vo) {
        if (waKuangWalletMapper.subJldhBalance(vo.getUserId(), vo.getFromCoin(), vo.getFromBalance()) > 0) {

            if(vo.getToCoin().equals("BNB")){
                waKuangWalletMapper.addJltxBalance(vo.getUserId(), vo.getToCoin(), vo.getToBalance());
                waKuangShanduiMapper.insert(vo);
            }else{
                waKuangWalletMapper.addBalance(vo.getUserId(), vo.getToCoin(), vo.getToBalance());
                waKuangShanduiMapper.insert(vo);
            }

            // 如果兑换了DOMI,奖励百分之20
            if(toWallet.getCoin().toUpperCase().equals("DOMI")){

                // 查出上级的钱包
                WaKuangWallet parentWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, "Domi")
                        .eq(WaKuangWallet::getCode, fromWallet.getInvitationCode()).last("limit 1"));

                if(parentWallet!=null){
                    if (parentWallet.getBalance().compareTo(BigDecimal.ZERO) > 0&&parentWallet.getBalance().compareTo(BigDecimal.ZERO)>0) {
                        BigDecimal jlBalnace = new BigDecimal(0);
                        // 如果父级的金额小于当前用户的金额，拿小的
                        if (parentWallet.getBalance().compareTo(vo.getToBalance())==-1){
                            jlBalnace =parentWallet.getBalance().multiply(BigDecimal.valueOf(20)).divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_DOWN);
                        }else{
                            jlBalnace =vo.getToBalance().multiply(BigDecimal.valueOf(20)).divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_DOWN);
                        }
                        WaKuangJl jl = new WaKuangJl();
                        jl.setUserId(parentWallet.getUserName());
                        jl.setFromUserId(fromWallet.getUserName());
                        jl.setBalance(jlBalnace.divide(BigDecimal.valueOf(2)));
                        jl.setCoin("Domi");
                        jl.setSourceType(2);
                        jl.setStatus(0);
                        jl.setCreateTime(new Date());
                        jl.setUpdateTime(new Date());
                        waKuangJlService.save(jl);

                        waKuangWalletMapper.addBalance(parentWallet.getUserName(), "Domi", jlBalnace.divide(BigDecimal.valueOf(2)));
                    }
                }

                executorService.submit(() -> {
                    System.out.println("团队奖励 - 开始");
                    tdjl(fromWallet, vo);
                    waKuangWalletMapper.addTdjlBalance(vo.getUserId(), vo.getToCoin(), vo.getToBalance());
                    System.out.println("团队奖励 - 结束");
                });
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void tdjl(WaKuangWallet fromWallet, WaKuangShandui vo) {
        List<WaKuangWallet> list = new ArrayList<>();
        String code = fromWallet.getInvitationCode();
        while(true) {
            WaKuangWallet parentWallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, "Domi")
                    .eq(WaKuangWallet::getCode, code).last("limit 1"));

                if (Objects.isNull(parentWallet)) {
                    break;
                }
            list.add(parentWallet);
            code = parentWallet.getInvitationCode();
            if (code == null || code == "") {
                break;
            }
        }
        Map<String, BigDecimal> jlMap = new HashMap<>();

        Integer totalBfl = 0;
        for (WaKuangWallet wallet : list) {
            List<WaKuangWallet> childList = waKuangWalletService.list(
                    Wrappers.<WaKuangWallet>lambdaQuery()
                            .eq(WaKuangWallet::getCoin, "Domi")
                            .eq(WaKuangWallet::getInvitationCode, wallet.getCode())
            );

            if (CollectionUtil.isNotEmpty(childList) && childList.size() > 1) {
                Map<String, BigDecimal> dtMap = new HashMap<>();
                Map<String, Integer> rsMap = new HashMap<>();
                for (WaKuangWallet cw : childList) {
                    dtMap.put(cw.getUserName(), cw.getBalance());
                    rsMap.put(cw.getUserName(), 1);
                    nextYj(cw.getUserName(), cw.getCode(), dtMap, rsMap);
                }

                // 大小区总业绩
                BigDecimal zyj = dtMap.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add);

                // 最大区业绩
                BigDecimal maxQ = dtMap.values().stream().reduce(BigDecimal.ZERO,BigDecimal::max);

                // 所有小区之和达不到奖励
                if ((zyj.subtract(maxQ)).compareTo(xqMinYj(zyj)) == -1) {
                    continue;
                }

                // 总业绩奖励百分比
                Integer zyjBfl = yjBfl(zyj);

                // 平级或者越级
                if (zyjBfl <= totalBfl) {
                    continue;
                }
//                // 奖励记录
//                WaKuangRewardRecord record = new WaKuangRewardRecord();
//                record.setUserId(wallet.getUserName());
//                record.setSubordinateUserId(wallet.getUserName());
//                record.setNumberOfPeople(rsMap.get(wallet.getUserName()));
//                record.setAchievements(dtMap.get(wallet.getUserName()));
//                record.setCreateTime(new Date());
//                record.setProfit(vo.getToBalance().multiply(BigDecimal.valueOf(bfl)).divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_DOWN));
//
//                // 批量插入奖励记录
//                waKuangRewardRecordService.save(record);
                jlMap.put(wallet.getUserName(), vo.getToBalance().multiply(BigDecimal.valueOf(zyjBfl - totalBfl)).divide(BigDecimal.valueOf(100), 3, BigDecimal.ROUND_DOWN));
                totalBfl += zyjBfl;
            }
        }

        List<WaKuangJl> jlList = jlMap.entrySet().stream().filter(
                v -> v.getValue().compareTo(BigDecimal.ZERO) == 1
        ).map(v -> {
            waKuangWalletMapper.addBalance(v.getKey(), "Domi", v.getValue().divide(BigDecimal.valueOf(2)));

            WaKuangJl jl = new WaKuangJl();
            jl.setUserId(v.getKey());
            jl.setBalance(v.getValue().divide(BigDecimal.valueOf(2)));
            jl.setCoin("Domi");
            jl.setSourceType(1);
            jl.setStatus(0);
            jl.setCreateTime(new Date());
            jl.setUpdateTime(new Date());
            return jl;
        }).collect(Collectors.toList());

        // 批量插入
        if (CollectionUtil.isNotEmpty(jlList)) {
            System.out.println("团队奖励");
            waKuangJlService.saveBatch(jlList);
        }
    }

    /**
     * 上级业绩百分比
     * @param yj
     * @return
     */
    public Integer yjBfl(
            BigDecimal yj
    ) {
        if (yj.compareTo(BigDecimal.valueOf(1000000)) >= 0) {
            return 80;
        }
        if (yj.compareTo(BigDecimal.valueOf(500000)) >= 0) {
            return 50;
        }
        if (yj.compareTo(BigDecimal.valueOf(200000)) >= 0) {
            return 40;
        }
        if (yj.compareTo(BigDecimal.valueOf(100000)) >= 0) {
            return 30;
        }
        if (yj.compareTo(BigDecimal.valueOf(20000)) >= 0) {
            return 20;
        }
        return 0;
    }

    /**
     * 小区业绩达到多少才有奖励
     * @param yj
     * @return
     */
    private BigDecimal xqMinYj(
            BigDecimal yj
    ) {
        if (yj.compareTo(BigDecimal.valueOf(1000000)) >= 0) {
            return BigDecimal.valueOf(400000);
        }
        if (yj.compareTo(BigDecimal.valueOf(500000)) >= 0) {
            return BigDecimal.valueOf(200000);
        }
        if (yj.compareTo(BigDecimal.valueOf(200000)) >= 0) {
            return BigDecimal.valueOf(60000);
        }
        if (yj.compareTo(BigDecimal.valueOf(100000)) >= 0) {
            return BigDecimal.valueOf(30000);
        }
        if (yj.compareTo(BigDecimal.valueOf(20000)) >= 0) {
            return BigDecimal.valueOf(5000);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 下级业绩
     */
    private void nextYj(String userId, String code, Map<String, BigDecimal> dtMap, Map<String, Integer> rsMap){
        List<WaKuangWallet> list = waKuangWalletMapper.selectList(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "Domi")
                        .eq(WaKuangWallet::getInvitationCode, code)
        );
        rsMap.put(userId, rsMap.get(userId) + list.size());
        for (WaKuangWallet wallet : list) {
            BigDecimal val = dtMap.get(userId).add(wallet.getTdjlBalance());
            dtMap.put(userId, val);
            nextYj(userId, wallet.getCode(), dtMap, rsMap);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void zhuanzhang(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangZhuanzhang vo) {
        fromWallet.setBalance(fromWallet.getBalance().subtract(vo.getBalance()));
        fromWallet.setUpdateTime(new Date());
        toWallet.setBalance(toWallet.getBalance().add(vo.getBalance()));
        toWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(fromWallet);
        waKuangWalletMapper.updateById(toWallet);
        vo.setType(0);
        waKuangZhuanZhangMapper.insert(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void zhuanzhangKzy(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangZhuanzhang vo) {
        fromWallet.setKzyBalance(fromWallet.getKzyBalance().subtract(vo.getBalance()));
        fromWallet.setUpdateTime(new Date());
        toWallet.setKzyBalance(toWallet.getKzyBalance().add(vo.getBalance()));
        toWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(fromWallet);
        waKuangWalletMapper.updateById(toWallet);
        vo.setType(1);
        waKuangZhuanZhangMapper.insert(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void zhuanzhangByDappJltx(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangZhuanzhang vo) {
        fromWallet.setJltxBalance(fromWallet.getJltxBalance().subtract(vo.getBalance()));
        fromWallet.setUpdateTime(new Date());
        toWallet.setJltxBalance(toWallet.getJltxBalance().add(vo.getBalance()));
        toWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(fromWallet);
        waKuangWalletMapper.updateById(toWallet);
        vo.setType(2);
        waKuangZhuanZhangMapper.insert(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void zhuanzhangByDappJldh(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangZhuanzhang vo) {
        fromWallet.setJldhBalance(fromWallet.getJldhBalance().subtract(vo.getBalance()));
        fromWallet.setUpdateTime(new Date());
        toWallet.setJldhBalance(toWallet.getJldhBalance().add(vo.getBalance()));
        toWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(fromWallet);
        waKuangWalletMapper.updateById(toWallet);
        vo.setType(3);
        waKuangZhuanZhangMapper.insert(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void zhuanzhangJl(WaKuangWallet fromWallet,WaKuangWallet toWallet,WaKuangJlZhuanzhang vo) {
        fromWallet.setJlBalance(fromWallet.getJlBalance().subtract(vo.getBalance()));
        fromWallet.setUpdateTime(new Date());
        toWallet.setJlBalance(toWallet.getJlBalance().add(vo.getBalance()));
        toWallet.setUpdateTime(new Date());
        waKuangWalletMapper.updateById(fromWallet);
        waKuangWalletMapper.updateById(toWallet);
        waKuangJlZhuanZhangMapper.insert(vo);
    }

    @Override
    public void updateWallet(WaKuangWallet wallet) {
        waKuangWalletMapper.updateById(wallet);
    }

    @Override
    public boolean addBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.addBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean subBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.subBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean addJlTxBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.addJltxBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean addKjBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.addKjBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean subJlDhBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.subJldhBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean addJlDhBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.addJldhBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean subJlTxBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.subJltxBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean addZtBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.addZtBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean subZtBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.subZtBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean addJlBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.addJlBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean subJlBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.subJlBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean addZyjlBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.addZyjlBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean subZyjlBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.subZyjlBalance(userId, coin, balance) > 0;
    }

    @Override
    public boolean returnZyBalance(String userId, String coin, BigDecimal balance) {
        return waKuangWalletMapper.returnZyBalance(userId, coin, balance) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdraw(WaKuangJlWithdraw waKuangJlWithdraw) {
        String userId = waKuangJlWithdraw.getUserId();
        WaKuangWallet wallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, userId).eq(WaKuangWallet::getCoin, "Domi").last("limit 1"));
        if (Objects.nonNull(wallet)) {

            BigDecimal balance = wallet.getJlBalance().add(wallet.getZtBalance()).add(wallet.getZyjlBalance());
            if (balance.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }

            Date nowDate = new Date();

            if (waKuangWalletMapper.subJlBalance(userId, "Domi", wallet.getJlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(1);
                jl.setSourceType(1);
                jl.setCreateTime(nowDate);
                jl.setUpdateTime(nowDate);
                jl.setBalance(wallet.getJlBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (waKuangWalletMapper.subZtBalance(userId, "Domi", wallet.getZtBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(1);
                jl.setSourceType(2);
                jl.setCreateTime(nowDate);
                jl.setUpdateTime(nowDate);
                jl.setBalance(wallet.getZtBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (waKuangWalletMapper.subZyjlBalance(userId, "Domi", wallet.getZyjlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(1);
                jl.setSourceType(3);
                jl.setCreateTime(nowDate);
                jl.setUpdateTime(nowDate);
                jl.setBalance(wallet.getZyjlBalance());
                waKuangWithdrawJlService.save(jl);
            }

            // 手续费为百分之1
            BigDecimal sxfBalance = balance.multiply(BigDecimal.valueOf(0.01));
            balance = balance.subtract(sxfBalance);

            WaKuangJlWithdraw jlWithdraw = new WaKuangJlWithdraw();
            jlWithdraw.setCoin("Domi");
            jlWithdraw.setUserId(wallet.getUserName());
            jlWithdraw.setStatus(0);
            jlWithdraw.setBalance(balance);
            jlWithdraw.setSxfBalance(sxfBalance);
            jlWithdraw.setAddress(waKuangJlWithdraw.getAddress());
            jlWithdraw.setRemark("");
            jlWithdraw.setCreateTime(nowDate);
            jlWithdraw.setUpdateTime(nowDate);

            return waKuangJlWithdrawService.save(jlWithdraw);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdraw2(WaKuangJlWithdrawVO vo) {
        String userId = vo.getUserId();
        WaKuangWallet wallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, userId).eq(WaKuangWallet::getCoin, "Domi").last("limit 1"));
        if (Objects.nonNull(wallet)) {

            BigDecimal balance = vo.getJlBalance().add(vo.getZtBalance()).add(vo.getZyjlBalance());
            if (balance.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }

            Date nowDate = new Date();

            if (vo.getJlBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangWalletMapper.subJlBalance(userId, "Domi", vo.getJlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(1);
                jl.setSourceType(1);
                jl.setCreateTime(nowDate);
                jl.setUpdateTime(nowDate);
                jl.setBalance(vo.getJlBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (vo.getZtBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangWalletMapper.subZtBalance(userId, "Domi", vo.getZtBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(1);
                jl.setSourceType(2);
                jl.setCreateTime(nowDate);
                jl.setUpdateTime(nowDate);
                jl.setBalance(vo.getZtBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (vo.getZyjlBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangWalletMapper.subZyjlBalance(userId, "Domi", vo.getZyjlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(1);
                jl.setSourceType(3);
                jl.setCreateTime(nowDate);
                jl.setUpdateTime(nowDate);
                jl.setBalance(vo.getZyjlBalance());
                waKuangWithdrawJlService.save(jl);
            }

            // 手续费为百分之1
            BigDecimal sxfBalance = balance.multiply(BigDecimal.valueOf(0.01));
            balance = balance.subtract(sxfBalance);

            WaKuangJlWithdraw jlWithdraw = new WaKuangJlWithdraw();
            jlWithdraw.setCoin("Domi");
            jlWithdraw.setUserId(wallet.getUserName());
            jlWithdraw.setStatus(0);
            jlWithdraw.setBalance(balance);
            jlWithdraw.setSxfBalance(sxfBalance);
            jlWithdraw.setAddress(vo.getAddress());
            jlWithdraw.setRemark("");
            jlWithdraw.setCreateTime(nowDate);
            jlWithdraw.setUpdateTime(nowDate);

            return waKuangJlWithdrawService.save(jlWithdraw);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean jlShandui(String userId, BigDecimal balance) {
        WaKuangWallet wallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, userId).eq(WaKuangWallet::getCoin, "Domi").last("limit 1"));
        if (Objects.nonNull(wallet)) {

            BigDecimal formBalance = wallet.getJlBalance().add(wallet.getZtBalance()).add(wallet.getZyjlBalance());
            if (formBalance.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }

            if (waKuangWalletMapper.shanduiJlBalance(userId, "Domi", wallet.getJlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(2);
                jl.setSourceType(1);
                jl.setCreateTime(new Date());
                jl.setUpdateTime(new Date());
                jl.setBalance(wallet.getJlBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (waKuangWalletMapper.shanduiZtBalance(userId, "Domi", wallet.getZtBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(2);
                jl.setSourceType(2);
                jl.setCreateTime(new Date());
                jl.setUpdateTime(new Date());
                jl.setBalance(wallet.getZtBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (waKuangWalletMapper.shanduiZyjlBalance(userId, "Domi", wallet.getZyjlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(userId);
                jl.setCoin("Domi");
                jl.setTypes(2);
                jl.setSourceType(3);
                jl.setCreateTime(new Date());
                jl.setUpdateTime(new Date());
                jl.setBalance(wallet.getZyjlBalance());
                waKuangWithdrawJlService.save(jl);
            }

            if (waKuangWalletMapper.addBalance(userId, "DomiDao", balance) > 0) {
                // 闪兑记录
                WaKuangShandui shandui = new WaKuangShandui();
                shandui.setUserId(userId);
                shandui.setFromCoin("Domi");
                shandui.setFromBalance(formBalance);
                shandui.setToCoin("DomiDao");
                shandui.setToBalance(balance);
                shandui.setCreateTime(new Date());
                shandui.setUpdateTime(new Date());
                waKuangShanduiMapper.insert(shandui);
                return true;
            }

        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean jlShandui(WaKuangShanduiVO vo) {
        WaKuangWallet wallet = waKuangWalletMapper.selectOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserId()).eq(WaKuangWallet::getCoin, "Domi").last("limit 1"));
        if (Objects.nonNull(wallet)) {

            BigDecimal formBalance = vo.getJlBalance().add(vo.getZtBalance()).add(vo.getZyjlBalance());
            if (formBalance.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }

            if (vo.getJlBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangWalletMapper.shanduiJlBalance(vo.getUserId(), "Domi", vo.getJlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(vo.getUserId());
                jl.setCoin("Domi");
                jl.setTypes(2);
                jl.setSourceType(1);
                jl.setCreateTime(new Date());
                jl.setUpdateTime(new Date());
                jl.setBalance(vo.getJlBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (vo.getZtBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangWalletMapper.shanduiZtBalance(vo.getUserId(), "Domi", vo.getZtBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(vo.getUserId());
                jl.setCoin("Domi");
                jl.setTypes(2);
                jl.setSourceType(2);
                jl.setCreateTime(new Date());
                jl.setUpdateTime(new Date());
                jl.setBalance(vo.getZtBalance());
                waKuangWithdrawJlService.save(jl);
            }
            if (vo.getZyjlBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangWalletMapper.shanduiZyjlBalance(vo.getUserId(), "Domi", vo.getZyjlBalance()) > 0) {
                WaKuangWithdrawJl jl = new WaKuangWithdrawJl();
                jl.setUserId(vo.getUserId());
                jl.setCoin("Domi");
                jl.setTypes(2);
                jl.setSourceType(3);
                jl.setCreateTime(new Date());
                jl.setUpdateTime(new Date());
                jl.setBalance(vo.getZyjlBalance());
                waKuangWithdrawJlService.save(jl);
            }

            if (waKuangWalletMapper.addBalance(vo.getUserId(), "DomiDao", vo.getBalance()) > 0) {
                // 闪兑记录
                WaKuangShandui shandui = new WaKuangShandui();
                shandui.setUserId(vo.getUserId());
                shandui.setFromCoin("Domi");
                shandui.setFromBalance(formBalance);
                shandui.setToCoin("DomiDao");
                shandui.setToBalance(vo.getBalance());
                shandui.setCreateTime(new Date());
                shandui.setUpdateTime(new Date());
                waKuangShanduiMapper.insert(shandui);
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean resetTdjlBalance() {
        return waKuangWalletMapper.resetTdjlBalance() > 0;
    }

    @Override
    public DataResult czBep20(WaKuangWallet dto) {
        return null;
    }

    @Override
    public boolean setType(String userId, Integer type) {
        return waKuangWalletMapper.setType(userId, type) > 0;
    }

    @Override
    public boolean setLevel(String userId, Integer level) {
        return waKuangWalletMapper.setLevel(userId, level) > 0;
    }

    @Override
    public void resetLevel() {
        waKuangWalletMapper.resetLevel();
    }

    @Override
    public int setTotailJtjlZyBalance(String userId, BigDecimal xjzyBalance, BigDecimal xjsyBalance) {
        return waKuangWalletMapper.setTotailJtjlZyBalance(userId, xjzyBalance, xjsyBalance);
    }

    @Override
    public Boolean updateLevelBatch(ArrayList<WaKuangWallet> waKuangWalletList) {
        return waKuangWalletMapper.updateLevelBatch(waKuangWalletList)>0;
    }

}
