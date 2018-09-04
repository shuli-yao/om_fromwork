package com.business.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName LoginController
 * @Description 登录控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午3:46
 */
@Api(value = "/role", description = "登录控制器")
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {


    @PostMapping("/loginUser")
    public String LoginUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info("登录用户:"+username+",密码:"+password);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException uae) {
            log.info("用户名不存在！");
            return "login";
        } catch (IncorrectCredentialsException ice) {
            log.info("密码不正确！");
            return "login";
        } catch (LockedAccountException lae) {
            log.info("账户已被冻结！");
            return "login";
        }

        log.info("登陆：" + subject.getPrincipal());
        return "index";
    }
}
