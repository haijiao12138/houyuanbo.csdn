package tech.niua.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.niua.auth.domain.SysResource;
import tech.niua.auth.domain.UserDetail;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tech.niua.auth.mapper.SysResourceMapper;
import tech.niua.auth.mapper.SysUserMapper;

import java.util.List;

/**
 * 登录身份认证
 * @author: Wangzhen
 * createAt: 2020/5/29
 */
@Component("CustomUserDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

    private final SysUserMapper sysUserMapper;

    private final SysResourceMapper resourceMapper;

    public CustomUserDetailsServiceImpl(SysUserMapper sysUserMapper, SysResourceMapper resourceMapper) {
        this.sysUserMapper = sysUserMapper;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public UserDetail loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDetail userDetail = sysUserMapper.findByUsername(name);
        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", name));
        }
        List<SysResource> resourceList = resourceMapper.selectByUsername(userDetail.getUsername());
        userDetail.setResources(resourceList);
        return userDetail;
    }
}
