package com.company.project.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.WaKuangJl;
import com.company.project.entity.WaKuangWallet;
import com.company.project.entity.WaKuangZy;
import com.company.project.entity.WaKuangZyDTO;
import com.company.project.mapper.WaKuangJlMapper;
import com.company.project.mapper.WaKuangZyMapper;
import com.company.project.service.WaKuangJlService;
import com.company.project.service.WaKuangWalletService;
import com.company.project.service.WaKuangZyService;
import com.company.project.vo.resp.WaKuangWalletZyVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/data/wakuang/zy")
@Slf4j
public class WaKuangZyController {

    @Resource
    private WaKuangZyService waKuangZyService;

    @Resource
    private WaKuangJlService waKuangJlService;

    @Resource
    private WaKuangJlMapper waKuangJlMapper;

    @Resource
    private WaKuangZyMapper waKuangZyMapper;

    @Resource
    private WaKuangWalletService waKuangWalletService;


    @ApiOperation("查询用户DOMI总质押")
    @GetMapping("/getDomiTotalPledge/{userId}")
    public DataResult getDomiTotalPledge(@PathVariable("userId") String userId) {
        return DataResult.success(waKuangZyService.getDomiTotalPledge(userId));
    }

    @ApiOperation("查询用户质押usdt剩余奖励额度")
    @GetMapping("/getUsdtJLBalance/{userId}")
    public DataResult getUsdtJLBalance(@PathVariable("userId") String userId) {
        return DataResult.success(waKuangZyService.getUsdtJLBalance(userId));
    }




    @PostMapping("/list")
    public DataResult userPageInfo(@RequestBody WaKuangZy vo) {
        return DataResult.success(waKuangZyService.pageInfo(vo)
        );
    }
    @PostMapping("/listExport")
    public void userPageInfoExport(@RequestBody WaKuangZy vo,HttpServletResponse response) throws IOException {
        waKuangZyService.pageInfoExport(vo,response);
    }

    /**
     * 根据用户ID查出一层下级的质押总金额
     *
     * @param waKuangZy
     * @return
     */
    @PostMapping("/listByUserId")
    public DataResult listByUserIds(@RequestBody WaKuangZy waKuangZy) {
        Map map = new HashMap();

        // 先查出一层下级
        WaKuangWallet dto = new WaKuangWallet();
        dto.setInvitationCode(waKuangZy.getUserId());
        List<WaKuangWallet> userList = waKuangWalletService.List(dto);
        if (userList.size() <= 0) {
            map.put("zyBalance", 0);
            map.put("jlBalance", 0);
            return DataResult.success(map);
        }

        List<String> zyDto = new ArrayList<>();
        for (WaKuangWallet data : userList
        ) {
            zyDto.add(data.getUserName());
        }

        LambdaQueryWrapper<WaKuangZy> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(WaKuangZy::getUserId, zyDto);
        queryWrapper.eq(WaKuangZy::getCoin, "USDT-BEP20");
        List<WaKuangZy> list = waKuangZyMapper.selectList(queryWrapper);
        // 总质押金额
        BigDecimal sumBalance = new BigDecimal(0);
        for (WaKuangZy data : list
        ) {
            sumBalance = sumBalance.add(data.getZyBalance());
        }
        // 总静态奖励
        LambdaQueryWrapper<WaKuangJl> queryWrapperjl = Wrappers.lambdaQuery();
        queryWrapperjl.in(WaKuangJl::getUserId, zyDto);
        queryWrapperjl.eq(WaKuangJl::getSourceType, 3);
        List<WaKuangJl> waKuangJlList = waKuangJlMapper.selectList(queryWrapperjl);
        BigDecimal sumJlBalance = new BigDecimal(0);
        for (WaKuangJl data : waKuangJlList
        ) {
            sumJlBalance = sumJlBalance.add(data.getBalance());
        }
        map.put("zyBalance", sumBalance);
        map.put("jlBalance", sumJlBalance);
        return DataResult.success(map);
    }

    @PostMapping("/findAll")
    public DataResult findAll(@RequestBody WaKuangZyDTO vo) {
        return DataResult.success(waKuangZyService.findList(vo)
        );
    }


    @PostMapping("/jl/list")
    public DataResult jlPageInfo(@RequestBody WaKuangJl vo) {
        return DataResult.success(waKuangZyService.jlPageInfo(vo)
        );
    }

    @PostMapping("/jl/listExport")
    public void jlPageInfoExport(@RequestBody WaKuangJl vo, HttpServletResponse response) throws IOException {
        waKuangZyService.jlPageInfoExport(vo, response);
    }

    @PostMapping("/bmd/list")
    public DataResult bmdList(@RequestBody WaKuangZy vo) {
        List<String> accounts = Arrays.asList("14760153401", "14760153402", "14760153403", "14760153404", "1557116987901", "1387160823801", "1860727536101", "70742214101", "1");
        return DataResult.success(accounts);
    }


    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public DataResult userAdd(@RequestBody WaKuangZy vo) throws Exception {

        BigDecimal zyBalance = vo.getZyBalance();
        String coin = vo.getCoin().equals("All")?"USDT-BEP20":vo.getCoin();

        WaKuangWallet waKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserId()).eq(WaKuangWallet::getCoin, coin));
        if (!vo.getCoin().equals("Domi")) {
            // usdt和usdt+domi质押需要判断重复质押
            int count = waKuangZyService.count(
                    Wrappers.<WaKuangZy>lambdaQuery()
                            .eq(WaKuangZy::getUserId, vo.getUserId())
                            .eq(WaKuangZy::getCoin, coin)
                            .eq(WaKuangZy::getIsReturn, 0)
                            .gt(WaKuangZy::getJlBalance, 0));
            if(count>0){
                return DataResult.fail("已存在质押，无法再质押");
            }
        }

        vo.setMbxhStatus(1);

        BigDecimal zyEdBalance = zyBalance.multiply(new BigDecimal(2));

        if (vo.getCoin().equals("All")) {
            if (zyEdBalance.compareTo(BigDecimal.valueOf(10000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(3)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyEdBalance.compareTo(BigDecimal.valueOf(5000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2.5)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyEdBalance.compareTo(BigDecimal.valueOf(2000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyEdBalance.compareTo(BigDecimal.valueOf(1000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            } else {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            }

            WaKuangWallet domiWaKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserId()).eq(WaKuangWallet::getCoin, "Domi"));
            if (domiWaKuangWallet.getKzyBalance().compareTo(zyBalance) == -1) {
                return DataResult.fail("Domi不足");
            }
            domiWaKuangWallet.setKzyBalance(domiWaKuangWallet.getKzyBalance().subtract(zyBalance));
            waKuangWalletService.updateWallet(domiWaKuangWallet);

            vo.setCoin("USDT-BEP20");
            vo.setType(1);
            vo.setZyBalance(zyEdBalance);
            vo.setMbxhStatus(0);
            waKuangZyService.save(vo);
        }else if (vo.getCoin().equals("All-mzy")) {
            if (zyEdBalance.compareTo(BigDecimal.valueOf(10000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(3)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyEdBalance.compareTo(BigDecimal.valueOf(5000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2.5)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyEdBalance.compareTo(BigDecimal.valueOf(2000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyEdBalance.compareTo(BigDecimal.valueOf(1000)) > 0) {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            } else {
                vo.setJlBalance(zyEdBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            }

            vo.setCoin("USDT-BEP20");
            vo.setType(1);
            vo.setZyBalance(zyEdBalance);
            vo.setMbxhStatus(0);
            waKuangZyService.save(vo);
        }else if (vo.getCoin().equals("LP")) {
            vo.setZyBalance(zyBalance);
            waKuangZyService.save(vo);
        }else if (Objects.equals(vo.getCoin().toUpperCase(), "USDT-BEP20")) {
            if (zyBalance.compareTo(BigDecimal.valueOf(10000)) > 0) {
                vo.setJlBalance(zyBalance.multiply(BigDecimal.valueOf(3)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyBalance.compareTo(BigDecimal.valueOf(5000)) > 0) {
                vo.setJlBalance(zyBalance.multiply(BigDecimal.valueOf(2.5)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyBalance.compareTo(BigDecimal.valueOf(2000)) > 0) {
                vo.setJlBalance(zyBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            } else if (zyBalance.compareTo(BigDecimal.valueOf(1000)) > 0) {
                vo.setJlBalance(zyBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            } else {
                vo.setJlBalance(zyBalance.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(0.95)));
            }
            vo.setZyBalance(zyBalance);
            waKuangZyService.save(vo);
        }else if (Objects.equals(vo.getCoin().toUpperCase(), "DOMI")) {
            if (waKuangWallet.getJltxBalance().compareTo(vo.getZyBalance()) == -1) {
                return DataResult.fail("质押金额不足");
            }
            waKuangWallet.setJltxBalance(waKuangWallet.getJltxBalance().subtract(zyBalance));
            waKuangWalletService.updateWallet(waKuangWallet);
            waKuangZyService.save(vo);
        }else if (vo.getCoin().equals("DOMI+IDM")) {
//            zyBalance质押domi的数量
//            zyBalance1质押idm的数量
            WaKuangWallet domiWaKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserId()).eq(WaKuangWallet::getCoin, "Domi"));
            if (domiWaKuangWallet.getKzyBalance().compareTo(zyBalance) == -1) {
                return DataResult.fail("Domi不足");
            }
            domiWaKuangWallet.setKzyBalance(domiWaKuangWallet.getKzyBalance().subtract(zyBalance));
            waKuangWalletService.updateWallet(domiWaKuangWallet);
            waKuangZyService.save(vo);
        } else if (vo.getCoin().equals("DOMI+BNB")) {
//            zyBalance质押domi的数量
//            zyBalance1质押BNB的数量
            WaKuangWallet domiWaKuangWallet = waKuangWalletService.getOne(Wrappers.<WaKuangWallet>lambdaQuery().eq(WaKuangWallet::getUserName, vo.getUserId()).eq(WaKuangWallet::getCoin, "Domi"));
            if (domiWaKuangWallet.getKzyBalance().compareTo(zyBalance) == -1) {
                return DataResult.fail("Domi不足");
            }
            domiWaKuangWallet.setKzyBalance(domiWaKuangWallet.getKzyBalance().subtract(zyBalance));
            waKuangWalletService.updateWallet(domiWaKuangWallet);
            waKuangZyService.save(vo);
        }else {
            return DataResult.fail("不存在的币种，无法再质押");
        }

        return DataResult.success();
    }

    @PostMapping("/update")
    public DataResult userUpdate(@RequestBody WaKuangZy vo) {
        return DataResult.success(waKuangZyService.updateById(vo));
    }

    @PostMapping("/num/{userId}")
    public DataResult num(
            @PathVariable String userId
    ) {
        return DataResult.success(waKuangZyService.getZyNumByUserId(userId));
    }

    @PostMapping("/cancel/{id}")
    public DataResult cancel(
            @PathVariable String id
    ) {
        return DataResult.success(waKuangZyService.cancel(id));
    }

    @ApiOperation(value = "查看团队质押、静态收益")
    @PostMapping("/{userId}")
    public DataResult detail(@PathVariable String userId) {
        WaKuangWallet wallet = waKuangWalletService.getOne(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getUserName, userId)
                        .eq(WaKuangWallet::getCoin, "USDT-BEP20")
                        .last("limit 1")

        );
        WaKuangWalletZyVO vo = new WaKuangWalletZyVO();
        vo.setZyBalance(BigDecimal.ZERO);
        vo.setSyBalance(BigDecimal.ZERO);
        vo.setSubNum(0L);
        vo.setSubPledge(BigDecimal.ZERO);

        if (Objects.nonNull(wallet)) {
            if (Objects.isNull(wallet.getXjsyBalance()) && Objects.isNull(wallet.getXjzyBalance())) {
                nextZySy(wallet.getCode(), vo);
                waKuangWalletService.setTotailJtjlZyBalance(wallet.getUserName(), vo.getZyBalance(), vo.getSyBalance());
            } else {
                vo.setZyBalance(wallet.getXjzyBalance());
                vo.setSyBalance(wallet.getXjsyBalance());
            }
            if (ObjectUtil.isNotNull(wallet.getSubNum()))
                vo.setSubNum(wallet.getSubNum());
            if (ObjectUtil.isNotNull(wallet.getSubPledge()))
                vo.setSubPledge(wallet.getSubPledge());
        }

        return DataResult.success(vo);
    }

    /**
     * 下级质押、静态收益
     */
    private void nextZySy(String code, WaKuangWalletZyVO vo) {
        List<WaKuangWallet> list = waKuangWalletService.list(
                Wrappers.<WaKuangWallet>lambdaQuery()
                        .eq(WaKuangWallet::getCoin, "USDT-BEP20")
                        .eq(WaKuangWallet::getInvitationCode, code)
                        .ne(WaKuangWallet::getCode, "apfs2x4f")
        );
        if (CollectionUtil.isNotEmpty(list)) {
            for (WaKuangWallet wallet : list) {
                vo.setSyBalance(vo.getSyBalance().add(wallet.getJtsyBalance()));
                vo.setZyBalance(vo.getZyBalance().add(wallet.getUsdtZyBalance()));
                nextZySy(wallet.getCode(), vo);
            }
        }
    }

}
