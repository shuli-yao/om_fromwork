package com.megvii.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName Swagger2Configuration
 * @Description spring 使用swagger2配置类
 * @Author shuliyao
 * @CreateTime 2018/7/20 下午2:34
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    /**
     * Swagger ui 页面显示信息
     */
    private final String SWAGGER2_API_TITLE = "江苏省厅下载工具操作接口";    //标题
    private final String SWAGGER2_API_DESCRIPTION = "江苏省厅图片落地工具";
    private final String SWAGGER2_API_CONTACT = "shuliyao";
    private final String SWAGGER2_API_VERSION = "1.0";

    /**
     * createRestApi
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * apiInfo
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(SWAGGER2_API_TITLE)
                .description(SWAGGER2_API_DESCRIPTION)
                .contact(SWAGGER2_API_CONTACT)
                .version(SWAGGER2_API_VERSION)
                .build();
    }
}
