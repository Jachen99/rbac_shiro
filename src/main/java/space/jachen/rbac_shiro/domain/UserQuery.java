package space.jachen.rbac_shiro.domain;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserQuery implements Serializable {

    private String name;

    private String pwd;

}
