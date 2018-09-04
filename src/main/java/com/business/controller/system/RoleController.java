package com.business.controller.system;

import com.business.po.Role;
import com.business.service.RoleService;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @ClassName RoleController
 * @Description 角色控制器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午6:47
 */
@Api(value = "/role",description = "角色控制器")
@RestController
@RequestMapping("/role")
@RequiresRoles(value = {"admin"},logical = Logical.AND)
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping("/add")
    @ApiOperation(value = "新增",response = String.class)
    public String addRole(@RequestBody Role role){
        return roleService.addRole(role);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改",response = String.class)
    public String updateRole(@RequestBody Role role){
        return roleService.updateRole(role);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "查找",response = String.class)
    public List<Role> findRole(){
        return roleService.findRoleByAll();
    }

    @PostMapping("/remove/{roleId}")
    @ApiOperation(value = "删除",response = String.class)
    public String removeRole(@PathVariable("roleId") String roleId){
        return roleService.removeRole(roleId);
    }
}
