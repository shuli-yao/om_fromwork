package com.megvii.component.run.impl;

import com.megvii.component.network.NetworkSend;
import com.megvii.component.network.bean.NetworkSendObject;
import com.megvii.component.run.RunService;

/**
 * @ClassName HttpRunImpl
 * @Description 接口请求run方法
 * @Author shuliyao
 * @CreateTime 2018/7/23 下午4:05
 */
public class NetworkRunImpl implements RunService{

    NetworkSendObject networkSendObject = null;

    NetworkSend networkSend = null;

    public NetworkRunImpl(NetworkSend networkSend, NetworkSendObject networkSendObject){
        this.networkSend =networkSend;
        this.networkSendObject = networkSendObject;
    }

    @Override
    public void run() {
        networkSend.send(networkSendObject.getUrl(),networkSendObject.getBody());
    }
}
