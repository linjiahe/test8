package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.QRCodeUtil;
import com.company.project.entity.*;
import com.company.project.mapper.VersionMapper;
import com.company.project.service.DayPriceService;
import com.company.project.service.SpHysService;
import com.company.project.service.WalletRechareService;
import com.company.project.service.WalletService;
import com.company.project.util.WalletUtilBsc;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.crypto.MnemonicException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/sp/")
@Slf4j
public class SpController {

    @Resource
    private WalletService walletService;

    @Resource
    private SpHysService spHysService;

    /**
     * 视频直播登录
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public DataResult spLogin(@RequestBody Map<String,String> map) throws Exception {
        String userId = map.get("userId");
        if (userId==null||userId.equals("")){
            return DataResult.fail("userId不能为空");
        }

        // 目前的币种数量
        List<Wallet> res = walletService.list(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,userId));
        if (res.size()<=0){
            return DataResult.fail("账号不存在");
        }

        return DataResult.success();
    }


    /**
     * 会议室列表
     * @param spHys
     * @return
     * @throws Exception
     */
    @PostMapping("/hys/list")
    public DataResult hysList(@RequestBody SpHys spHys) throws Exception {
        return DataResult.success(spHysService.pageInfo(spHys));
    }

    @PostMapping("/hys/add")
    public DataResult hysAdd(@RequestBody SpHys spHys) throws Exception {
        return DataResult.success(spHysService.save(spHys));
    }
}
