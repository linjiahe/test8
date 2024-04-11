package com.company.project.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.entity.WaKuangRewardRecord;
import com.company.project.entity.WaKuangRewardRecordTjVO;
import com.company.project.entity.WaKuangRewardRecordVO;
import com.company.project.entity.WaKuangWallet;
import com.company.project.mapper.WaKuangRewardRecordMapper;
import com.company.project.service.WaKuangRewardRecordService;
import com.company.project.service.WaKuangWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class WaKuangRewardRecordSeviceImpl extends ServiceImpl<WaKuangRewardRecordMapper, WaKuangRewardRecord> implements WaKuangRewardRecordService {

    @Resource
    private WaKuangRewardRecordMapper waKuangRewardRecordMapper;

    @Resource
    private WaKuangWalletService waKuangWalletService;

    @Override
    public List<WaKuangRewardRecord> list(WaKuangRewardRecord vo) {
        LambdaQueryWrapper<WaKuangRewardRecord> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangRewardRecord::getUserId, vo.getUserId());
        }
        queryWrapper.ge(WaKuangRewardRecord::getCreateTime, DateUtil.beginOfDay(new Date()));
        return waKuangRewardRecordMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<WaKuangRewardRecord> pageInfo(WaKuangRewardRecord vo) {
        Page page = new Page(vo.getPage(), vo.getLimit());
        LambdaQueryWrapper<WaKuangRewardRecord> queryWrapper = Wrappers.lambdaQuery();
        if (!StringUtils.isEmpty(vo.getUserId())) {
            queryWrapper.eq(WaKuangRewardRecord::getUserId, vo.getUserId());
        }
        queryWrapper.ge(WaKuangRewardRecord::getCreateTime, DateUtil.beginOfDay(new Date()));

        IPage<WaKuangRewardRecord> iPage = waKuangRewardRecordMapper.selectPage(page, queryWrapper);
        return iPage;
    }

    @Override
    public WaKuangRewardRecordTjVO tj(String userId) {

        WaKuangRewardRecordTjVO tjVO = new WaKuangRewardRecordTjVO();

        List<WaKuangRewardRecordVO> child = new ArrayList<>();

        WaKuangWallet wallet = waKuangWalletService.getOne(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "Domi")
                        .eq(WaKuangWallet::getUserName, userId)
                        .last("limit 1")
        );
        List<WaKuangWallet> childList = waKuangWalletService.list(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "Domi")
                        .eq(WaKuangWallet::getInvitationCode, wallet.getCode())
        );

        // 奖励记录
        if (CollectionUtil.isNotEmpty(childList)) {
            for (WaKuangWallet cw : childList) {
                Map<String, BigDecimal> dtMap = new HashMap<>();
                Map<String, BigDecimal> jlMap = new HashMap<>();
                Map<String, Integer> rsMap = new HashMap<>();

                dtMap.put(cw.getUserName(), cw.getBalance());
                jlMap.put(cw.getUserName(), cw.getJlBalance());
                rsMap.put(cw.getUserName(), 1);
                nextYj(cw.getUserName(), cw.getCode(), dtMap, jlMap, rsMap);

                WaKuangRewardRecordVO vo = new WaKuangRewardRecordVO();
                vo.setId(cw.getId());
                vo.setUserName(cw.getUserName());
                vo.setCode(cw.getCode());
                vo.setInvitationCode(cw.getInvitationCode());
                vo.setNumberOfPeople(rsMap.get(cw.getUserName()));
                vo.setAchievements(dtMap.get(cw.getUserName()));
                vo.setProfit(jlMap.get(cw.getUserName()));
                vo.setAddress(cw.getAddress());
                vo.setCreateTime(cw.getCreateTime());
                child.add(vo);
            }
        }

        WaKuangWallet parenWallet = waKuangWalletService.getOne(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "Domi")
                        .eq(WaKuangWallet::getCode, wallet.getInvitationCode())
                        .last("limit 1")
        );

        if (Objects.nonNull(parenWallet)) {
            tjVO.setUserId(parenWallet.getUserName());
        }

        tjVO.setNumberOfPeople(child.stream().map(WaKuangRewardRecordVO::getNumberOfPeople).reduce(0, Integer::sum));
        tjVO.setAchievements(child.stream().map(WaKuangRewardRecordVO::getAchievements).reduce(BigDecimal.ZERO, BigDecimal::add));
        tjVO.setProfit(child.stream().map(WaKuangRewardRecordVO::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add));

        tjVO.setChild(child);

        return tjVO;
    }

    /**
     * 下级业绩
     */
    private void nextYj(String userId, String code, Map<String, BigDecimal> dtMap, Map<String, BigDecimal> jlMap, Map<String, Integer> rsMap){
        List<WaKuangWallet> list = waKuangWalletService.list(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "Domi")
                        .eq(WaKuangWallet::getInvitationCode, code)
        );
        if (CollectionUtil.isNotEmpty(list) && list.size() > 0) {
            rsMap.put(userId, rsMap.get(userId) + list.size());
            for (WaKuangWallet wallet : list) {
                dtMap.put(userId, dtMap.get(userId).add(wallet.getBalance()));
                jlMap.put(userId, jlMap.get(userId).add(wallet.getJlBalance()));
                nextYj(userId, wallet.getCode(), dtMap, jlMap, rsMap);
            }
        }
    }

}
