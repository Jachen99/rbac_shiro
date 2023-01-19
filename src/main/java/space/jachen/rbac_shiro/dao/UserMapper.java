package space.jachen.rbac_shiro.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import space.jachen.rbac_shiro.domain.User;

/**
 * @author JaChen
 * @date 2023/1/19 18:06
 */
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User findByUserName(@Param("username")String username);

    @Select("select * from user where id = #{userId}")
    User findBtId(@Param("userId")int id);

    @Select("select * from user where username = #{username} and pwd = #{pwd}")
    User findByUsernameAndPassword(
            @Param("username")String username,
            @Param("pwd")String pwd);


}
