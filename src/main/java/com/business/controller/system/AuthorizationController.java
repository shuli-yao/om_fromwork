package com.business.controller.system;

import com.business.po.Authorization;
import com.business.service.AuthorizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @ClassName AuthorizationController
 * @Description 权限控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午6:47
 */
@Api(value = "/authorization",description = "权限控制器")
@RestController
@RequestMapping("/authorization")
@RequiresRoles(value = {"admin"},logical = Logical.AND)
public class AuthorizationController {

    @Autowired
    AuthorizationService authorizationService;

    @PostMapping("/add")
    @ApiOperation(value = "新增",response = String.class)
    public String addAuthorization(@RequestBody Authorization authorization){
        return authorizationService.addAuthorization(authorization);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改",response = String.class)
    public String updateAuthorization(@RequestBody Authorization authorization){
        return authorizationService.updateAuthorization(authorization);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "查找",response = String.class)
    public List<Authorization> findAuthorization(){
        return authorizationService.findAuthorizationByAll();
    }

    @PostMapping("/remove/{authorizationId}")
    @ApiOperation(value = "删除",response = String.class)
    public String removeAuthorization(@PathVariable("authorizationId") String authorizationId){
        return authorizationService.removeAuthorization(authorizationId);
    }
}
