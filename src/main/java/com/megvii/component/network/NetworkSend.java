package com.megvii.component.network;

/**
 * @ClassName HttpSend
 * @Description http请求发送规范接口
 * @Author shuliyao
 * @CreateTime 2018/7/23 下午4:12
 */
public interface NetworkSend{

     void send(String url,String body);
}
