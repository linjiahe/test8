package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.NftMectManghe;
import com.company.project.entity.NftMectMangheUser;
import com.company.project.mapper.NftMectMangheMapper;
import com.company.project.mapper.NftMectMangheUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/data/yq")
@Slf4j
public class YuanQiaoController {



    @PostMapping("/list")
    public DataResult pageInfo(@RequestBody NftMectManghe nftMectManghe) {
        Map map = new HashMap<>();
        List list = new ArrayList<>();
        list.add("https://metacitybridge.com/yuanqiao_img/lunbo1.jpg");
        list.add("https://metacitybridge.com/yuanqiao_img/lunbo2.PNG");
        list.add("https://metacitybridge.com/yuanqiao_img/lunbo3.PNG");

        map.put("qidonye","https://metacitybridge.com/yuanqiao_img/qidonye.jpg");
        map.put("lunbotu",list);
        return DataResult.success(map
        );
    }


}
