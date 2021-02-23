package com.siweidg.swgeoserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBoot启动类
 * 打成war包时需要改造 继承SpringBootServletInitializer实现configure方法
 * 打jar包则不需要
 */
@SpringBootApplication
@EnableTransactionManagement //开启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
//@ComponentScan({"it.geosolutions.swgeoserver.config"})
@MapperScan("com.siweidg.swgeoserver.dao")
public class SwgeoserverApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //这里的SwgeoserverApplication是SpringBoot的启动类
        return builder.sources(SwgeoserverApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SwgeoserverApplication.class, args);
    }

}
