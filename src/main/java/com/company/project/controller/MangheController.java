package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.NftMectManghe;
import com.company.project.entity.NftMectMangheUser;
import com.company.project.entity.NftWallet;
import com.company.project.entity.SysUser;
import com.company.project.mapper.NftMectMangheMapper;
import com.company.project.mapper.NftMectMangheUserMapper;
import com.company.project.service.NftWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 钱包管理
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/data/manghe")
@Slf4j
public class MangheController {

    @Resource
    private NftMectMangheMapper nftMectMangheMapper;

    @Resource
    private NftMectMangheUserMapper nftMectMangheUserMapper;


    @PostMapping("/list")
    public DataResult pageInfo(@RequestBody NftMectManghe nftMectManghe) {
        return DataResult.success(nftMectMangheMapper.selectList(null)
        );
    }

    /**
     * 开盲盒
     * @param vo
     * @return
     */
    @PostMapping("/add")
    public DataResult add(@RequestBody NftMectMangheUser vo) {
        NftMectMangheUser nftMectMangheUser = nftMectMangheUserMapper.selectOne(Wrappers.<NftMectMangheUser>lambdaQuery().eq(NftMectMangheUser::getAddress, vo.getAddress()));
        if (nftMectMangheUser!=null){
            return DataResult.fail("Blind box has been opened！");
        }
        NftMectManghe nftMectManghe = nftMectMangheMapper.selectOne(Wrappers.<NftMectManghe>lambdaQuery().eq(NftMectManghe::getId, vo.getMangheId()));
        if(nftMectManghe.getCount()==nftMectManghe.getSyCount()){
            return DataResult.fail("The blind box has been opened！");
        }
        nftMectManghe.setSyCount(nftMectManghe.getSyCount()+1);
        nftMectMangheMapper.updateById(nftMectManghe);
        return DataResult.success(nftMectMangheUserMapper.insert(vo)
        );
    }

    /**
     * 验证是否开过盲盒
     * @param vo
     * @return
     */
    @PostMapping("/check")
    public DataResult update(@RequestBody NftMectMangheUser vo) {
        NftMectMangheUser nftMectMangheUser = nftMectMangheUserMapper.selectOne(Wrappers.<NftMectMangheUser>lambdaQuery().eq(NftMectMangheUser::getAddress, vo.getAddress()));
        if (nftMectMangheUser!=null){
            return DataResult.fail("Blind box has been opened！");
        }
        return DataResult.success();
    }

}
