package space.jachen.rbac_shiro.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Data
public class User implements Serializable {

    private int id;

    private String username;

    private String password;

    private Date createTime;

    private String salt;


    /**
     * 角色集合
     */
    private List<Role> roleList;

}
