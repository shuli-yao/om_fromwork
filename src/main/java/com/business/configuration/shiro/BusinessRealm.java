package com.business.configuration.shiro;

import com.business.EncryptUtils;
import com.business.po.User;
import com.business.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        //进入系统默认超级管理员用户认证
        if(username!=null && "admin".equals(username)){
            //加密
            String password =EncryptUtils.sha256Encrypt("123456a");
            info = new SimpleAuthenticationInfo(username,password,getName());
            return info;
        }

        //进入正常登录验证，通过查询db信息验证用户
        if (username != null) {
            User user;
            try {
                user = userService.findUserByName(username);
                info = new SimpleAuthenticationInfo(username,user.getPassword(),getName());
            } catch (NullPointerException n) {
                log.info("没有查询到相应的用户名信息!");

            }
            return info;
        }

        return null;
    }

    @Override
    public void setAuthorizationCachingEnabled(boolean authenticationCachingEnabled) {
        super.setAuthorizationCachingEnabled(false);
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher(new SHA256HashedCredentialsMatcher());
    }
}

