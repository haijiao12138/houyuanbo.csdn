package tech.niua.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.niua.auth.domain.*;
import tech.niua.auth.service.IRoleService;
import tech.niua.auth.service.ISysUserService;
import tech.niua.common.enums.BusinessType;
import tech.niua.core.annotation.ApiVersion;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;
import tech.niua.core.annotation.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangzhen
 * @title: UserController
 * @projectName niua_easy_parent
 * @description: 用户管理
 * @date 2020/11/30 下午9:48
 */
@RestController
@Api(value = "用户管理")
@ApiVersion(1)
@RequestMapping("/{version}/user")
public class UserController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IRoleService roleService;

    @Log(value = "列表查询", businessType = BusinessType.LIST)
    @PreAuthorize("hasAuthority('/user')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/list/{currentPage}/{pageSize}")
    public ResultJson index(@RequestBody SysUser user, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {


        IPage<SysUser> pageList = userService.listPage(user, new Page<>(currentPage, pageSize));

        return ResultJson.ok(pageList);
    }

    @PreAuthorize("hasAuthority('/user')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/findUserById/{id}")
    public ResultJson findUserById(@PathVariable Long id) {
        SysUser user = userService.getById(id);
        if(user != null){
            return ResultJson.ok(user);
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    @Log(value = "更新或添加操作", businessType = BusinessType.INSERTORUPDATE)
    @PreAuthorize("hasAuthority('/user/saveOrUpdate')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/saveOrUpdate")
    public ResultJson saveOrUpdate(@RequestBody SysUser sysUser) {

        boolean flag = userService.saveOrUpdate(sysUser);
        System.out.println("添加的用户信息:"+sysUser.toString());
        if(flag){
            return ResultJson.ok();
        }

        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    @Log(value = "删除操作", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('/user/delete')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("ids") Long[] ids) {
        boolean flag = userService.delete(ids);
        if(flag){
            return ResultJson.ok();
        }

        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    /**
     * 查询全部可用的权限
     * @return
     */
    @PreAuthorize("hasAuthority('/user')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/loadAllRoles")
    public ResultJson loadAllRoles() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available", true);
        List<SysRole> roleList = roleService.list(queryWrapper);
        return ResultJson.ok(roleList);
    }

    @Log(value = "设置用户角色", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('/user')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/updateUserAndRole/{userId}")
    public ResultJson updateUserAndRole(@PathVariable Long userId, @RequestBody Long[] roleIds){
        int num = userService.updateUserAndRole(userId, roleIds);
        if(num > 0){
            return ResultJson.ok();
        }else{
            return ResultJson.failure(ResultCode.SERVER_ERROR);
        }
    }

    @Log(value = "修改后台人员密码", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('/user/modify-password')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping(value = "/modify-password")
    public ResultJson modifyPassword(@RequestBody SysUser user) {
        SysUser dbUser = userService.getById(user.getId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (dbUser != null) {
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.updateById(dbUser);
            return ResultJson.ok();
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }
}
