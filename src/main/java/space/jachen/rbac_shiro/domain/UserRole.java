package space.jachen.rbac_shiro.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRole implements Serializable {

    private int id;

    private int userId;

    private int roleId;

}
