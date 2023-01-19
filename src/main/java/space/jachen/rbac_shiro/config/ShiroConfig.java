package space.jachen.rbac_shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/1/19 22:54
 */
@Configuration
@Slf4j
public class ShiroConfig {



    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager manager){

        log.info("执行 ShiroFilterFactoryBean.shiroFilter()");

        ShiroFilterFactoryBean shiroFilterFactoryBean =
                new ShiroFilterFactoryBean();

        // 必须设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(manager);

        // 需要登录的接口 ,如果没登陆 跳转登录页面 需要调用此接口
        shiroFilterFactoryBean.setLoginUrl("/pub/need_login");

        // 登录成功,跳转url 如果为前后端分离项目 则不调用此接口
        shiroFilterFactoryBean.setSuccessUrl("/");

        // 登录后没有权限 未授权走此接口  403
        shiroFilterFactoryBean.setUnauthorizedUrl("pub/not_permit");

        // 拦截器路径 不能用hashmap
        Map<String,String> filterChainDefinitionMap =
                new LinkedHashMap<>();
        // 1、退出过滤器
        filterChainDefinitionMap.put("/logout","logout");
        // 2、匿名可以访问  游客模式
        filterChainDefinitionMap.put("/pub/**","anon");
        // 3、登录用户才能访问
        filterChainDefinitionMap.put("/authc/**","authc");
        // 4、管理员角色才能访问
        filterChainDefinitionMap.put("/admin/**","roles[admin]");
        // 5、有编辑权限才能访问
        filterChainDefinitionMap.put("/video/update","perms[video_update]");
        // .......等等  过滤链顺序执行 一般/**放在最后面

        // authc:url定义必须通过认证才能访问
        // anon:url可以匿名访问
        filterChainDefinitionMap.put("/**","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;

    }


    @Bean
    public SecurityManager securityManager(){

        // 一定调用的时Web的Manage
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // 前后端分离要设置
        defaultWebSecurityManager.setSessionManager(sessionManager());

        // setRealm放在后面 防止可能不生效
        defaultWebSecurityManager.setRealm(customRealm());

        return defaultWebSecurityManager;
    }


    @Bean
    public CustomRealm customRealm(){

        CustomRealm customRealm = new CustomRealm();

        //customRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return customRealm;

    }



    /**
     * 密码加解密规则
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();

        // 设置散列算法 ：MD5
        matcher.setHashAlgorithmName("md5");

        // 散列次数2 相当于加密俩次md5(md5(xxxx))
        matcher.setHashIterations(2);

        return matcher;
    }


    /**
     * 自定义 SessionManager
     * @return
     */
    @Bean
    public SessionManager sessionManager(){

        CustomSessionManager customSessionManager = new CustomSessionManager();

        // 设置超时时间 默认30min 会话超时  单位ms
        //customSessionManager.setGlobalSessionTimeout(20000);

        return customSessionManager;

    }



}
