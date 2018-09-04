package com.business.service.impl;

import com.business.mapper.mysql.RoleJoinAuthorizationMapper;
import com.business.mapper.mysql.RoleMapper;
import com.business.po.Authorization;
import com.business.po.Role;
import com.business.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleService
 * @Description 角色实现类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午3:21
 */
@Service
public class RoleServiceImpl implements RoleService{


    @Autowired(required = false)
    RoleMapper roleMapper;

    @Autowired(required = false)
    RoleJoinAuthorizationMapper roleJoinAuthorizationMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public String addRole(Role role) {
        String resultStr = "失败！";

        int result = roleMapper.insertRole(role);

        if(result >=1){

            resultStr = "成功";

            for (Authorization authorization : role.getAuthorizations()) {

               Map<String,String> map = roleJoinAuthorizationMapper.selectByRoleIdAndAuthorizationId(
                   role.getId(),authorization.getId());

               if(null!=map)
                    continue;

               int joinResult =  roleJoinAuthorizationMapper.insertRoleJoinAuthorization(
                   role.getId(),authorization.getId());

               if(joinResult <=0){
                   resultStr="失败";
               }
            }
        }
        return resultStr;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public String updateRole(Role role) {
        return roleMapper.updateUset(role)>=1?"成功":"失败";
    }

    @Override
    public List<Role> findRoleByAll() {
        return roleMapper.selectByAll();
    }

    @Override
    public Role findRoleById(String id) {
        return roleMapper.selectById(id);
    }

    @Override
    public String removeRole(String id) {
        return roleMapper.deleteById(id)>=1?"成功":"失败";
    }
}
