package com.business.mapper.mysql;

import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserMapper
 * @Description db
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午5:23
 */
public interface UserMapper {

    @Select("select * from t_user")
    List<Map<String,Object>> selectAllUser();
}
