package space.jachen.rbac_shiro.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import space.jachen.rbac_shiro.domain.Permission;

import java.util.List;

/**
 *
 * SELECT *FROM `user` u
 * -- 查询用户对应的角色映射关系
 * LEFT JOIN user_role ur ON u.id=ur.user_id
 * -- 查询用户对应的角色信息
 * LEFT JOIN role r on ur.role_id=r.id
 * -- 查询角色和权限的关系
 * LEFT JOIN role_permission rp on r.id = rp.role_id
 * -- 查询角色对应的权限信息（某个用户具备的角色和权限集合）
 * LEFT JOIN permission p ON rp.permission_id = p.id
 * WHERE u.id=1
 *
 * @author JaChen
 * @date 2023/1/19 18:17
 */
public interface PermissionMapper {


    @Select("SELECT p.id as id,p.name as name,p.url as url " +
            "FROM role_permission rp " +
            "LEFT JOIN permission p ON rp.permission_id = p.id " +
            "WHERE rp.role_id = #{roleId}")
    List<Permission> findPermissionListByRoleId(@Param("roleId") int roleId);


}
