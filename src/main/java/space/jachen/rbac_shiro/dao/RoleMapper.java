package space.jachen.rbac_shiro.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import space.jachen.rbac_shiro.domain.Role;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/1/19 18:15
 */
public interface RoleMapper {

    /**
     * 要多表关联查询
     * @param userId
     * @return
     */
    @Select("SELECT " +
            "ur.role_id as id," +
            "r.name as name," +
            "r.description as description " +
            "FROM user_role ur " +
            "LEFT JOIN role r on ur.role_id=r.id " +
            "WHERE ur.id=#{userId}")
    @Results(
            value = {
                    @Result(id = true,property = "id",column = "id"),
                    @Result(property = "name",column = "name"),
                    @Result(property = "description",column = "description"),
                    @Result(property = "permissionList",column = "id",
                    many = @Many(select = "space.jachen.rbac_shiro.dao.PermissionMapper.findPermissionListByRoleId",fetchType = FetchType.DEFAULT)
                    )
            }
    )
    List<Role> findRoleListByUserId(@Param("userId")int userId);


}
