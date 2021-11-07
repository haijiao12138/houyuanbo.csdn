package tech.niua.admin.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.niua.admin.test.domain.Test;
import tech.niua.admin.test.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import tech.niua.core.annotation.ApiVersion;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;
import java.util.Arrays;
import tech.niua.common.utils.poi.ExcelUtil;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import tech.niua.core.annotation.NoRepeatSubmit;



/**
 * <p>
 *  测试表 控制类
 * </p>
 *
 * @author niua
 * @since 2021-01-22 11:30:06
 */
@RestController
@ApiVersion(1)
@RequestMapping("{version}/test")
public class TestController {

    @Autowired
    private ITestService testService;

    /**
    * 查询列表
    *
    * @param currentPage
    * @param pageSize
    * @param test
    * @return
    */
    @PreAuthorize("hasAuthority('/test')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/list/{currentPage}/{pageSize}")
    public ResultJson index(@PathVariable Integer currentPage, @PathVariable Integer pageSize,@RequestBody  Test test) {
        QueryWrapper<Test> queryWrapper = new QueryWrapper<>();

         if(StringUtils.isNotBlank(test.getName())) {
             queryWrapper.like("name", test.getName());
         }


        if(test.getCreateTimeBegin() != null && test.getCreateTimeEnd()  != null ){
            queryWrapper.between("create_time", test.getCreateTimeBegin(), test.getCreateTimeEnd());
        }
        IPage<Test> pageList = testService.page(new Page<>(currentPage, pageSize), queryWrapper);
        return ResultJson.ok(pageList);
    }

    /**
    *根据id查找
    * @param: id
    * @return
    */
    @PreAuthorize("hasAuthority('/test')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/findById/{id}")
    public ResultJson findTestById(@PathVariable Long id) {
        Test test = testService.getById(id);
        if(test != null){
            return ResultJson.ok(test);
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    /**
    * 添加修改
    * @param test
    * @return
    */
    @PreAuthorize("hasAuthority('/test/saveOrUpdate')")
    @NoRepeatSubmit
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/saveOrUpdate")
    public ResultJson saveOrUpdate(@RequestBody Test test){
        boolean flag = testService.saveOrUpdate(test);
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
    @PreAuthorize("hasAuthority('/test/delete')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("ids") Long[] ids){
        boolean flag = testService.removeByIds(Arrays.asList(ids));
        return ResultJson.ok(flag);
    }

    /**
    * 数据导出
    * @return
    */
    @PreAuthorize("hasAuthority('/test/export')")
    @GetMapping("/export")
    public ResultJson export(Test test) {
        List<Test> list = testService.list();
        ExcelUtil<Test> util = new ExcelUtil<>(Test.class);
        return util.exportExcel(list, "自动生成测试表数据");
    }

}

