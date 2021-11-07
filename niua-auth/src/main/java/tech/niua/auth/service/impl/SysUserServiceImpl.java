package tech.niua.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.niua.auth.domain.SysUser;
import tech.niua.common.exception.CustomException;
import tech.niua.auth.mapper.SysUserMapper;
import tech.niua.auth.service.ISysUserService;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;

import java.util.Arrays;
import java.util.List;


/**
 * @author wangzhen
 * @title: SysUserService
 * @projectName niua_easy_parent
 * @description: 系统用户服务类
 * @date 2020/6/2 10:46 下午
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public SysUser register(SysUser sysUser) {
        final String username = sysUser.getUsername();
        if(userMapper.findByUsername(username)!=null) {
            throw new CustomException(ResultJson.failure(ResultCode.BAD_REQUEST, "用户已存在"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = sysUser.getPassword();
        sysUser.setPassword(encoder.encode(rawPassword));
        userMapper.insert(sysUser);
        return sysUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserAndRole(long userId, Long[] roleIds){
        userMapper.deleteByUserRoleId(userId); // 删除角色绑定信息
        int num = userMapper.addUserAndRoles(userId, roleIds);
        return num;
    }


    @Override
    public IPage<SysUser> listPage(@Param("sysUser") SysUser sysUser, IPage<SysUser> page){
        return this.userMapper.listPage(sysUser, page);
    }

    /**
     * 通过用户id解除角色的绑定
     * @param userIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long[] userIds){
        try {
            this.removeByIds(Arrays.asList(userIds));
            this.userMapper.deleteByUserRoleBatchIds(Arrays.asList(userIds));
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }
}
