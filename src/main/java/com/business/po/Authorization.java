package com.business.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Authorization
 * @Description 权限类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/20 下午6:35
 */
@Data
@ApiModel(value="权限对象",description="权限对象")
public class Authorization implements Serializable {

    @JsonProperty("id")
    @ApiModelProperty(value = "权限id")
    String id;

    @JsonProperty("name")
    @ApiModelProperty(value = "权限名称",required=true)
    String name;
}
