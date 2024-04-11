package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.*;
import com.company.project.mapper.GonghuiTaocanMapper;
import com.company.project.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/data/gonghui")
@Slf4j
public class GonghuiController {

    @Resource
    private GonghuiService gonghuiService;

    @Resource
    private GonghuiUserService gonghuiUserService;

    @Resource
    private GonghuiUserShuhuiService gonghuiUserShuhuiService;

    @Resource
    private GonghuiJlRecordService gonghuiJlRecordService;

    @Resource
    private GonghuiTaocanMapper gonghuiTaocanMapper;



    @PostMapping("/list")
    public DataResult pageInfo(@RequestBody Gonghui vo) {
        return DataResult.success(gonghuiService.pageInfo(vo)
        );
    }

    @PostMapping("/detail")
    public DataResult detail(@RequestBody Gonghui vo) {
        return DataResult.success(gonghuiService.getById(vo.getId())
        );
    }

    @PostMapping("/check")
    public DataResult check(@RequestBody Gonghui vo) {
        Gonghui gonghui = gonghuiService.getOne(Wrappers.<Gonghui>lambdaQuery().eq(Gonghui::getAddress,vo.getAddress()));
        if (gonghui!=null){
            return DataResult.fail("当前地址已有公会");
        }
        return DataResult.success();
    }

    @PostMapping("/add")
    public DataResult add(@RequestBody Gonghui vo) {
        Gonghui gonghui = gonghuiService.getOne(Wrappers.<Gonghui>lambdaQuery().eq(Gonghui::getAddress,vo.getAddress()));
        if (gonghui!=null){
            return DataResult.fail("当前地址已有公会");
        }
        return DataResult.success(gonghuiService.save(vo)
        );
    }

    @PostMapping("/update")
    public DataResult update(@RequestBody Gonghui vo) {
        return DataResult.success(gonghuiService.updateById(vo)
        );
    }

    @PostMapping("/jl-record/list")
    public DataResult jlRecordPageInfo(@RequestBody GonghuiJlRecord vo) {
        vo.setStatus(2);
        return DataResult.success(gonghuiJlRecordService.pageInfo(vo));
    }

    @PostMapping("/taocan/list")
    public DataResult taocanPageInfo(@RequestBody GonghuiTaocan vo) {
        return DataResult.success(gonghuiTaocanMapper.selectList(null));
    }

    @PostMapping("/user/list")
    public DataResult userPageInfo(@RequestBody GonghuiUser vo) {
        return DataResult.success(gonghuiUserService.pageInfo(vo)
        );
    }

    @PostMapping("/user/findAll")
    public DataResult findAll(@RequestBody GonghuiUser vo) {
        return DataResult.success(gonghuiUserService.findList(vo)
        );
    }

    @PostMapping("/user/check")
    public DataResult userCheck(@RequestBody GonghuiUser vo) {
        GonghuiUser gonghuiUser = gonghuiUserService.getOne(Wrappers.<GonghuiUser>lambdaQuery().eq(GonghuiUser::getAddress,vo.getAddress()));
        if (gonghuiUser!=null){
            return DataResult.fail("已加入其他公会");
        }
        return DataResult.success();
    }

    @PostMapping("/user/add")
    public DataResult userAdd(@RequestBody GonghuiUser vo) {
        GonghuiUser gonghuiUser = gonghuiUserService.getOne(Wrappers.<GonghuiUser>lambdaQuery().eq(GonghuiUser::getAddress,vo.getAddress()));
        if (gonghuiUser!=null){
            return DataResult.fail("已加入其他公会");
        }
        return DataResult.success(gonghuiUserService.save(vo)
        );
    }

    @PostMapping("/user/delete")
    public DataResult userDelete(@RequestBody GonghuiUser vo) {
        GonghuiUser gonghuiUser = gonghuiUserService.getOne(Wrappers.<GonghuiUser>lambdaQuery().eq(GonghuiUser::getAddress,vo.getAddress()).eq(GonghuiUser::getGonghuiId,vo.getGonghuiId()));
        if (gonghuiUser==null){
            return DataResult.fail("查不到记录");
        }
        return DataResult.success(gonghuiUserService.removeById(gonghuiUser)
        );
    }

    @PostMapping("/user/update")
    public DataResult userUpdate(@RequestBody GonghuiUser vo) {
        return DataResult.success(gonghuiUserService.updateById(vo)
        );
    }

    // 下面的是赎回
    @PostMapping("/user/shuhui/list")
    public DataResult userPageInfo(@RequestBody GonghuiUsershuhui vo) {
        return DataResult.success(gonghuiUserShuhuiService.pageInfo(vo)
        );
    }

    @PostMapping("/user/shuhui/findAll")
    public DataResult findAll(@RequestBody GonghuiUsershuhui vo) {
        return DataResult.success(gonghuiUserShuhuiService.findList(vo)
        );
    }

    @PostMapping("/user/shuhui/add")
    public DataResult userAdd(@RequestBody GonghuiUsershuhui vo) {
        return DataResult.success(gonghuiUserShuhuiService.save(vo)
        );
    }

    @PostMapping("/user/shuhui/update")
    public DataResult userUpdate(@RequestBody GonghuiUsershuhui vo) {
        return DataResult.success(gonghuiUserShuhuiService.updateById(vo)
        );
    }

}
