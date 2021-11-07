package tech.niua.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import tech.niua.auth.domain.SysResource;
import tech.niua.auth.domain.SysRole;

import java.util.List;

/**
 * @author wangzhen
 * @title: IRoleService
 * @projectName niua_easy_parent
 * @description: 用户角色接口
 * @date 2020/11/28 下午2:56
 */
public interface IRoleService extends IService<SysRole> {
    IPage<SysRole>  listPage(@Param("sysRole") SysRole sysRole, IPage<SysRole> page);
    /**
     * 根据角色id组合权限
     * @param id
     * @return
     */
    List<SysResource> loadResourcesByRoleId(long id);

    /**
     * 更新资源信息
     * @param roleId
     * @param list
     * @return
     */
    boolean addRoleResourcesBatch(long roleId, List list);

    /**
     * 根据角色id删除角色信息
     * @param roleIds
     * @return
     */
    boolean deleteRoleById(List<Long> roleIds);

    /**
     * 根据角色id查询资源
     * @param roleId
     * @return
     */
    List<SysResource> selectResourceByRoleId(Long roleId);
}
