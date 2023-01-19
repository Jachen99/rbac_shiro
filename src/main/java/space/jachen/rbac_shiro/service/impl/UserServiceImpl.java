package space.jachen.rbac_shiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.rbac_shiro.dao.PermissionMapper;
import space.jachen.rbac_shiro.dao.RoleMapper;
import space.jachen.rbac_shiro.dao.UserMapper;
import space.jachen.rbac_shiro.domain.Role;
import space.jachen.rbac_shiro.domain.User;
import space.jachen.rbac_shiro.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/1/19 19:26
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public User findAllUserInfoByUsername(String username) {

        User user = userMapper.findByUserName(username);
        List<Role> roleList = roleMapper.findRoleListByUserId(user.getId());
        user.setRoleList(roleList);

        return user;
    }

    @Override
    public User findSimpleUserInfoById(int userId) {
        return userMapper.findBtId(userId);
    }

    @Override
    public User findSimpleUserInfoByUsername(String username) {
        return userMapper.findByUserName(username);
    }
}
