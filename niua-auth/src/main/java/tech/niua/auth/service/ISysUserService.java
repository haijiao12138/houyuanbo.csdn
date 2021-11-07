package tech.niua.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import tech.niua.auth.domain.SysUser;
import tech.niua.auth.domain.UserDetail;

import java.util.List;

/**
 * @author wangzhen
 * @title: ISysUserService
 * @projectName niua_easy_parent
 * @description: 用户 业务层
 * @date 2020/5/29 11:14 下午
 */
public interface ISysUserService  extends IService<SysUser> {

    /**
     * 用户注册
     * @param sysUser
     * @return
     */
    SysUser register(SysUser sysUser);

    /**
     * 更新用户权限
     * @param userId
     * @param roleIds
     * @return
     */
    int updateUserAndRole(long userId, Long[] roleIds);


    /**
     * 分页查询用户信息
     * @param sysUser
     * @param page
     * @return
     */
    IPage<SysUser> listPage(@Param("sysUser") SysUser sysUser, IPage<SysUser> page);

    /**
     * 通过用户id解除角色的绑定
     * @param userIds
     * @return
     */
    boolean delete(Long[] userIds);


}
