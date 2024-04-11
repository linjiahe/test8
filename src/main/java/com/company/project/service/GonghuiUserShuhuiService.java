package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.GonghuiUsershuhui;
import com.company.project.entity.GonghuiUsershuhui;

import java.util.List;

/**
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface GonghuiUserShuhuiService extends IService<GonghuiUsershuhui> {

    IPage<GonghuiUsershuhui> pageInfo(GonghuiUsershuhui vo);

    List<GonghuiUsershuhui> findList(GonghuiUsershuhui vo);

}
