package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @ClassName FrameworkApplication
 * @Description 电商架构启动类
 * @Author colin_xun@163.com
 * @CreateTime 2018/8/15 上午11:29
 */
@SpringBootApplication
@MapperScan("com.business.mapper.mysql")
public class FrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkApplication.class, args);
    }
}

