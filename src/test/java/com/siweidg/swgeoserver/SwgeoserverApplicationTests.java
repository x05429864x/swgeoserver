package com.siweidg.swgeoserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwgeoserverApplicationTests {

    @Autowired
    private Environment env;



    @Test
    public void contextLoads() {

    }

    /**
     *author: itmyhome
     */
//    @Test
//    public static void main(String[] args) {
//        File file = new File("HelloWorld.java");
//        String fileName = file.getName();
//        String suffix = fileName.substring(0,fileName.lastIndexOf("."));
//        System.out.println(suffix);
//    }

    @Test
    public void filll(){
        File file = new File("HelloWorld.java");
        String fileName = file.getName();
        String suffix = fileName.substring(0,fileName.lastIndexOf("."));
        System.out.println(suffix);
    }

    @Test
    public void Test3() {
        System.out.println("12.5的四舍五入值：" + Math.round(12.5));//13
        System.out.println("-12.5的四舍五入值：" + Math.round(-12.5));//12
    }

    @Test
    public void Test4() {
        BigDecimal d = new BigDecimal(100000); // 存款
        BigDecimal r = new BigDecimal(0.001875 * 3); // 利息
        BigDecimal i = d.multiply(r).setScale(2, RoundingMode.HALF_EVEN); // 使用银行家算法

        System.out.println("季利息是：" + i);//100000 * 0.001875 * 3
    }

    @Test
    public void Test5() {
        double f = 111231.5585;
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        System.out.println("f1:" + f1);//111231.56
    }

    @Test
    public void Test6() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String s1 = df.format(3.1415926);
        String s2 = df.format(3.1465926);
        System.out.println("s:" + s1);//3.14
        System.out.println("s:" + s2);//3.15
    }

    @Test
    public void Test7() {
        String s = String.format("%.2f",3.1415926);
        String s1 = String.format("%.2f",3.1465926);
        System.out.println("S:"+s);//3.14
        System.out.println("S:"+s1);//3.15

    }

    @Test
    public void test() throws IOException {
//        String xml = "http://192.168.8.254:8080/geoserver/rest/workspaces/workspaceTest/datastores/storeName_SHP/featuretypes/Hotel.xml";
//        Map<String,String> map = PropUtil.readXML(xml);



//        File zipFile = new ClassPathResource("config.properties").getFile();
//        Map<String,String> map  = new HashMap();
//        map.put("key","测试k111");
//        map.put("value","测试value111");




//        PropUtil.getV("aaaa");
//        System.out.println(PropUtil.getV("aaaa"));
//
//        PropUtil.setV(map);
//        System.out.println(PropUtil.getV("测试k111"));
//        PropUtil.setProperty(map);
//        String s = PropUtil.getProperty("测试k");
//        System.out.println(s);
//        PropUtil.setValue(zipFile.getAbsolutePath(),map);
    }
}
