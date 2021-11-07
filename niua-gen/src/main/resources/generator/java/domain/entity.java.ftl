package ${package.Entity};

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import tech.niua.common.annotation.Excel;

import java.time.LocalDateTime;

/**
* @className: Generator
* @description: ${table.comment}
* @author: niua
* @since ${.now?string("yyyy-MM-dd HH:mm:ss")}
*
*/
@Builder
@Data
@TableName("${table.name}")
public class ${table.entityName} {
<#list table.fields as field >

    <#if field.keyFlag>
        @TableId(type = IdType.AUTO)
        @Excel(name = "序号", cellType = Excel.ColumnType.NUMERIC)
        private ${field.propertyType} ${field.propertyName};
    </#if>
    <#if field.propertyName == "updateTime">
        @ApiModelProperty(value = "更新时间")
        @TableField(fill = FieldFill.INSERT_UPDATE)
        @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
        private ${field.propertyType} ${field.propertyName};
    </#if>
    <#if field.propertyName == "createTime">
        @ApiModelProperty(value = "创建时间")
        @TableField(fill = FieldFill.INSERT)
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
        private ${field.propertyType} ${field.propertyName};

        @TableField(exist=false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime ${field.propertyName}Begin;
        @TableField(exist=false)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime ${field.propertyName}End;

    </#if>
    <#if field.propertyName != "createTime" && field.propertyName != "updateTime" && !field.keyFlag>
        @Excel(name = "${field.comment}")
        private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>





}
