package com.business.vo;


import lombok.Data;

/**
 * @ClassName Message
 * @Description 返回结果对象
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/9/5 下午2:40
 */
@Data
public class Message<T>{

    String code;

    String message;

    T Data;
}
