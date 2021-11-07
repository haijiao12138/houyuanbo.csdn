package tech.niua.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.niua.auth.domain.SysResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysResourceMapper extends BaseMapper<SysResource> {

    /**
     * @Title: selectByUsername
     * @Description: 根据用户名查询资源列表
     * @Author: lxt
     * @param: username
     * @Date: 2019-06-10 15:49
     * @return: java.util.List<tech.niua.auth.domain.SysResource>
     * @throws:
     */
    List<SysResource> selectByUsername(String username);

    /**
     * 查询全部资源信息
     * @return
     */
    List<SysResource> selectAll();

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
     * @Title: listPage
     * @Description: 分页查询资源信息
     * @Author: lxt 
     * @param: sysResource
     * @param: page
     * @Date: 2019-06-10 18:00 
     * @return: java.util.List<tech.niua.auth.domain.SysResource>
     * @throws: 
     */
    IPage<SysResource> listPage(@Param("resource") SysResource sysResource,IPage<SysResource> page);

    /**
     * 加载菜单资源
     * @return
     */
    List<SysResource> loadMenus();

    /**
     * 查看是否有角色使用了相关角色信息
     * @param resourceIds
     * @return
     */
    long checkAuth(@Param("resourceIds") List resourceIds);

}