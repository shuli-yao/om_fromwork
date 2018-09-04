package com.business.mapper.mysql;

import com.business.po.Authorization;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ClassName AuthorizationMapper
 * @Description 权限数据库操作接口
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午3:36
 */
public interface AuthorizationMapper {

    @Insert("insert t_authorization(name) values(#{name})")
    Integer insertAuthorization(Authorization authorization);

    @Update("update t_authorization set name=#{name} where id = #{id}")
    Integer updateUset(Authorization authorization);

    @Select("select * from t_authorization")
    List<Authorization> selectByAll();

    @Select("select * from t_authorization")
    Authorization selectById(String id);

    @Select("select * from t_authorization where id =#{id}")
    Integer deleteById(String id);
}
