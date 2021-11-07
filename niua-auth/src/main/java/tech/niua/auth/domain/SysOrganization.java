package tech.niua.auth.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 组织机构 对象
 * @author Wangzhen
 */
@Data
@Builder
public class SysOrganization {
    private Long id;

    private Boolean available;

    private String name;

    private Long parentId;

}