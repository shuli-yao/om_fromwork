package com.business.service.impl;

import com.business.mapper.mysql.UserMapper;
import com.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    UserMapper userMapper;


    @Override
    public List<Map<String, Object>> queryUserAll() {

        return userMapper.selectAllUser();
    }
}
