package com.megvii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName ConcurrentApplication
 * @Description 测试用启动类
 * @Author shuliyao
 * @CreateTime 2018/7/24 下午2:41
 */
@SpringBootApplication
public class ConcurrentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrentApplication.class,args);
    }
}
