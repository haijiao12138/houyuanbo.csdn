package tech.niua.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.niua.auth.domain.SysResource;
import tech.niua.auth.domain.SysUser;
import tech.niua.common.model.ResultCode;

import java.util.List;

/**
 * @author wangzhen
 * @title: ISysResource
 * @projectName niua_easy_parent
 * @description: TODO
 * @date 2020/6/2 9:14 下午
 */
public interface ISysResourceService extends IService<SysResource> {

    /**
     * @Title: selectResourceByRoleId
     * @Description: 根据角色id查询资源信息
     * @Author: lxt
     * @param: roleId
     * @Date: 2019-06-10 15:49
     * @return: java.util.List<tech.niua.auth.domain.SysResource>
     * @throws:
     */
    List<SysResource> selectResourceByRoleId(Long roleId);

    /**
     * 加载菜单资源
     * @return
     */
    List<SysResource> loadMenus();

    /**
     * 分页查询资源信息
     * @param sysResource
     * @param page
     * @return
     */
    IPage<SysResource> listPage(SysResource sysResource, IPage<SysResource> page);

    /**
     * 查询全部资源信息
     * @return
     */
    List<SysResource> selectAll();

    /**
     * 检查资源是否已经被角色使用
     * @param resourceIds
     * @return
     */
    long checkAuth(List resourceIds);

    /**
     * 批量生成权限
     * @param resource
     * @return
     */
    ResultCode batchCreate(SysResource resource);
}
