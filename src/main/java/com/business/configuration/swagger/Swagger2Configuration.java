package com.business.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName Swagger2Configuration
 * @Description swagger API初始配置类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午2:20
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    /**
     * Swagger ui 页面显示信息
     */
    private final String SWAGGER2_API_TITLE = "演示集成架构 by 电商";    //标题
    private final String SWAGGER2_API_DESCRIPTION = "根据电商所需求特点搭建一整套，电商用架构。";
    private final String SWAGGER2_API_VERSION = "1.0";


    private final String SWAGGER2_API_CONTACT_NAME="shuli-yao";
    private final String SWAGGER2_API_CONTACT_URL="https://github.com/shuli-yao/";
    private final String SWAGGER2_API_CONTACT_EMAIL="ysl1397940314@163.com";


    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(newApiInfo());
    }

    public ApiInfo newApiInfo(){
        return  new ApiInfoBuilder()
            .title(SWAGGER2_API_TITLE)
            .description(SWAGGER2_API_DESCRIPTION)
            .contact(newContact())
            .version(SWAGGER2_API_VERSION)
            .build();
    }


    public Contact newContact(){
        return new Contact(SWAGGER2_API_CONTACT_NAME,SWAGGER2_API_CONTACT_URL,SWAGGER2_API_CONTACT_EMAIL);
    }
}
