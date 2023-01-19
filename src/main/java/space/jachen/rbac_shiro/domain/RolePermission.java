package space.jachen.rbac_shiro.domain;

import lombok.Data;

/**
 * 角色权限
 */
@Data
public class RolePermission {

    private int id;

    private int roleId;

    private int permissionId;

}
