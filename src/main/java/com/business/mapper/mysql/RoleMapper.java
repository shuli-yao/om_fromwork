package com.business.mapper.mysql;

import com.business.po.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * @ClassName RoleMapper
 * @Description 角色数据库操作接口
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午3:35
 */
public interface RoleMapper {

    @Insert("insert t_role(name) values(#{name})")
    @SelectKey(statement = "select LAST_INSERT_ID() ",keyProperty = "id",before = false,resultType=String.class)
    Integer insertRole(Role role);

    @Update("update t_role set name=#{name} where id = #{id}")
    Integer updateUset(Role role);

    @Delete("delete t_role u where u.id = #{id}")
    Integer deleteById(String id);

    @Select("select * from t_role")
    List<Role> selectByAll();

    @Select("select * from t_role where id =#{id}")
    Role selectById(String id);


}
