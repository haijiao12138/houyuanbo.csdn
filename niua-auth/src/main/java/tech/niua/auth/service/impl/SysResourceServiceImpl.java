package tech.niua.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.niua.auth.domain.SysResource;
import tech.niua.auth.mapper.SysResourceMapper;
import tech.niua.auth.service.ISysResourceService;
import tech.niua.common.model.ResultCode;

import java.util.List;

/**
 * @author wangzhen
 * @title: SysResourceServiceImpl
 * @projectName niua_easy_parent
 * @description: 权限资源服务 实现类
 * @date 2020/6/2 9:17 下午
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource>  implements ISysResourceService {

    @Autowired
    private SysResourceMapper sysResourceMapper;

    /**
     * @Title: selectResourceByRoleId
     * @Description: 根据角色id查询资源信息
     * @Author: lxt
     * @param: roleId
     * @Date: 2019-06-10 15:49
     * @return: java.util.List<tech.niua.auth.domain.SysResource>
     * @throws:
     */
    @Override
    public List<SysResource> selectResourceByRoleId(Long roleId){
        return sysResourceMapper.selectResourceByRoleId(roleId);
    }

    @Override
    public List<SysResource> loadMenus() {
        return sysResourceMapper.loadMenus();
    }

    @Override
    public IPage<SysResource> listPage(SysResource sysResource, IPage<SysResource> page) {
        return sysResourceMapper.listPage(sysResource, page);
    }

    @Override
    public List<SysResource> selectAll() {
        return sysResourceMapper.selectAll();
    }

    @Override
    public long checkAuth(List resourceIds) {
        return sysResourceMapper.checkAuth(resourceIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultCode batchCreate(SysResource resource) {
        try{
            QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("permission", resource.getPermission());
            List<SysResource> resourcesList = this.list(queryWrapper);
            if(resourcesList.size() > 0){
                return ResultCode.DATA_EXIST;
            }
            resource.setType(0);
            this.save(resource);// 存储菜单权限

            long parentId = resource.getId();
            String prefix = resource.getPermission();
            // 存储按钮权限
            resource.setType(1);
            resource.setParentId(parentId);
            resource.setPermission(prefix);
            resource.setName("查看");
            this.save(resource);// 存储查看权限
            resource.setName("保存和编辑权限");
            resource.setPermission(prefix + "/saveOrUpdate");
            this.save(resource);// 存储保存和更新权限数据
            resource.setName("删除权限");
            resource.setPermission(prefix + "/delete");
            this.save(resource);// 删除权限
            resource.setName("导出权限");
            resource.setPermission(prefix + "/export");
            this.save(resource);// 导出权限
            return ResultCode.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultCode.SERVER_ERROR;
        }
    }
}
