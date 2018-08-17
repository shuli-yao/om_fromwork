package com.business.service;


import java.util.List;
import java.util.Map;

/**
 * @ClassName UserService
 * @Description 用户服务借口类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午4:25
 */
public interface UserService {


     List<Map<String,Object>> queryUserAll();
}
