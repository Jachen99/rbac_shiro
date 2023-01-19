package space.jachen.rbac_shiro.service;

import space.jachen.rbac_shiro.domain.User;

/**
 * @author JaChen
 * @date 2023/1/19 19:22
 */
public interface UserService {

    /**
     * 查询重对象 所有信息
     * @param username
     * @return
     */
    User findAllUserInfoByUsername(String username);

    User findSimpleUserInfoById(int userId);

    User findSimpleUserInfoByUsername(String username);


}
