package tech.niua.auth.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import tech.niua.auth.domain.*;
import tech.niua.auth.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tech.niua.auth.service.ISysResourceService;
import tech.niua.auth.service.ISysUserService;
import tech.niua.auth.utils.JwtUtils;
import tech.niua.core.annotation.ApiVersion;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Wangzhen
 * createAt: 2020/5/29
 */
@RestController
@Api(value = "用户权限接口", tags = {"用户权限接口"})
@ApiVersion(1)
@RequestMapping("/{version}/auth")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final AuthService authService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysResourceService resourceService;

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private HttpServletRequest request;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录", notes = "登录成功返回token,登录之前请先注册账号")
    public ResultJson<ResponseUserToken> login(
            @Valid @RequestBody SysUser user){
        final ResponseUserToken response = authService.login(user.getUsername(), user.getPassword());
        return ResultJson.ok(response);
    }

    @GetMapping(value = "/logout")
    @ApiOperation(value = "登出", notes = "退出登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public ResultJson logout(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return ResultJson.failure(ResultCode.UNAUTHORIZED);
        }
        authService.logout(token);
        return ResultJson.ok();
    }

    @GetMapping(value = "/user")
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public ResultJson getUser(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return ResultJson.failure(ResultCode.UNAUTHORIZED);
        }
        UserDetail userDetail = authService.getUserByToken(token);
        return ResultJson.ok(userDetail);
    }

    @PostMapping(value = "/sign")
    @ApiOperation(value = "用户注册")
    public ResultJson sign(@RequestBody SysUser user) {
        if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
            return ResultJson.failure(ResultCode.BAD_REQUEST);
        }

        return ResultJson.ok(sysUserService.register(user));
    }
//    @GetMapping(value = "refresh")
//    @ApiOperation(value = "刷新token")
//    public ResultJson refreshAndGetAuthenticationToken(
//            HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        ResponseUserToken response = authService.refresh(token);
//        if(response == null) {
//            return ResultJson.failure(ResultCode.BAD_REQUEST, "token无效");
//        } else {
//            return ResultJson.ok(response);
//        }
//    }
}
