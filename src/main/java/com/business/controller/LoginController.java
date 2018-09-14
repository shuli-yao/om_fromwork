package com.business.controller;

import com.business.vo.Message;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginController
 * @Description 登录控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午3:46
 */
@Api(value = "/role", description = "登录控制器")
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {


    @PostMapping("/loginUser")
    public Message LoginUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse httpServletResponse) throws IOException {
        Message<String> message = new Message<>();
        log.info("登录用户:"+username+",密码:"+password);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
            message.setCode("200");
            message.setMessage("登录成功");
        } catch (UnknownAccountException uae) {
            message.setCode("400");
            message.setMessage("用户名不存在!");
        } catch (IncorrectCredentialsException ice) {
            message.setCode("400");
            message.setMessage("密码不正确!");
        } catch (LockedAccountException lae) {
            message.setCode("400");
            message.setMessage("账户已被冻结!");
        }
        return message;
    }
}
