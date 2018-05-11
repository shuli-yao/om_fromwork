package com.megvii.utlis;

import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Megvii on 2017/11/20.
 */
public class ConfigUtlis <T>{
  public T convertConfig(Properties properties,T t){
    Map<String, String> map = new HashMap<String, String>((Map) properties);
    BeanMap beanMap = BeanMap.create(t);
    beanMap.putAll(map);
    return  t;
  }
}
