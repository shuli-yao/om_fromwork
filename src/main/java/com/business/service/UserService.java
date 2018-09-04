package com.business.service;


import com.business.po.User;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserService
 * @Description 用户服务借口类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午4:25
 */
public interface UserService extends Serializable{

    String addUser(User user);

    String updateUser(User uesr);

    List<User> findUserByAll();

    User findUserById(String id);

    User findUserByName(String name);

    String removeUser(String id);

    List<Map<String,Object>> queryUserAll();
}
