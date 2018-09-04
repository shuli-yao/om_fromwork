package com.business.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName Role
 * @Description 角色类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午6:34
 */
@Data
@ApiModel(value="角色对象",description="角色对象")
public class Role implements Serializable{

    @JsonProperty("id")
    @ApiModelProperty(value = "角色ID")
    String id;

    @JsonProperty("roleName")
    @ApiModelProperty(value = "角色名称",required=true)
    String name;

    @JsonProperty("authorizations")
    @ApiModelProperty(value = "角色所拥有权限")
    List<Authorization> authorizations;
}
