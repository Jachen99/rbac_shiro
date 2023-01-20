package space.jachen.rbac_shiro.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色权限
 */
@Data
public class RolePermission implements Serializable {

    private int id;

    private int roleId;

    private int permissionId;

}
