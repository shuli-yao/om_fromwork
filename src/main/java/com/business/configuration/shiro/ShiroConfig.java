package com.business.configuration.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description shiro框架配置类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/17 下午6:00
 */
@Configuration
public class ShiroConfig {

    private DefaultWebSecurityManager defaultSecurityManager = null;

    @Bean
    public ShiroFilterFactoryBean shirFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(getSecurityManager());

        //拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

        //防止登录成功之后下载favicon.ico
        filterChainDefinitionMap.put("/favicon.ico", "anon");

        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/login/loginUser", "anon");

        filterChainDefinitionMap.put("/css/*", "anon");
        filterChainDefinitionMap.put("/img/*", "anon");
        filterChainDefinitionMap.put("/js/*", "anon");

        filterChainDefinitionMap.put("/pressure/*", "roles[pressure]");
        filterChainDefinitionMap.put("/sys/*", "roles[admin]");

        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login.html");

        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/login.html");

        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.html");

        return shiroFilterFactoryBean;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {

        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor
            = new AuthorizationAttributeSourceAdvisor();

        authorizationAttributeSourceAdvisor.setSecurityManager(getSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }


    public SecurityManager getSecurityManager() {
        if (defaultSecurityManager == null) {
            defaultSecurityManager = new DefaultWebSecurityManager();
            defaultSecurityManager.setRealm(new BusinessRealm());
        }
        return defaultSecurityManager;
    }
}
