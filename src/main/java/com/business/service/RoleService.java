package com.business.service;

import com.business.po.Role;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RoleService
 * @Description 角色服务接口
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午3:11
 */
public interface RoleService extends Serializable{

    String addRole(Role role);

    String updateRole(Role role);

    List<Role> findRoleByAll();

    Role findRoleById(String id);

    String removeRole(String id);
}
