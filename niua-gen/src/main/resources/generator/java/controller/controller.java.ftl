package ${package.Controller};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import tech.niua.core.annotation.ApiVersion;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import tech.niua.common.enums.BusinessType;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;
import java.util.Arrays;
import tech.niua.common.utils.poi.ExcelUtil;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import tech.niua.core.annotation.NoRepeatSubmit;
import tech.niua.core.annotation.Log;



/**
 * <p>
 *  ${table.comment} 控制类
 * </p>
 *
 * @author niua
 * @since ${.now?string("yyyy-MM-dd HH:mm:ss")}
 */
@RestController
@ApiVersion(1)
@RequestMapping("{version}/${entity ?uncap_first}")
public class ${table.controllerName} {

    @Autowired
    private ${table.serviceName} ${entity?uncap_first}Service;

    /**
    * 查询列表
    *
    * @param currentPage
    * @param pageSize
    * @param ${entity?uncap_first}
    * @return
    */
    @Log(value = "查询列表", businessType = BusinessType.LIST)
    @PreAuthorize("hasAuthority('/${entity?uncap_first}')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/list/{currentPage}/{pageSize}")
    public ResultJson index(@PathVariable Integer currentPage, @PathVariable Integer pageSize,@RequestBody  ${entity} ${entity?uncap_first}) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
<#list table.fields as field >
<#if (field.propertyName != "id" && field.propertyName != "createTime" && field.propertyName != "updateTime") >
     <#if field.propertyType == "String">
         if(StringUtils.isNotBlank(${entity?uncap_first}.get${field.propertyName?cap_first}())) {
             queryWrapper.like("${field.name}", ${entity?uncap_first}.get${field.propertyName?cap_first}());
         }
     </#if>
</#if>

    <#if field.propertyName == "createTime">
        if(${entity?uncap_first}.get${field.propertyName?cap_first}Begin() != null && ${entity?uncap_first}.get${field.propertyName?cap_first}End()  != null ){
            queryWrapper.between("${field.name}", ${entity?uncap_first}.get${field.propertyName?cap_first}Begin(), ${entity?uncap_first}.get${field.propertyName?cap_first}End());
        }
        queryWrapper.orderByDesc("create_time");
    </#if>
</#list>
        IPage<${entity}> pageList = ${entity?uncap_first}Service.page(new Page<>(currentPage, pageSize), queryWrapper);
        return ResultJson.ok(pageList);
    }

    /**
    *根据id查找
    * @param: id
    * @return
    */
    @PreAuthorize("hasAuthority('/${entity?uncap_first}')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/findById/{id}")
    public ResultJson find${entity}ById(@PathVariable Long id) {
        ${entity} ${entity ?uncap_first} = ${entity ?uncap_first}Service.getById(id);
        if(${entity ?uncap_first} != null){
            return ResultJson.ok(${entity ?uncap_first});
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    /**
    * 添加修改
    * @param ${entity?uncap_first}
    * @return
    */
    @Log(value = "添加修改", businessType = BusinessType.INSERTORUPDATE)
    @PreAuthorize("hasAuthority('/${entity?uncap_first}/saveOrUpdate')")
    @NoRepeatSubmit
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/saveOrUpdate")
    public ResultJson saveOrUpdate(@RequestBody ${entity} ${entity?uncap_first}){
        boolean flag = ${entity?uncap_first}Service.saveOrUpdate(${entity?uncap_first});
        if(flag){
            return ResultJson.ok();
        }
        return ResultJson.failure(ResultCode.NOT_UPDATE);
    }

    /**
    * 删除
    * @param ids
    * @return
    */
    @Log(value = "删除", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('/${entity?uncap_first}/delete')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("ids") Long[] ids){
        boolean flag = ${entity?uncap_first}Service.removeByIds(Arrays.asList(ids));
        return ResultJson.ok(flag);
    }

    /**
    * 数据导出
    * @return
    */
    @Log(value = "数据导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('/${entity?uncap_first}/export')")
    @GetMapping("/export")
    public ResultJson export(${entity} ${entity?uncap_first}) {
        List<${entity}> list = ${entity?uncap_first}Service.list();
        ExcelUtil<${entity}> util = new ExcelUtil<>(${entity}.class);
        return util.exportExcel(list, "自动生成${table.comment}数据");
    }

}

