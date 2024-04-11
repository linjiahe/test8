package com.company.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.SysUser;
import com.company.project.vo.req.LoginReqVO;
import com.company.project.vo.req.RegisterReqVO;
import com.company.project.vo.req.UpdatePasswordReqVO;
import com.company.project.vo.resp.LoginRespVO;
import com.company.project.vo.resp.UserOwnRoleRespVO;

import java.util.List;

/**
 * 用户 服务类
 *
 * @author linijahe
 * @version V1.0
 * @date 2020年3月18日
 */
public interface UserService extends IService<SysUser> {

    String register(RegisterReqVO vo);

    LoginRespVO login(LoginReqVO vo);

    void updateUserInfo(SysUser vo, String operationId);

    IPage<SysUser> pageInfo(SysUser vo);

    void addUser(SysUser vo);

    void logout();

    void updatePwd(UpdatePasswordReqVO vo,String userId);

    List<SysUser> getUserListByDeptIds(List<Object> deptIds);

    UserOwnRoleRespVO getUserOwnRole(String userId);

    void updateUserInfoMy(SysUser vo, String userId);
}
