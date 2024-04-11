package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.Gonghui;
import com.company.project.entity.GonghuiUser;

import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface GonghuiUserService extends IService<GonghuiUser> {

    IPage<GonghuiUser> pageInfo(GonghuiUser vo);

    List<GonghuiUser> findList(GonghuiUser vo);

}
