package com.siweidg.swgeoserver.comm.utils;

import org.mybatis.generator.api.ShellRunner;

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