package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.Rp;
import com.company.project.entity.RpDTO;
import com.company.project.entity.RpRecord;
import com.company.project.entity.Wallet;
import com.company.project.service.RpRecordService;
import com.company.project.service.RpService;
import com.company.project.service.WalletService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/data/rp")
@Slf4j
public class RpController {

    @Resource
    private RpService rpService;

    @Resource
    private RpRecordService rpRecordService;

    @Resource
    private WalletService walletService;

    /**
     * 创建红包
     * @return
     */
    @PostMapping("/add")
    public DataResult add(@RequestBody RpDTO rpDTO) {

//        if (!(rpDTO.getCoin().equals("MECT")||rpDTO.getCoin().equals("USDT-BEP20"))){
//            return DataResult.fail("该币种不支持发红包");
//        }

        BigDecimal balance = new BigDecimal(rpDTO.getBalance());
        Wallet wallet = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,rpDTO.getUserId()).eq(Wallet::getCoin,rpDTO.getCoin()));
        if(wallet==null){
            return DataResult.fail("Wallet account does not exist");
        }
        if(wallet.getRpBalance()==null){
            wallet.setRpBalance(new BigDecimal(0));
        }
        if(wallet.getBalance().add(wallet.getRpBalance()).compareTo(balance)==-1){
            return DataResult.fail("Insufficient amount");
        }

        wallet.setRpBalance(wallet.getRpBalance().subtract(balance));
        walletService.updateWallet(wallet);

        RedEnvelope redEnvelope = new RandomRedEnvelope(rpDTO.getBalance(),rpDTO.getCount());
        Rp rp = new Rp();
        rp.setCoin(rpDTO.getCoin());
        rp.setMsg(rpDTO.getMsg());
        rp.setBalance(rpDTO.getBalance());
        rp.setCount(rpDTO.getCount());
        rp.setStatus(1);
        rp.setUserId(rpDTO.getUserId());
        rpService.save(rp);
        showProcess(redEnvelope,rp.getId(),rpDTO.getToUserIds(),rpDTO.getMsg(),rpDTO.getUserId());
        return DataResult.success();
    }

    /**
     * 抢红包
     * @param map
     * @return
     */
    @PostMapping("/addRpRecord")
    public DataResult addRp(@RequestBody Map<String,String> map) {
        String userId = map.get("userId");
        String rpRecordId = map.get("rpRecordId");
        if (userId==null||userId.equals("")){
            return DataResult.fail("userId not null");
        }

        RpRecord rpRecord = rpRecordService.getOne(Wrappers.<RpRecord>lambdaQuery().eq(RpRecord::getRpId,rpRecordId).eq(RpRecord::getStatus,1).eq(RpRecord::getUserId,userId));
        if (rpRecord==null){
            return DataResult.fail("Red packet does not exist");
        }

        rpRecord.setStatus(2);
        rpRecordService.updateById(rpRecord);

        Rp rp = rpService.getOne(Wrappers.<Rp>lambdaQuery().eq(Rp::getId,rpRecord.getRpId()));
        if (rpRecord==null){
            return DataResult.fail("Red packet does not exist");
        }

        Wallet wallet = walletService.getOne(Wrappers.<Wallet>lambdaQuery().eq(Wallet::getUserId,userId).eq(Wallet::getCoin,rp.getCoin()));
        if(wallet==null){
            return DataResult.fail("Wallet account does not exist");
        }

        wallet.setRpBalance(wallet.getRpBalance().add(new BigDecimal(rpRecord.getBalance())));
        walletService.updateWallet(wallet);

        return DataResult.success();
    }


    /**
     * 红包记录列表
     * @param vo
     * @return
     */
    @PostMapping("/rpRecordlist")
    public DataResult rpRecordlistPageInfo(@RequestBody RpRecord vo) {
        return DataResult.success(rpRecordService.pageInfo(vo)
        );
    }

    /**
     * 红包列表
     * @param vo
     * @return
     */
    @PostMapping("/rpList")
    public DataResult rpList(@RequestBody Rp vo) {
        return DataResult.success(rpService.pageInfo(vo)
        );
    }

    public  void showProcess(RedEnvelope redEnvelope,String rpId,List<String> userIds,String msg,String rpUserId){
        List<RpRecord> list = new ArrayList<>();
        double sum = 0;
        int index = 0;
        while(redEnvelope.remainPeople>0){
            double money = redEnvelope.giveMoney();
            RpRecord rpRecord = new RpRecord();
            rpRecord.setRpId(rpId);
            rpRecord.setStatus(1);
            rpRecord.setMsg(msg);
            rpRecord.setRpUserId(rpUserId);
            rpRecord.setBalance(money);
            rpRecord.setUserId(userIds.get(index));
            list.add(rpRecord);
            sum = sum+money;
            index++;
        }
        String s = String.format("%.2f",sum);   //金额保留两位小数
        sum = Double.parseDouble(s);
        rpRecordService.saveBatch(list);
    }

}
