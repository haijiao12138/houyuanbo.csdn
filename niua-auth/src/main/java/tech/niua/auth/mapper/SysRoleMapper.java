package tech.niua.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.niua.auth.domain.SysResource;
import tech.niua.auth.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * @Title: insertSelective
     * @Description: 插入角色返回主键
     * @Author: lxt
     * @param: sysRole
     * @Date: 2019-06-10 10:58
     * @return: int
     * @throws:
     */
    int insertSelective(SysRole sysRole);

    /**
     * @Title: selectRolesByUserId
     * @Description: 根据用户id获取用户权限信息
     * @Author: lxt
     * @param: userId
     * @Date: 2019-06-10 11:12
     * @return: java.util.List<tech.niua.auth.domain.SysRole>
     * @throws:
     */
    List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 根据角色id查询资源
     * @param roleId
     * @return
     */
    List<SysResource> selectResourceByRoleId(Long roleId);

    /**
     * @Title: listPage
     * @Description: 分页查询用户信息
     * @Author: lxt
     * @param: sysRole
     * @param: page
     * @Date: 2019-06-10 17:20
     * @return: java.util.List<tech.niua.auth.domain.SysRole>
     * @throws:
     */
    IPage<SysRole> listPage(@Param("sysRole") SysRole sysRole, IPage<SysRole> page);

    /**
     * 根据角色id组合权限
     * @param id
     * @return
     */
    List<SysResource> loadResourcesByRoleId(long id);

    /**
     * 删除角色和资源的绑定关系
     * @param id
     * @return
     */
    int deleteRefResourcesByRoleId(List<Long> id);

    /**
     * 批量插入角色和资源的绑定关系
     * @param list
     * @return
     */
    int insertRoleResourcesBatch(List list);

}