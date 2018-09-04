package com.business.mapper.mysql;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserJoinRoleMapper
 * @Description 用户角色关联操作接口
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午5:43
 */
public interface UserJoinRoleMapper {

    @Insert("insert t_user_role(user_id,role_id) values(#{userId},#{roleId})")
    Integer insertUserJoinRole(@Param("userId") String userId, @Param("roleId") String roleId);

    @Delete("delete t_user_role where user_id = #{userId} and role_id = #{roleId}")
    Integer deleteUserJoinRole(@Param("userId") String userId, @Param("roleId") String roleId);

    @Select("select * from t_user_role where user_id = #{userId}")
    List<Map<String,String>> selectByUserId(String userId);

    @Select("select * form t_user_role where role_id = #{roleId}")
    List<Map<String,String>> selectByRoleId(String roleId);

    @Select("select * from t_user_role where id = #{id}")
    Map<String,String> selectById(String id);
}
