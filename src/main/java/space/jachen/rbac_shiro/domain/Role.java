package space.jachen.rbac_shiro.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色
 */
@Data
public class Role implements Serializable {

    private int id;

    private String name;

    private String description;

    private List<Permission> permissionList;

}
