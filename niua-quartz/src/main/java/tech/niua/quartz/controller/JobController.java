package tech.niua.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.niua.common.enums.BusinessType;
import tech.niua.common.model.ResultJson;
import tech.niua.core.annotation.ApiVersion;
import tech.niua.core.annotation.Log;
import tech.niua.quartz.domain.QuartzJob;
import tech.niua.quartz.service.IJobService;

/**
 * @author wangzhen
 * @title: JobController
 * @projectName nuia-easy
 * @description: 调度控制层
 * @date 2021/1/29 下午2:27
 */
@RestController
@Api(value = "获取权限角色")
@ApiVersion(1)
@RequestMapping("/{version}/quartz")
public class JobController {
    private final static Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private IJobService jobService;

    @Log(value = "查询任务列表", businessType = BusinessType.LIST)
    @PreAuthorize("hasAuthority('/quartz')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/list/{currentPage}/{pageSize}")
    public ResultJson list(@RequestBody QuartzJob quartz, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        LOGGER.info("任务列表");
        IPage<QuartzJob> pageInfo = jobService.listQuartzJob(quartz.getJobName(), new Page<>(currentPage, pageSize));
        return ResultJson.ok(pageInfo);
    }

    @Log(value = "添加任务", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('/quartz/add')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/add")
    public ResultJson save(@RequestBody QuartzJob quartz) {
        LOGGER.info("新增任务");
        ResultJson result = jobService.saveJob(quartz);
        return result;
    }

    @Log(value = "触发任务", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('/quartz/trigger')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/trigger")
    public ResultJson trigger(@RequestBody QuartzJob quartz) {
        LOGGER.info("触发任务");
        ResultJson result = jobService.triggerJob(quartz.getJobName(), quartz.getJobGroup());
        return result;
    }

    @Log(value = "暂停任务", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('/quartz/pause')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/pause")
    public ResultJson pause(@RequestBody QuartzJob quartz) {
        LOGGER.info("停止任务");
        ResultJson result = jobService.pauseJob(quartz.getJobName(), quartz.getJobGroup());
        return result;
    }
    @Log(value = "恢复任务", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('/quartz/resume')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/resume")
    public ResultJson resume(@RequestBody QuartzJob quartz) {
        LOGGER.info("恢复任务");
        ResultJson result = jobService.resumeJob(quartz.getJobName(), quartz.getJobGroup());
        return result;
    }

    @Log(value = "删除任务", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('/quartz/delete')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/delete")
    public ResultJson remove(@RequestBody QuartzJob quartz) {
        LOGGER.info("移除任务");
        ResultJson result = jobService.removeJob(quartz.getJobName(), quartz.getJobGroup());
        return result;
    }

}
