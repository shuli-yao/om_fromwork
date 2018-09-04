package com.business.service;

import com.business.po.Authorization;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName AuthorizationService
 * @Description 权限服务接口
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午3:10
 */
public interface AuthorizationService extends Serializable{

    String addAuthorization(Authorization authoriza);

    String updateAuthorization(Authorization authoriza);

    List<Authorization> findAuthorizationByAll();

    Authorization findAuthorizationById(String id);

    String removeAuthorization(String id);
}
