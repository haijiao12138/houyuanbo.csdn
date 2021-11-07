package tech.niua.admin.operlog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import tech.niua.common.annotation.Excel;

import java.time.LocalDateTime;

/**
* @className: Generator
* @description: 操作日志记录
* @author: niua
* @since 2021-01-29 18:43:12
*
*/
@Data
@TableName("sys_oper_log")
public class SysOperLog {

        @TableId(type = IdType.AUTO)
        @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
        private Long id;

        @Excel(name = "操作")
        private String operation;

        @Excel(name = "业务类型")
        private String businessType;

        @Excel(name = "方法名称")
        private String method;

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


        @Excel(name = "操作用户")
        private String operName;

        @Excel(name = "参数")
        private String params;

        @Excel(name = "请求的ip地址")
        private String ip;





}
