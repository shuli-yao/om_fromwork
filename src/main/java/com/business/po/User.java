package com.business.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName User
 * @Description 用户实体类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午6:34
 */
@Data
@ApiModel(value="用户对象",description="用户对象user")
public class User implements Serializable{

    @JsonProperty("id")
    @ApiModelProperty(value = "用户id",hidden = true)
    String id;

    @JsonProperty("username")
    @ApiModelProperty(value = "用户名",required=true)
    String username;

    @JsonProperty("password")
    @ApiModelProperty(value = "密码",required=true)
    String password;

    @JsonProperty("roles")
    @ApiModelProperty(value = "用户所拥有角色")
    List<Role> roles;

}
