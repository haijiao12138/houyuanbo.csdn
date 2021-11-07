package tech.niua.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.niua.auth.controller.ResourceController;
import tech.niua.auth.domain.SysResource;
import tech.niua.auth.domain.SysRole;
import tech.niua.auth.mapper.SysRoleMapper;
import tech.niua.auth.service.IRoleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangzhen
 * @title: RoleServiceImpl
 * @projectName niua_easy_parent
 * @description: TODO
 * @date 2020/11/28 下午2:57
 */
@Service
public class RoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements IRoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public IPage<SysRole>  listPage(SysRole sysRole, IPage<SysRole> page) {
        return roleMapper.listPage(sysRole, page);
    }

    @Override
    public List<SysResource> loadResourcesByRoleId(long id) {
        return roleMapper.loadResourcesByRoleId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addRoleResourcesBatch(long roleId, List list) {
        try {
            roleMapper.deleteRefResourcesByRoleId(Arrays.asList(new Long[]{roleId}));
            if(list.size() > 0){
                roleMapper.insertRoleResourcesBatch(list);
            }
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRoleById(List<Long> roleIds) {
        try{
            roleMapper.deleteRefResourcesByRoleId(roleIds);
            roleMapper.deleteBatchIds(roleIds);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public List<SysResource> selectResourceByRoleId(Long roleId) {
        return roleMapper.selectResourceByRoleId(roleId);
    }
}
