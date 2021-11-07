package tech.niua.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Insert;
import tech.niua.auth.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import tech.niua.auth.domain.UserDetail;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

	/**
	 * 根据用户名查找用户
	 * @param name
	 * @return
	 */
	UserDetail findByUsername(@Param("name") String name);

	/**
	 * @Title: selectByUserId
	 * @Description:  根据角色id查询资源信息
	 * @Author: lxt
	 * @param: id
	 * @Date: 2019-06-10 16:11
	 * @return: tech.niua.auth.domain.SysUser
	 * @throws:
	 */
	SysUser selectByUserId(Long id);
	/**
	 * @Title: selectByUsername
	 * @Description: 根据用户名查找唯一用户
	 * @Author: lxt
	 * @param: userName
	 * @Date: 2019-06-10 16:11
	 * @return: tech.niua.auth.domain.SysUser
	 * @throws:
	 */
	SysUser selectByUsername(String userName);
	/**
	 * @Title: selectByUser
	 * @Description: 根据条件查询用户信息
	 * @Author: lxt
	 * @param: user
	 * @Date: 2019-06-10 16:10
	 * @return: java.util.List<tech.niua.auth.domain.SysUser>
	 * @throws:
	 */
	List<SysUser> selectByUser(SysUser user);
	/**
	 * @Title: listPage
	 * @Description: 分页查询用户信息
	 * @Author: lxt
	 * @param: sysUser
	 * @param: page
	 * @Date: 2019-06-10 17:07
	 * @return: java.util.List<tech.niua.auth.domain.SysUser>
	 * @throws:
	 */
	IPage<SysUser> listPage(@Param("sysUser") SysUser sysUser,IPage<SysUser> page);

	/**
	 * 通过用户id解除角色的绑定
	 * @param userId
	 * @return
	 */
	int deleteByUserRoleId(Long userId);

	/**
	 * 根据用户id，批量删除用户信息
	 * @param userIds
	 * @return
	 */
	int deleteByUserRoleBatchIds(List<Long> userIds);

	/**
	 * 添加用户和角色绑定信息
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	int addUserAndRoles(long userId, Long[] roleIds);
}