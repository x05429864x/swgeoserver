package it.geosolutions.swgeoserver.comm.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * \* User: x
 * \* Date: 2020/11/10
 * \* Time: 15:07
 * \* Description:
 * \
 */
public class MBG {
    //该配置文件放在src\\main\\resources\\该路径下即可
    public static void main(String[] args) {
        args = new String[] { "-configfile", "src\\main\\resources\\mybatis-generator.xml", "-overwrite" };
        ShellRunner.main(args);
    }
}