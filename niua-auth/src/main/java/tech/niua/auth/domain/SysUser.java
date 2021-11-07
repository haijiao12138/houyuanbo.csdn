package tech.niua.auth.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.niua.common.annotation.Excel;

import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * @author : Wangzhen
 * createAt: 2020/5/29
 */
@Builder
@Data
public class SysUser {
    private Long id;
    @ApiModelProperty(value = "用户名", required = true)
    @Size(min=2, max=20)
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    @Size(min=2, max=20)
    private String password;
    private Boolean available;
    private int sexType;
    private String email;
    private String tel;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist=false)
    private List<SysRole> roles;

}
