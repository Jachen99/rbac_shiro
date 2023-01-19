package space.jachen.rbac_shiro.domain;

import lombok.Data;

@Data
public class UserRole {

    private int id;

    private int userId;

    private int roleId;

}
