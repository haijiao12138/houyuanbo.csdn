package tech.niua.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.niua.auth.domain.SysResource;
import tech.niua.auth.service.ISysResourceService;
import tech.niua.common.enums.BusinessType;
import tech.niua.core.annotation.ApiVersion;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;
import tech.niua.core.annotation.Log;

import java.util.Arrays;
import java.util.List;

/**
 * 项目权限资源控制类
 * 
 * @author 王震
 *
 */
@RestController
@Api(value = "获取权限资源")
@ApiVersion(1)
@RequestMapping("/{version}/resource")
public class ResourceController {
	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

	@Autowired
	private ISysResourceService resourceService;


	@PreAuthorize("hasAuthority('/resource')")
	@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
	@GetMapping("/findResource/{id}")
	public ResultJson findResource(@PathVariable Integer id) {
		SysResource resource = resourceService.getById(id);

		return ResultJson.ok(resource);
	}


	@Log(value = "查询列表", businessType = BusinessType.LIST)
	@PreAuthorize("hasAuthority('/resource')")
	@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
	@PostMapping("/listResources/{currentPage}/{pageSize}")
	public ResultJson listResources(@RequestBody SysResource resource, @PathVariable Integer currentPage,@PathVariable Integer pageSize) {
//		resource.setAvailable(true);
		resource.setParentId(1L);

		IPage<SysResource> resourceList = resourceService.listPage(resource, new Page<>(currentPage, pageSize));

		return ResultJson.ok(resourceList);
	}

	@PreAuthorize("hasAuthority('/resource')")
	@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
	@GetMapping("/loadResources/{parentId}")
	public ResultJson loadResources(@PathVariable long parentId) {
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);

		List<SysResource> resourceList = resourceService.list(queryWrapper);

		return ResultJson.ok(resourceList);
	}

	@Log(value = "添加或者编辑操作", businessType = BusinessType.INSERTORUPDATE)
	@PreAuthorize("hasAuthority('/resource/saveOrUpdate')")
	@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
	@PostMapping("/saveOrUpdate")
	public ResultJson saveOrUpdate(@RequestBody SysResource resource) {

		boolean flag = resourceService.saveOrUpdate(resource);
		if(flag){
			return ResultJson.ok();
		}

		return ResultJson.failure(ResultCode.NOT_UPDATE);
	}

	/**
	 * 批量权限自动生成
	 * @param resource
	 * @return
	 */
	@Log(value = "权限批量生成操作", businessType = BusinessType.GENCODE)
	@PreAuthorize("hasAuthority('/resource/saveOrUpdate')")
	@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
	@PostMapping("/batchCreate")
	public ResultJson batchCreate(@RequestBody SysResource resource) {
		ResultCode resultCode = resourceService.batchCreate(resource);
		if(resultCode.getCode() == 200){
			return ResultJson.ok();
		}
		return ResultJson.failure(resultCode);
	}

	@Log(value = "删除操作", businessType = BusinessType.DELETE)
	@PreAuthorize("hasAuthority('/resource/delete')")
	@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
	@GetMapping("/delete")
	public ResultJson delete(@RequestParam Long[] ids) {
		QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("parent_id", ids);
		List<SysResource> childResources = resourceService.list(queryWrapper);
		if(childResources.size() > 0){
			return ResultJson.failure(ResultCode.CHILD_DATA_EXIST);
		}

		long num = resourceService.checkAuth(Arrays.asList(ids)); // 判断是否有角色绑定相关权限资源
        if(num  > 0 ){
			return ResultJson.failure(ResultCode.ROLE_LOAD_RESOURCES);
		}

		boolean flag = resourceService.removeByIds(Arrays.asList(ids));
		if(flag){
			return ResultJson.ok();
		}

		return ResultJson.failure(ResultCode.BAD_REQUEST);
	}

}
