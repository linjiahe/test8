package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.GonghuiJlRecord;
import com.company.project.entity.Wallet;
import com.company.project.service.GonghuiJlRecordService;
import com.company.project.util.WalletUtilBsc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/gonghui/jl")
@Slf4j
public class GonghuiJlRecordController {

    @Resource
    private GonghuiJlRecordService jlRecordService;

    @PostMapping("/list")
    public DataResult pageInfo(@RequestBody GonghuiJlRecord vo) {
        return DataResult.success(jlRecordService.pageInfo(vo)
        );
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public DataResult update(@RequestBody GonghuiJlRecord vo) {
        // 查出一条奖励记录
        GonghuiJlRecord gonghuiJlRecord = jlRecordService.getOne(Wrappers.<GonghuiJlRecord>lambdaQuery().eq(GonghuiJlRecord::getStatus,1).eq(GonghuiJlRecord::getId,vo.getId()));
        if(gonghuiJlRecord==null){
            return DataResult.fail("查不到记录");
        }

        // 转出地址
        String from = "0x521CB79ef7Dbc29406Cdc695d5E3632b6a80365c";
        //转入地址
        String to = gonghuiJlRecord.getAddress();
        //转入数量
        String value = gonghuiJlRecord.getBalance().toString();
        //转出地址私钥
        String privateKey ="aeea547aa848c524756a2a83b6d0201df15d0cb937de72993bd10f82123adc6d";
        //合约地址
        String contractAddress="0xEC03279899b22DCAa779F5F17b5Aa105bdf569da";
        //位数，根据合约里面的来
        int decimal=18;
        String res= WalletUtilBsc.tokenDeal(from,to,value,privateKey,contractAddress,decimal);
        if (res==null){
            return DataResult.fail("转账失败");
        }

        gonghuiJlRecord.setStatus(2);
        gonghuiJlRecord.setUpdateTime(new Date());
        jlRecordService.updateById(gonghuiJlRecord);
        return DataResult.success();
    }


}
