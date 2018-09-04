package com.business.mapper.mysql;

import com.business.po.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserMapper
 * @Description 用户数据库操作接口
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午5:23
 * */
public interface UserMapper {

    @Insert("insert t_user(username,password) values(#{username},#{password})")
    @SelectKey(statement = "select LAST_INSERT_ID() ",keyProperty = "id",before = false,resultType=String.class)
    Integer insertUser(User user);

    @Update("update t_user set username=#{username},password=#{password} where id = #{id}")
    Integer updateUset(User user);

    @Delete("delete t_user u where u.id = #{id}")
    Integer deleteById(String id);


    @Select("select * from t_user")
    List<User> selectByAll();

    @Select("select * from t_user where name =#{name}")
    User selectByName(String name);

    @Select("select * from t_user where id =#{id}")
    User selectById(String id);


}

