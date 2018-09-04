package com.business.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SystemController
 * @Description 系统控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午6:09
 */
@RestController
@RequestMapping("/sys")
public class SystemController {

    @GetMapping("/method1")
    @RequiresPermissions("sys:method1")
    @ApiOperation(value = "m1")
    public String method1(){
        return "method1";
    }

    @GetMapping("/method2")
    @RequiresPermissions("sys:method2")
    @RequiresRoles(value = {"admin","root"},logical = Logical.OR)
    @ApiOperation(value = "m2")
    public String method2(){
        return "method2";
    }
}
