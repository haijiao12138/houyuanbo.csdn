package tech.niua.admin.operlog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.niua.admin.operlog.domain.SysOperLog;
import tech.niua.admin.operlog.service.ISysOperLogService;
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
 *  操作日志记录 控制类
 * </p>
 *
 * @author niua
 * @since 2021-01-29 20:47:38
 */
@RestController
@ApiVersion(1)
@RequestMapping("{version}/sysOperLog")
public class SysOperLogController {

    @Autowired
    private ISysOperLogService sysOperLogService;

    /**
    * 查询列表
    *
    * @param currentPage
    * @param pageSize
    * @param sysOperLog
    * @return
    */
//    @Log(value = "查询列表", businessType = BusinessType.LIST)
    @PreAuthorize("hasAuthority('/sysOperLog')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/list/{currentPage}/{pageSize}")
    public ResultJson index(@PathVariable Integer currentPage, @PathVariable Integer pageSize,@RequestBody  SysOperLog sysOperLog) {
        QueryWrapper<SysOperLog> queryWrapper = new QueryWrapper<>();

         if(StringUtils.isNotBlank(sysOperLog.getOperation())) {
             queryWrapper.like("operation", sysOperLog.getOperation());
         }

        if(sysOperLog.getCreateTimeBegin() != null && sysOperLog.getCreateTimeEnd()  != null ){
            queryWrapper.between("create_time", sysOperLog.getCreateTimeBegin(), sysOperLog.getCreateTimeEnd());
        }

         if(StringUtils.isNotBlank(sysOperLog.getOperName())) {
             queryWrapper.like("oper_name", sysOperLog.getOperName());
         }

         if(StringUtils.isNotBlank(sysOperLog.getIp())) {
             queryWrapper.like("ip", sysOperLog.getIp());
         }
        queryWrapper.orderByDesc("create_time");
        IPage<SysOperLog> pageList = sysOperLogService.page(new Page<>(currentPage, pageSize), queryWrapper);
        return ResultJson.ok(pageList);
    }

    /**
    *根据id查找
    * @param: id
    * @return
    */
    @PreAuthorize("hasAuthority('/sysOperLog')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/findById/{id}")
    public ResultJson findSysOperLogById(@PathVariable Long id) {
        SysOperLog sysOperLog = sysOperLogService.getById(id);
        if(sysOperLog != null){
            return ResultJson.ok(sysOperLog);
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    /**
    * 数据导出
    * @return
    */
//    @Log(value = "数据导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAuthority('/sysOperLog/export')")
    @GetMapping("/export")
    public ResultJson export(SysOperLog sysOperLog) {
        List<SysOperLog> list = sysOperLogService.list();
        ExcelUtil<SysOperLog> util = new ExcelUtil<>(SysOperLog.class);
        return util.exportExcel(list, "自动生成操作日志记录数据");
    }

}

