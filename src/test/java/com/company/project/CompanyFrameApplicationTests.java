package com.company.project;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.PasswordUtils;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.WaKuangZy;
import com.company.project.mapper.WaKuangZyMapper;
import com.company.project.service.WaKuangJlService;
import com.company.project.service.WaKuangWalletService;
import com.company.project.service.WaKuangZyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CompanyFrameApplicationTests {

    @Resource
    private WaKuangZyMapper waKuangZyMapper;

    @Resource
    private WaKuangJlService waKuangJlService;

    @Resource
    private WaKuangWalletService waKuangWalletService;

    @Resource
    private WaKuangZyService waKuangZyService;

    private final static String TOP_CODE = "apfs2x4f";
    @Test
    public void run() throws Exception {
        /**
         * 挖矿的团队收益，极差制
         * D1 个人500u. 团队1万u 10%
         * D2 个人1000u. 团队3万u 20%
         * D3 个人2000u. 团队10万u 30%
         * D4 个人5000u. 团队50万u  40%
         * D5 个人10000u. 团队150万u 50%
         * D6 个人15000u. 团队450万u 60%
         * D7 个人20000u. 团队1350万u 70%
         * *团队：是指伞下全部加起来
         * *触发：（每天12点固定触发）
         * 1.就是说我如果达到D1的级别，我自己最少是投入500u，下面整个团队加起来到了1万u，
         * 那么我就能拿到我下面所有人每天静态产出的10%，假设我下面总共有11000u，不管静态收益是0.8%还是1.5%，总产出如果是110个的话，我就拿10个。
         * 2.就是说我如果达到D1的级别，我自己最少是投入500u，下面整个团队加起来到了1万u，
         * 那么我就能拿到我下面所有人每天静态产出的10%，假设我下面总共有11000u，不管静态收益是0.8%还是1.5%，总产出如果是110个的话，我就拿11个。\
         *
         * 平级：
         * 举例：如果我是A，我推荐了一个B，A入金500u，总业绩达到1万u我就是D1，如果B也入金是500，他下面的我到1万u，那么我俩就平级了，我如果没有推荐其他人，动态我就只能拿B收入的10%.
         *
         * 越级没有团队收益，平级就拿自己下一代收益的10%
         *
         *
         * --------------------------------------------------------
         * A入金1000u，直推了B1（1000u）和B2（2000u），
         * B1直推了C1（5000u）和C2（50000u），B2直推（5000u），算A的收入
         *
         *
         * A	1000
         * B1	1000	C1	5000	C2	5W
         * B2	2000	E1	5000
         *
         * A、B1平级D2	20%
         * B2 没有级别	0
         *
         *   (C1 + C2) * 1% * 10% * 20% +（B2 + E1）* 1% * 10% * （20% - 0）
         * = 55000 * 1% * 10% * 20% + 7000 * 1% * 10%* 20%
         * = 11 + 1.4
         * = 12.5
         *
         */

        //查询质押记录表中质押货币为USDT-BEP20未归还并且历史质押总额>=500的用户ID
        List<String> userIds = waKuangZyMapper.tdjlids();
        if (CollectionUtil.isEmpty(userIds)) {
            return;
        }
        List<WaKuangWallet> waKuangWallets = waKuangWalletService.list(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "USDT-BEP20")
                        .in(WaKuangWallet::getUserName, userIds)
        );

        if (CollectionUtil.isEmpty(waKuangWallets)) {
            return;
        }
        //用户个人质押Map
        ConcurrentHashMap<String, BigDecimal> pledgeMap = new ConcurrentHashMap<>();
        //用户个人质押Map
        ConcurrentHashMap<String, BigDecimal> staticMap = new ConcurrentHashMap<>();
        //用户个人等级
        ConcurrentHashMap<String, BigDecimal> gradeMap = new ConcurrentHashMap<>();
        //团队总质押
        ConcurrentHashMap<String, BigDecimal> teamPledgeMap = new ConcurrentHashMap<>();
        //团队总质押
        ConcurrentHashMap<String, BigDecimal> teamNumMap = new ConcurrentHashMap<>();
        //团队总静态
        ConcurrentHashMap<String, BigDecimal> teamStaticMap = new ConcurrentHashMap<>();
        //用户的上级
        ConcurrentHashMap<String, String> parentMap = new ConcurrentHashMap<>();
        //用户奖励
        ConcurrentHashMap<String, BigDecimal> awardMap = new ConcurrentHashMap<>();
        //用户越级奖励
        ConcurrentHashMap<String, BigDecimal> surpassAwardMap = new ConcurrentHashMap<>();
        //用户下级平级用户名
        ConcurrentHashMap<String, String> extraStaticMap = new ConcurrentHashMap<>();
        //用户奖励额度
        ConcurrentHashMap<String, WaKuangZy> awardCreditMap = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(waKuangWallets.size());
        //循环用户列表
        for (WaKuangWallet waKuangWallet : waKuangWallets) {
            ThreadUtil.execAsync(() -> {
                try {
                    //团队质押
                    BigDecimal teamPledge = BigDecimal.ZERO;
                    BigDecimal teamNum = BigDecimal.ZERO;
                    //静态质押
                    BigDecimal staticPledge = BigDecimal.ZERO;
                    if (!parentMap.containsKey(StrUtil.isNotBlank(waKuangWallet.getUserName()) ? waKuangWallet.getUserName() : "空用户名")) {
                        parentMap.put(StrUtil.isNotBlank(waKuangWallet.getUserName()) ? waKuangWallet.getUserName() : "空用户名", " ");
                    }
                    //查询下层用户
                    Map<String, BigDecimal> map = calculate(staticMap, teamStaticMap, awardCreditMap, pledgeMap, gradeMap, teamPledgeMap, teamNumMap, parentMap, waKuangWallet, teamPledge, teamNum, staticPledge, StrUtil.isNotBlank(waKuangWallet.getUserName()) ? waKuangWallet.getUserName() : "空用户名");
                    teamPledge = map.get("parentTeamPledge");
                    teamNum = map.get("parentTeamNum");
                    staticPledge = map.get("parentTeamStatic");
                } finally {
                    countDownLatch.countDown();
                }
            });

        }
        countDownLatch.await();
        List<String> usernames = new ArrayList<>(gradeMap.keySet());
        //循环所有质押用户
        for (String username : usernames) {
            awardMap.put(username, BigDecimal.ZERO);
            surpassAwardMap.put(username, BigDecimal.ZERO);
            calculateAward(staticMap, teamStaticMap, extraStaticMap, pledgeMap, gradeMap, teamPledgeMap, parentMap, awardMap, username, username, BigDecimal.ZERO, surpassAwardMap);
        }
        ConcurrentHashMap<String, BigDecimal> awardMap2 = new ConcurrentHashMap<>(awardMap);
        if (surpassAwardMap.size() > 0) {
            surpassAwardMap.forEach((key, value) -> {
                awardMap2.put(key, awardMap2.get(key).add(value));
            });
        }
        extraStaticMap.forEach((key, value) -> {
            if (!value.equals(",") && StrUtil.isNotBlank(value)) {
                List<String> user = (List<String>) Arrays.asList(value.replaceFirst(",", "").split("-"));
                if (user.size() > 0) {
                    for (String u : user) {
                        BigDecimal award = awardMap.get(u);
                        if (ObjectUtil.isNotNull(award) && award.compareTo(BigDecimal.ZERO) > 0) {
                            awardMap2.put(key, awardMap2.get(key).add(award.multiply(new BigDecimal("0.1"))));
                        }
                    }
                }
            }
        });
        List<WaKuangJl> waKuangJls = new ArrayList<>();
        awardMap2.forEach((username, award) -> {
            if (award.compareTo(BigDecimal.ZERO) > 0) {
                WaKuangZy waKuangZy = awardCreditMap.get(username);
                if (waKuangZy.getJlBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangZy.getJlBalance().compareTo(award) > 0) {
                    WaKuangJl waKuangJl = new WaKuangJl();
                    waKuangJl.setUserId(username);
                    waKuangJl.setCoin("Domi");
                    waKuangJl.setBalance(award);
                    waKuangJl.setSourceType(1);
                    waKuangJl.setStatus(0);
                    waKuangJl.setCreateTime(new Date());
                    waKuangJls.add(waKuangJl);
                } else if (waKuangZy.getJlBalance().compareTo(BigDecimal.ZERO) > 0 && waKuangZy.getJlBalance().compareTo(award) <= 0) {
                    WaKuangJl waKuangJl = new WaKuangJl();
                    waKuangJl.setUserId(username);
                    waKuangJl.setCoin("Domi");
                    waKuangJl.setBalance(waKuangZy.getJlBalance());
                    waKuangJl.setSourceType(1);
                    waKuangJl.setStatus(0);
                    waKuangJl.setCreateTime(new Date());
                    waKuangJls.add(waKuangJl);
                }
            }
        });
        if (waKuangJls.size() > 0) {
            waKuangJlService.saveBatch(waKuangJls);
            //搜索得到奖励的用户质押记录
            List<WaKuangZy> waKuangZyList = waKuangZyMapper.selectNotReturnByUserIdsAndCoin(waKuangJls, "USDT-BEP20");
            if (CollectionUtil.isNotEmpty(waKuangZyList) && waKuangZyList.size() > 0) {
                List<WaKuangZy> waKuangZyList2 = waKuangZyList.stream().map(waKuangZy -> {
                    WaKuangZy waKuangZy2 = new WaKuangZy();
                    BeanUtil.copyProperties(waKuangZy, waKuangZy2);
                    waKuangZy2.setId(waKuangZy.getId());
                    log.info(waKuangZy.getUserId());
                    waKuangZy2.setJlBalance(waKuangZy.getJlBalance().subtract(awardMap2.get(waKuangZy.getUserId())));
                    if (waKuangZy2.getJlBalance().compareTo(BigDecimal.ZERO) == 0) {
                        waKuangZy2.setIsReturn(1);
                    } else if (waKuangZy2.getJlBalance().compareTo(BigDecimal.ZERO) < 0) {
                        waKuangZy2.setJlBalance(BigDecimal.ZERO);
                        waKuangZy2.setIsReturn(1);
                    }
                    waKuangZy2.setUpdateTime(new Date());
                    return waKuangZy2;
                }).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(waKuangZyList2) && waKuangZyList2.size() > 0) {
                    waKuangZyService.updateBatchById(waKuangZyList2);
                }
            }
        }
        ArrayList<WaKuangWallet> waKuangWalletList = new ArrayList<>();
        //更新用户Level
        gradeMap.forEach((username, grade) -> {
            WaKuangWallet waKuangWallet = new WaKuangWallet();
            waKuangWallet.setUserName(username);
            BigDecimal num = teamNumMap.get(username);
            if (ObjectUtil.isNotNull(num))
                waKuangWallet.setSubNum(num.longValue());
            BigDecimal pledge = teamPledgeMap.get(username);
            if (ObjectUtil.isNotNull(pledge))
                waKuangWallet.setSubPledge(pledge);
            waKuangWallet.setCoin("USDT-BEP20");
            waKuangWallet.setLevel(grade.multiply(new BigDecimal("10")).intValue());
            waKuangWallet.setUpdateTime(new Date());
            waKuangWalletList.add(waKuangWallet);
        });
        if (waKuangWalletList.size() > 0) {
            waKuangWalletService.updateLevelBatch(waKuangWalletList);
        }


    }

    //获取静态比例
    private BigDecimal staticRatio(BigDecimal zyBalance) {
        if (zyBalance.compareTo(BigDecimal.valueOf(10000)) == 1) {
            return BigDecimal.valueOf(0.015);
        } else if (zyBalance.compareTo(BigDecimal.valueOf(5000)) == 1) {
            return BigDecimal.valueOf(0.013);
        } else if (zyBalance.compareTo(BigDecimal.valueOf(2000)) == 1) {
            return BigDecimal.valueOf(0.012);
        } else if (zyBalance.compareTo(BigDecimal.valueOf(1000)) == 1) {
            return BigDecimal.valueOf(0.011);
        } else if (zyBalance.compareTo(BigDecimal.valueOf(100)) >= 0) {
            return BigDecimal.valueOf(0.01);
        } else {
            return BigDecimal.ZERO;
        }
    }


    private void calculateAward(ConcurrentHashMap<String, BigDecimal> staticMap,
                                ConcurrentHashMap<String, BigDecimal> teamStaticMap,
                                ConcurrentHashMap<String, String> extraStaticMap,
                                ConcurrentHashMap<String, BigDecimal> pledgeMap,
                                ConcurrentHashMap<String, BigDecimal> gradeMap,
                                ConcurrentHashMap<String, BigDecimal> teamPledgeMap,
                                ConcurrentHashMap<String, String> parentMap,
                                ConcurrentHashMap<String, BigDecimal> awardMap,
                                String username, String topUser,
                                BigDecimal maxGrade,
                                ConcurrentHashMap<String, BigDecimal> surpassAwardMap) {
        //用户质押个人金额
        BigDecimal userPledge = pledgeMap.get(username);
        //用户个人现有等级
        BigDecimal userGrade = gradeMap.get(username);
        //用户名下团队质押总额
        BigDecimal userTeamPledge = teamPledgeMap.get(username);
        //用户上级
        String userParent = StrUtil.isEmpty(parentMap.get(username)) ? "Z" : parentMap.get(username);


        if (!extraStaticMap.containsKey(topUser))
            extraStaticMap.put(topUser, ",");


        BigDecimal grade = calculateGrade(userPledge, userTeamPledge);
        if (userGrade.compareTo(grade) < 0 && userGrade.compareTo(BigDecimal.valueOf(-0.1)) > 0) {
            userGrade = grade;
            gradeMap.put(username, userGrade);
        }
        if (userGrade.compareTo(BigDecimal.valueOf(-0.1)) == 0) {
            gradeMap.put(username, BigDecimal.ZERO);
        }
        if (!username.equals(topUser) && userGrade.compareTo(maxGrade) > 0) {
            maxGrade = userGrade;
        }
        //获取用户下级名
        List<String> subUsers = getKeyList(parentMap, username);
        //如果没有下级,说明当前用户是底层用户
        if (subUsers.size() == 0 && !username.equals(topUser) && gradeMap.get(topUser).compareTo(BigDecimal.ZERO) > 0) {
            if (userParent.equals(topUser)) {
                if (userGrade.compareTo(gradeMap.get(topUser)) < 0) {
                    awardMap.put(topUser, awardMap.get(topUser).add((gradeMap.get(topUser).subtract(userGrade)).multiply(staticMap.get(username))));
                } else if (userGrade.compareTo(gradeMap.get(topUser)) > 0) {
                    surpassAwardMap.put(topUser, surpassAwardMap.get(topUser).add(new BigDecimal("0.1").multiply(staticMap.get(username))));
                }
            } else {
                if (maxGrade.compareTo(BigDecimal.ZERO) == 0) {
                    awardMap.put(topUser, awardMap.get(topUser).add((gradeMap.get(topUser).subtract(maxGrade)).multiply(staticMap.get(username))));
                } else {
                    if (maxGrade.compareTo(gradeMap.get(topUser)) < 0) {
                        awardMap.put(topUser, awardMap.get(topUser).add((gradeMap.get(topUser).subtract(maxGrade)).multiply(staticMap.get(username))));
                    } else if (maxGrade.compareTo(gradeMap.get(topUser)) > 0) {
                        surpassAwardMap.put(topUser, surpassAwardMap.get(topUser).add(new BigDecimal("0.1").multiply(staticMap.get(username))));
                    }
                }
            }
        }   //如果用户有下级,持续递归
        else if (subUsers.size() > 0 && gradeMap.get(topUser).compareTo(BigDecimal.ZERO) > 0) {
            if (!username.equals(topUser)) {
                if (maxGrade.compareTo(gradeMap.get(topUser)) > 0) {
                    surpassAwardMap.put(topUser, surpassAwardMap.get(topUser).add(new BigDecimal("0.1").multiply(staticMap.get(username))));
                } else if (maxGrade.compareTo(gradeMap.get(topUser)) == 0) {
                    String usernames = extraStaticMap.get(topUser);
                    extraStaticMap.put(topUser, usernames + "-" + username);
                } else {
                    awardMap.put(topUser, awardMap.get(topUser).add((gradeMap.get(topUser).subtract(maxGrade)).multiply(staticMap.get(username))));
                    for (String subUser : subUsers) {
                        calculateAward(staticMap, teamStaticMap, extraStaticMap, pledgeMap, gradeMap, teamPledgeMap, parentMap, awardMap, subUser, topUser, maxGrade, surpassAwardMap);
                    }
                }
            } else {
                for (String subUser : subUsers) {
                    calculateAward(staticMap, teamStaticMap, extraStaticMap, pledgeMap, gradeMap, teamPledgeMap, parentMap, awardMap, subUser, topUser, BigDecimal.ZERO, surpassAwardMap);
                }
            }

        }
    }

    private boolean isPeer(ConcurrentHashMap<String, BigDecimal> pledgeMap, ConcurrentHashMap<String, BigDecimal> gradeMap, ConcurrentHashMap<String, BigDecimal> teamPledgeMap, ConcurrentHashMap<String, String> parentMap, String username, BigDecimal maxGrade) {
        List<String> allKeyList = getAllKeyList(parentMap, username);
        if (allKeyList.size() > 0) {
            for (String key : allKeyList) {
                BigDecimal grade = gradeMap.get(key);
                if (grade.compareTo(calculateGrade(pledgeMap.get(key), teamPledgeMap.get(key))) < 0&& grade.compareTo(BigDecimal.valueOf(-0.1)) > 0) {
                    grade = calculateGrade(pledgeMap.get(key), teamPledgeMap.get(key));
                }
                if (grade.compareTo(BigDecimal.valueOf(-0.1)) == 0) {
                    grade = BigDecimal.ZERO;
                }
                if (grade.compareTo(maxGrade) == 0) return true;

            }
        }
        return false;
    }

    //根据value值获取到对应的所有的key值
    private List<String> getAllKeyList(ConcurrentHashMap<String, String> map, String value) {
        List<String> keyList = new ArrayList();
        List<String> allList = new ArrayList();
        for (String getKey : map.keySet()) {
            if (map.get(getKey).equals(value)) {
                keyList.add(getKey);
            }
        }
        if (keyList.size() > 0) {
            for (String key : keyList) {
                List<String> allKeyList = getAllKeyList(map, key);
                if (allKeyList.size() > 0) allList.addAll(allKeyList);
            }

        }
        keyList.addAll(allList);
        return keyList;
    }

    //根据value值获取到对应的所有的key值
    private List<String> getKeyList(ConcurrentHashMap<String, String> map, String value) {
        List<String> keyList = new ArrayList();
        if (map.size() > 0) {
            for (String getKey : map.keySet()) {
                if (StrUtil.isNotBlank(getKey) && StrUtil.isNotBlank(value) && map.get(getKey).equals(value)) {
                    keyList.add(getKey);
                }
            }
        }
        return keyList;
    }

    //计算等级
    private BigDecimal calculateGrade(BigDecimal grzy, BigDecimal tdzy) {
        if (grzy.compareTo(new BigDecimal("20000")) >= 0 && (tdzy.compareTo(new BigDecimal("13500000")) >= 0)) {
            return new BigDecimal("0.7");
        } else if (grzy.compareTo(new BigDecimal("15000")) >= 0 && (tdzy.compareTo(new BigDecimal("4500000")) >= 0)) {
            return new BigDecimal("0.6");
        } else if (grzy.compareTo(new BigDecimal("10000")) >= 0 && (tdzy.compareTo(new BigDecimal("1500000")) >= 0)) {
            return new BigDecimal("0.5");
        } else if (grzy.compareTo(new BigDecimal("5000")) >= 0 && (tdzy.compareTo(new BigDecimal("500000")) >= 0)) {
            return new BigDecimal("0.4");
        } else if (grzy.compareTo(new BigDecimal("2000")) >= 0 && (tdzy.compareTo(new BigDecimal("100000")) >= 0)) {
            return new BigDecimal("0.3");
        } else if (grzy.compareTo(new BigDecimal("1000")) >= 0 && (tdzy.compareTo(new BigDecimal("30000")) >= 0)) {
            return new BigDecimal("0.2");
        } else if (grzy.compareTo(new BigDecimal("500")) >= 0 && (tdzy.compareTo(new BigDecimal("10000")) >= 0)) {
            return new BigDecimal("0.1");
        } else return new BigDecimal("0");
    }

    //计算数据
    private Map<String, BigDecimal> calculate(ConcurrentHashMap<String, BigDecimal> staticMap,
                                              ConcurrentHashMap<String, BigDecimal> teamStaticMap,
                                              ConcurrentHashMap<String, WaKuangZy> awardCreditMap,
                                              ConcurrentHashMap<String, BigDecimal> pledgeMap,
                                              ConcurrentHashMap<String, BigDecimal> gradeMap,
                                              ConcurrentHashMap<String, BigDecimal> teamPledgeMap,
                                              ConcurrentHashMap<String, BigDecimal> teamNumMap,
                                              ConcurrentHashMap<String, String> parentMap,
                                              WaKuangWallet yh,
                                              BigDecimal parentTeamPledge,
                                              BigDecimal parentTeamNum,
                                              BigDecimal parentTeamStatic,
                                              String parentUserName) {
        List<WaKuangWallet> subWaKuangWallet = waKuangWalletService.list(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getCoin, "USDT-BEP20").eq(WaKuangWallet::getInvitationCode, yh.getCode()));
        //质押
        BigDecimal pledge = waKuangZyMapper.zzy(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名");
        BigDecimal statics = pledge.multiply(staticRatio(pledge));
        WaKuangZy waKuangZy = waKuangZyMapper.selectByUserIdAndCoin(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", "USDT-BEP20");
        awardCreditMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", waKuangZy);
        //等级
        BigDecimal grade = new BigDecimal(yh.getType().toString()).multiply(new BigDecimal("0.1"));
        //团队质押
        BigDecimal teamPledge = BigDecimal.ZERO;
        BigDecimal teamNum = BigDecimal.ZERO;
        //静态质押
        BigDecimal teamStatic = BigDecimal.ZERO;
        if (CollectionUtil.isEmpty(subWaKuangWallet)) {
            //如果下层团队无人
            pledgeMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", pledge);
            staticMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", statics);
            gradeMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", grade);
            teamPledgeMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", teamPledge);
            teamNumMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", teamNum);
            teamStaticMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", teamStatic);
            ConcurrentHashMap<String, BigDecimal> objectObjectConcurrentHashMap = new ConcurrentHashMap<>();
            objectObjectConcurrentHashMap.put("parentTeamPledge", parentTeamPledge.add(pledge));
            objectObjectConcurrentHashMap.put("parentTeamNum", parentTeamNum.add(new BigDecimal("1")));
            objectObjectConcurrentHashMap.put("parentTeamStatic", parentTeamStatic.add(statics));
            return objectObjectConcurrentHashMap;
        } else {
            for (WaKuangWallet waKuangWallet : subWaKuangWallet) {
                parentMap.put(StrUtil.isNotBlank(waKuangWallet.getUserName()) ? waKuangWallet.getUserName() : "空用户名", parentUserName);
                //查询下层用户
                Map<String, BigDecimal> map = calculate(staticMap, teamStaticMap, awardCreditMap, pledgeMap, gradeMap, teamPledgeMap, teamNumMap, parentMap, waKuangWallet, teamPledge, teamNum, teamStatic, StrUtil.isNotBlank(waKuangWallet.getUserName()) ? waKuangWallet.getUserName() : "空用户名");
                teamPledge = map.get("parentTeamPledge");
                teamNum = map.get("parentTeamNum");
                teamStatic = map.get("parentTeamStatic");
            }
            pledgeMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", pledge);
            staticMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", statics);
            gradeMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", grade);
            teamPledgeMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", teamPledge);
            teamNumMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", teamNum);
            teamStaticMap.put(StrUtil.isNotBlank(yh.getUserName()) ? yh.getUserName() : "空用户名", teamStatic);
            ConcurrentHashMap<String, BigDecimal> objectObjectConcurrentHashMap = new ConcurrentHashMap<>();
            objectObjectConcurrentHashMap.put("parentTeamPledge", parentTeamPledge.add(pledge).add(teamPledge));
            objectObjectConcurrentHashMap.put("parentTeamNum", parentTeamNum.add(new BigDecimal("1")).add(teamNum));
            objectObjectConcurrentHashMap.put("parentTeamStatic", parentTeamStatic.add(teamStatic).add(statics));
            return objectObjectConcurrentHashMap;
        }
    }

}

