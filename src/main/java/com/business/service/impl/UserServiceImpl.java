package com.business.service.impl;

import com.business.mapper.mysql.UserJoinRoleMapper;
import com.business.mapper.mysql.UserMapper;
import com.business.po.Authorization;
import com.business.po.Role;
import com.business.po.User;
import com.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Description 用户实现类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午4:36
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired(required = false)
    UserMapper userMapper;

    @Autowired(required = false)
    UserJoinRoleMapper userJoinRoleMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public String addUser(User user) {
        String resultStr = "失败！";
        int result = userMapper.insertUser(user);
        if(result >0){

            resultStr = "成功";

            for (Role role : user.getRoles()) {

                int joinResult =  userJoinRoleMapper.insertUserJoinRole(role.getId(),role.getId());

                if(joinResult <=0){
                    resultStr="失败";
                }
            }
        }
        return resultStr;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public String updateUser(User user) {
        return userMapper.updateUset(user)>=1?"成功":"失败";
    }

    @Override
    public List<User> findUserByAll() {
        return userMapper.selectByAll();
    }

    @Override
    public User findUserById(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public String removeUser(String id) {
        return userMapper.deleteById(id)>=1?"成功":"失败";
    }

    @Override
    public List<Map<String, Object>> queryUserAll() {

        return null;
    }
}
