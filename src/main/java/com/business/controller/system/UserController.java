package com.business.controller.system;

import com.business.EncryptUtils;
import com.business.po.User;
import com.business.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserController
 * @Description 用户控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午6:46
 */
@Api(value = "/user", description = "用户控制器")
@RestController
@RequestMapping("/user")
@RequiresRoles(value = {"admin"}, logical = Logical.AND)
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    @ApiOperation(value = "新增", response = String.class)
    public String addUser(@RequestBody User user) {

        //加密
        String password = EncryptUtils.sha256Encrypt(user.getPassword());
        user.setPassword(password);

        return userService.addUser(user);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", response = String.class)
    public String updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "查找", response = String.class)
    public List<User> findUser() {
        return userService.findUserByAll();
    }

    @PostMapping("/remove/{userId}")
    @ApiOperation(value = "删除", response = String.class)
    public String removeUser(@PathVariable("userId") String userId) {
        return userService.removeUser(userId);
    }
}
