package com.megvii.component.network.bean;

import lombok.Data;

/**
 * @ClassName HttpSendObject
 * @Description 发送http请求参数要求对象
 * @Author shuliyao
 * @CreateTime 2018/7/23 下午3:16
 */
@Data
public class NetworkSendObject {
    String url;
    String body;
}
