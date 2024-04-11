package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/nft")
@Slf4j
public class NftWalletController {

    @Resource
    private NftWalletService nftWalletService;

    @Resource
    private NftWalletExRecordService nftWalletExRecordService;


    @PostMapping("/list")
    public DataResult pageInfo(@RequestBody NftWalletDTO vo) {
        return DataResult.success(nftWalletService.pageInfo(vo)
        );
    }

    @PostMapping("/findAll")
    public DataResult findAll(@RequestBody NftWalletDTO vo) {
        return DataResult.success(nftWalletService.pageInfo(vo)
        );
    }

    @PostMapping("/add")
    public DataResult add(@RequestBody NftWallet vo) {
        return DataResult.success(nftWalletService.save(vo)
        );
    }

    @PostMapping("/update")
    public DataResult update(@RequestBody NftWallet vo) {
        return DataResult.success(nftWalletService.updateById(vo)
        );
    }

    @PostMapping("/updateByImgId")
    public DataResult updateByImgId(@RequestBody NftWallet vo) {
        NftWallet nftWallet = nftWalletService.getOne(Wrappers.<NftWallet>lambdaQuery().eq(NftWallet::getImgId,vo.getImgId()));
        nftWallet.setAddress(vo.getAddress());
        nftWallet.setImg(vo.getImg());
        nftWallet.setDescription(vo.getDescription());
        nftWallet.setImgId(vo.getImgId());
        nftWallet.setUpdateTime(vo.getUpdateTime());
        nftWallet.setLevel(vo.getLevel());
        nftWallet.setPrice(vo.getPrice());
        nftWallet.setName(vo.getName());
        return DataResult.success(nftWalletService.updateById(nftWallet)
        );
    }

    /**
     * 创建交易记录，并改nft上架状态为已下架
     * @param vo
     * @return
     */
    @PostMapping("/ex/add")
    public DataResult exAdd(@RequestBody NftWalletAddEx vo) {
        NftWallet nftWallet = nftWalletService.getOne(Wrappers.<NftWallet>lambdaQuery().eq(NftWallet::getImgId,vo.getTokenId()));
        if(nftWallet.getAddress().indexOf("none")!=-1){
            return DataResult.fail("已下架");
        }
        if(nftWallet.getAddress().indexOf("suoding")!=-1){
            return DataResult.fail("已锁定");
        }
        nftWallet.setAddress(vo.getSuodingAddress());
        nftWalletService.updateById(nftWallet);

        NftWalletExRecord nftWalletExRecord = new NftWalletExRecord();
        nftWalletExRecord.setImg(vo.getImg());
        nftWalletExRecord.setTokenId(vo.getTokenId());
        nftWalletExRecord.setFromAddress(vo.getFromAddress());
        nftWalletExRecord.setToAddress(vo.getToAddress());
        nftWalletExRecord.setName(vo.getName());
        nftWalletExRecord.setPrice(vo.getPrice());
        nftWalletExRecord.setContract(vo.getContract());
        nftWalletExRecord.setStatus(0);
        return DataResult.success(nftWalletExRecordService.save(nftWalletExRecord)
        );
    }

    /**
     * @param vo
     * @return
     */
    @PostMapping("/ex/change")
    public DataResult exChange(@RequestBody NftWalletChange vo) {
        // 修改nft状态
        NftWallet nftWallet = nftWalletService.getOne(Wrappers.<NftWallet>lambdaQuery().eq(NftWallet::getImgId,vo.getTokenId()));
        nftWallet.setAddress(vo.getSuodingAddress());
        nftWalletService.updateById(nftWallet);

        // 修改交易明细的状态
        List<NftWalletExRecord> nftWalletExRecordList  = nftWalletExRecordService.list(Wrappers.<NftWalletExRecord>lambdaQuery().eq(NftWalletExRecord::getTokenId,vo.getTokenId()).eq(NftWalletExRecord::getFromAddress,vo.getFromAddress()).orderByDesc(NftWalletExRecord::getCreateTime));
        NftWalletExRecord nftWalletExRecord = nftWalletExRecordList.get(0);
        nftWalletExRecord.setStatus(vo.getStatus());
        return DataResult.success(nftWalletExRecordService.updateById(nftWalletExRecord));
    }

    /**
     * 修改交易记录状态：0:未支付 1：已付款 2：已完成 3:已取消
     * @param vo
     * @return
     */
    @PostMapping("/ex/updateByTokenId")
    public DataResult exUpdate(@RequestBody NftWalletExRecord vo) {
        List<NftWalletExRecord> nftWalletExRecordList  = nftWalletExRecordService.list(Wrappers.<NftWalletExRecord>lambdaQuery().eq(NftWalletExRecord::getTokenId,vo.getTokenId()).orderByDesc(NftWalletExRecord::getCreateTime));
        NftWalletExRecord nftWalletExRecord = nftWalletExRecordList.get(0);
        nftWalletExRecord.setStatus(vo.getStatus());
        return DataResult.success(nftWalletExRecordService.updateById(nftWalletExRecord)
        );
    }

    @PostMapping("/ex/list")
    public DataResult exPageInfo(@RequestBody NftWalletExRecord vo) {
        return DataResult.success(nftWalletExRecordService.pageInfo(vo)
        );
    }




}
