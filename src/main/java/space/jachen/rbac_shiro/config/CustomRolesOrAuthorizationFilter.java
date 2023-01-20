package space.jachen.rbac_shiro.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * 自定义filter
 *
 * 背景知识：
 * /admin/order= roles["admin, root"] ，表示 /admin/order 这个接口
 * 需要用户同时具备 admin 与 root 角色才可访问, 相当于hasAllRoles() 这个判断方法
 * 我们的需求：
 * 订单信息，可以由角色 普通管理员 admin 或者 超级管理员 root 查看
 * 只要用户具备其中一个角色即可
 *
 * @author JaChen
 * @date 2023/1/20 10:42
 */

public class CustomRolesOrAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) throws Exception {

        Subject subject = getSubject(req, resp);

        //获取当前访问路径所需要的角色集合
        String[] rolesArray = (String[]) mappedValue;

        //没有角色限制，有权限访问
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }

        //当前subject是rolesArray中的任何一个，则有权限访问
        for (String s : rolesArray) {
            if (subject.hasRole(s)) {
                return true;
            }
        }

        return false;
    }
}
