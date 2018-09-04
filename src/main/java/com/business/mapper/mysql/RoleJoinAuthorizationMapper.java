package com.business.mapper.mysql;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleAuthorizationMapper
 * @Description 角色权限关联操作
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午4:36
 */
public interface RoleJoinAuthorizationMapper {

    @Insert("insert t_role_authorization(role_id,authorization_id) values(#{roleId},#{authorizationId})")
    Integer insertRoleJoinAuthorization(@Param("roleId") String roleId, @Param("authorizationId") String authorizationId);

    @Delete("delete t_role_authorization where role_id = #{roleId} and authorization_id = #{authorizationId}")
    Integer deleteRoleJoinAuthorization(@Param("roleId") String roleId, @Param("authorizationId") String authorizationId);

    @Select("select * from t_role_authorization where role_id = #{roleId}")
    List<Map<String,String>> selectByRoleId(String roleId);

    @Select("select * from t_role_authorization where authorization_id = #{authorizationId}")
    List<Map<String,String>> selectByAuthorizationId(String authorizationId);

    @Select("select * from t_role_authorization where roleId = #{roleId} and authorizationId=#{authorizationId}")
    Map<String,String> selectByRoleIdAndAuthorizationId(@Param("roleId") String roleId, @Param("authorizationId") String authorizationId);

    @Select("select * from t_role_authorization where id = id")
    Map<String,String> selectById(String id);


}
