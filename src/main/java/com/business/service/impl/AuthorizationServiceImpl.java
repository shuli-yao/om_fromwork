package com.business.service.impl;

import com.business.mapper.mysql.AuthorizationMapper;
import com.business.po.Authorization;
import com.business.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName AuthorizationServiceImpl
 * @Description 权限实现类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/21 下午3:21
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService{

    @Autowired(required = false)
    AuthorizationMapper authorizaMapper;

    @Override
    public String addAuthorization(Authorization role) {
        return authorizaMapper.insertAuthorization(role)>=1?"成功":"失败";
    }

    @Override
    public String updateAuthorization(Authorization role) {
        return authorizaMapper.updateUset(role)>=1?"成功":"失败";
    }

    @Override
    public List<Authorization> findAuthorizationByAll() {
        return authorizaMapper.selectByAll();
    }

    @Override
    public Authorization findAuthorizationById(String id) {
        return authorizaMapper.selectById(id);
    }

    @Override
    public String removeAuthorization(String id) {
        return authorizaMapper.deleteById(id)>=1?"成功":"失败";
    }
}
