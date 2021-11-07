package tech.niua.gen.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import tech.niua.common.annotation.Excel;

import java.time.LocalDateTime;
import java.util.Date;

/**
* @className: Generator
* @description:
* @author: xiaoDong
* @date: 2020/12/14
*
*/
@Builder
@Data
@TableName("sys_generator")
public class Generator {

    @TableId(type = IdType.AUTO)
    @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
    private Long id;
    @Excel(name = "模块名称")
    private String moduleName;
    @Excel(name = "表名称")
    private String tableName;
    @Excel(name = "是否忽略前缀", readConverterExp = "0=否,1=是")
    private int ignoreFlag;
    @Excel(name = "前缀")
    private String ignorePrefix;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
