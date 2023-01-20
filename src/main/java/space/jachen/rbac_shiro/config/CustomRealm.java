package space.jachen.rbac_shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;
import space.jachen.rbac_shiro.domain.Permission;
import space.jachen.rbac_shiro.domain.Role;
import space.jachen.rbac_shiro.domain.User;
import space.jachen.rbac_shiro.service.UserService;

import javax.annotation.Resource;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 自定义Realm
 *
 * @author JaChen
 * @date 2023/1/19 22:28
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {


    @Resource
    private UserService userService;

    /**
     * 进行权限校验时调用
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {

        log.info("授权调用 : doGetAuthorizationInfo");
        User u = (User) principal.getPrimaryPrincipal();

        User user = userService.findAllUserInfoByUsername(u.getUsername());

        ArrayList<String> stringRoleList = new ArrayList<>();
        ArrayList<String> stringPermissionList = new ArrayList<>();

        List<Role> roleList = user.getRoleList();

        for (Role role : roleList) {
            stringRoleList.add(role.getName());
            List<Permission> permissionList = role.getPermissionList();
            for (Permission permission : permissionList) {
                if (permission!=null){
                    stringPermissionList.add(permission.getName());
                }
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(stringRoleList);
        simpleAuthorizationInfo.addRoles(stringPermissionList);

        return simpleAuthorizationInfo;

    }

    /**
     * 登录认证调用
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        log.info("登录认证调用 : doGetAuthenticationInfo");

        // 1、从token中获取用户输入信息
        String username = (String) token.getPrincipal();

        // 2、根据username从数据库查出用户对象
        User info = userService.findAllUserInfoByUsername(username);
        String password = info.getPassword();
        if (StringUtils.isEmpty(password)){
            return null;
        }

        return new SimpleAuthenticationInfo(info,password,this.getName());
    }
}
