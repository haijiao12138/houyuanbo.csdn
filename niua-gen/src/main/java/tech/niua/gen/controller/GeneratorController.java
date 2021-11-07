package tech.niua.gen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.niua.common.enums.BusinessType;
import tech.niua.common.utils.poi.ExcelUtil;
import tech.niua.core.annotation.ApiVersion;
import tech.niua.core.annotation.Log;
import tech.niua.common.model.ResultCode;
import tech.niua.common.model.ResultJson;
import tech.niua.gen.domain.Generator;

import tech.niua.gen.util.AutoGeneratorUtils;
import tech.niua.gen.service.IGeneratorService;

import java.util.Arrays;
import java.util.List;

/**
 * @className: GeneratorController
 * @description:
 * @author: xiaoDong
 * @date: 2020/12/14
 */
@Slf4j
@RestController
@Api(value = "GeneratorController")
@ApiVersion(1)
@RequestMapping("/{version}/generator")
public class GeneratorController {

    @Autowired
    public IGeneratorService generatorService;

    @Autowired
    public AutoGeneratorUtils autoGeneratorUtils;

    @PreAuthorize("hasAuthority('/generator')")
    @Log(value = "代码生成列表", businessType = BusinessType.LIST)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/list/{currentPage}/{pageSize}")
    public ResultJson pageListQuery(@RequestBody Generator generator, @PathVariable Integer currentPage, @PathVariable Integer pageSize) {
        QueryWrapper<Generator> queryWrapper = new QueryWrapper<>();
        if (generator == null) {
            return ResultJson.failure(ResultCode.BAD_REQUEST);
        }
        if (!("").equals(generator.getTableName())) {
            queryWrapper.like("table_name", generator.getTableName());
        }
        queryWrapper.orderByDesc("update_time");
        IPage<Generator> pageList = generatorService.page(new Page<>(currentPage, pageSize), queryWrapper);
        return ResultJson.ok(pageList);
    }

    @PreAuthorize("hasAuthority('/generator')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/findGeneratorById/{id}")
    public ResultJson findGeneratorById(@PathVariable Long id) {
        Generator generator = generatorService.getById(id);
        if (generator != null) {
            return ResultJson.ok(generator);
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('/generator/saveOrUpdate')")
    @Log(value = "保存或修改数据", businessType = BusinessType.INSERTORUPDATE)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/saveOrUpdate")
    public ResultJson saveOrUpdate(@RequestBody Generator generator) {
        if (generator == null) {
            return ResultJson.failure(ResultCode.BAD_REQUEST);
        }
        if (("").equals(generator.getTableName()) || ("").equals(generator.getModuleName())) {
            return ResultJson.failure(ResultCode.BAD_REQUEST);
        }
        boolean flag = generatorService.saveOrUpdate(generator);
        if (flag) {
            return ResultJson.ok();
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    /**
     * 代码生成
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/generator/autoGenerator')")
    @Log(value = "生成代码", businessType = BusinessType.GENCODE)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/autoGenerator/{id}")
    public ResultJson autoGenerator(@PathVariable Long id) {
        Generator generator = generatorService.getById(id);
        if (generator != null) {
            autoGeneratorUtils.createCode(generator);
            return ResultJson.ok("生成成功");
        }

        return ResultJson.failure(ResultCode.GENERATOR_FAILURE);
    }

    @PreAuthorize("hasAuthority('/generator/delete')")
    @Log(value = "批量删除", businessType = BusinessType.DELETE)
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/batchDelete")
    public ResultJson batchDelete(@RequestBody Long[] ids) {
        List<Long> longIds = Arrays.asList(ids);
        boolean flag = generatorService.removeByIds(longIds);
        if (flag) {
            return ResultJson.ok();
        }
        return ResultJson.failure(ResultCode.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('/generator')")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("/queryAllTableInDb")
    public ResultJson queryAllTableInDb() {
        List<String> strings = generatorService.quertyAllTableName();
        return ResultJson.ok(strings);
    }

    @PreAuthorize("hasAuthority('/generator/export')")
    @Log(value = "数据导出", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public ResultJson export(Generator generator) {
        List<Generator> list = generatorService.list();
        ExcelUtil<Generator> util = new ExcelUtil<>(Generator.class);
        return util.exportExcel(list, "自动生成数据");
    }

}
