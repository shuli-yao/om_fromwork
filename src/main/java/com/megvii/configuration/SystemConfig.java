package com.megvii.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName SystemConfig
 * @Description 系统配置参数类
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午2:36
 */
@Component
public class SystemConfig{

        @Value("${photo.download.queue.size:5000}")
        public  int downloadQueueSize;    //定义下载队列长度

        @Value("${photo.download.thread.pool.size:30}")
        public  int poolSize;        //线程池大小

        @Autowired
        public RestTemplate restTemplate;

        @Value("${download.file.path:/}")
        public String downloadFilePath;

        @Value("${download.url}")
        public String url;


}
