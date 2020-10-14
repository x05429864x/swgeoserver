package it.geosolutions.swgeoserver.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2     //开启Swagger2
public class SwaggerConfig {

    @Value("${app.evniroment}")
    private String evniroment;

    private static final List<String> excludedPathPrefix = Arrays.asList(
            "/swagger","/pg"
    );

    private Boolean checkEvniroment = false;
    //配置Swagger的Docket的bean实例
    @Bean
    public Docket docket() {
        if (evniroment.equals("dev")){
            checkEvniroment = true;
        }
        return new Docket(DocumentationType.SWAGGER_2).groupName("xqr")
                .apiInfo(apiInfo())
                .enable(checkEvniroment)
                .select()
                //RequestHandlerSelectors:配置要扫描接口的方式
                //basePackage:指定要扫描的包
                //any():扫描全部
                //none():不扫描
                .apis(RequestHandlerSelectors.basePackage("it.geosolutions.swgeoserver.controller"))
                //paths():过滤路径
                .paths((s) -> {
                    for(String pathPrefix : excludedPathPrefix) {
                        if(StringUtils.startsWith(s, pathPrefix)) {
                            return false;
                        }
                    }
                    return true;
                })
                .build();
    }

    //配置Swagger信息的apiInfo
    private ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("siweidg","http://www.spacewillinfo.com","email@siweidg.com");
        return new ApiInfo(
                "数海API文档",
                "",
                "v1.0",
                "",
                contact,
                "Apache 2.0",
                "http://www.apache.rog/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }
}