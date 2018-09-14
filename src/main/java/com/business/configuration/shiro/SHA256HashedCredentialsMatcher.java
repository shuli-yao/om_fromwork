package com.business.configuration.shiro;

import com.business.EncryptUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @ClassName SHA256HashedCredentialsMatcher
 * @Description 自定义sha256密码验证器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/9/4 下午6:17
 */
public class    SHA256HashedCredentialsMatcher implements CredentialsMatcher{
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //获取用户输入账户密码
        UsernamePasswordToken autoken = (UsernamePasswordToken) authenticationToken;

        //获取db中存储的账户密码
        SimpleAuthenticationInfo sinfo = (SimpleAuthenticationInfo)authenticationInfo;

        //加密用户输入的账户密码
        String ps = String.valueOf(autoken.getPassword()).toString();
        String password = EncryptUtils.sha256Encrypt(ps);


        boolean match = equals(sinfo.getCredentials().toString(),password);

        return match;
    }

    private boolean equals(String s, String s1) {
        System.out.println(s+"-------------"+s1);
        if(s.equals(s1)){
            return true;
        }
        return false;
    }
}
