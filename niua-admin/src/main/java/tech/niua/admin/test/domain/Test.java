package tech.niua.admin.test.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import tech.niua.common.annotation.Excel;

import java.time.LocalDateTime;

/**
* @className: Generator
* @description: 测试表
* @author: niua
* @since 2021-01-22 11:30:06
*
*/
@Builder
@Data
@TableName("t_test")
public class Test {

        @TableId(type = IdType.AUTO)
        @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
        private Long id;

        @Excel(name = "姓名")
        private String name;

        @ApiModelProperty(value = "创建时间")
        @TableField(fill = FieldFill.INSERT)
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime;

        @TableField(exist=false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTimeBegin;
        @TableField(exist=false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTimeEnd;






}
