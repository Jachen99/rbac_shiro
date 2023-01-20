package space.jachen.rbac_shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/1/19 22:54
 */
@Configuration
@Slf4j
public class ShiroConfig {


    /**
     * 管理shiro一些bean的生命周期 即bean初始化 与销毁
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 加入注解的使用，不加入这个AOP注解不生效(shiro的注解 例如 @RequiresGuest)
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     *  用来扫描上下文寻找所有的Advistor(通知器), 将符合条件的Advisor应用到切入点的Bean中，
     *  需要在LifecycleBeanPostProcessor创建后才可以创建
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public  DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }


    /**
     * 配置redisManager
     *
     */
    public RedisManager getRedisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("192.168.253.134");
        redisManager.setPort(6379);
        return redisManager;
    }


    /**
     * 配置具体cache实现类
     * @return
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        //使用自定义的cacheManager
        redisCacheManager.setRedisManager(getRedisManager());

        //设置过期时间，单位是秒，20s,数据及时同步
        redisCacheManager.setExpire(20);

        return redisCacheManager;
    }



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

        // 1、设置自定义filter javax.servlet.Filter;
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("roleOrFilter",new CustomRolesOrAuthorizationFilter());
        // 2、自定义filter与shiroFilterChain绑定
        shiroFilterFactoryBean.setFilters(filterMap);

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
        filterChainDefinitionMap.put("/admin/**","roleOrFilter[admin,root]");
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

        //使用自定义的cacheManager
        defaultWebSecurityManager.setCacheManager(cacheManager());

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

        //配置session持久化
        customSessionManager.setSessionDAO(redisSessionDAO());

        return customSessionManager;

    }


    /**
     * 自定义session持久化
     * @return
     */
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(getRedisManager());
        return redisSessionDAO;
    }



}
