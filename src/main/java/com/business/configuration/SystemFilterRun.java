package com.business.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName SystemFilterRun
 * @Description 拦截器使用类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午3:20
 */
@Configuration
public class SystemFilterRun implements WebMvcConfigurer{

//    @Autowired
//    ShiroFilter shiroFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        System.out.println("执行");
//        registry.addInterceptor((HandlerInterceptor) shiroFilter);
    }
}
