package tech.niua.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单/资源对象
 * @author Wangzhen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysResource {
    @TableId(type= IdType.AUTO)
    private Long id;

    private String name;

    private Integer orderNum;

    private String permission;

    private Integer type;

    private String url;

    private Long parentId;

    private Boolean available;

    @TableField(exist=false)
    private List<SysResource> childResources;

    public static enum ResourceType {

		menu("菜单"), button("按钮");

		private final String info;

		private ResourceType(String info) {
			this.info = info;
		}

		public String getInfo() {
			return info;
		}
	}

}