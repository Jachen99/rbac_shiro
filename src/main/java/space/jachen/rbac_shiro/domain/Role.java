package space.jachen.rbac_shiro.domain;

import lombok.Data;

import java.util.List;

/**
 * 角色
 */
@Data
public class Role {

    private int id;

    private String name;

    private String description;

    private List<Permission> permissionList;

}
