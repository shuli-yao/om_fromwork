package com.business.configuration.shiro;

import com.business.po.User;
import com.business.service.UserService;
import com.sun.istack.internal.tools.DefaultAuthenticator;
import jdk.nashorn.internal.ir.IfNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.awt.*;

/**
 * @ClassName BusinessRealm
 * @Description shiro 安全控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/17 下午5:54
 */
@Component
@Slf4j
public class BusinessRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("admin");
        simpleAuthorizationInfo.addStringPermission("sys:method2");
        simpleAuthorizationInfo.addStringPermission("pressure:test");
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = authenticationToken.getPrincipal().toString();

        SimpleAuthenticationInfo info= null;
        if (username != null) {
            User user;
            try {
                user = userService.findUserByName(username);
                info = new SimpleAuthenticationInfo(username,user.getPassword(),getName());
            } catch (NullPointerException n) {
                log.info("用户名不存在!");
            }
            return info;
        }

        return null;
    }

    @Override
    public void setAuthorizationCachingEnabled(boolean authenticationCachingEnabled) {
        super.setAuthorizationCachingEnabled(false);
    }


}

