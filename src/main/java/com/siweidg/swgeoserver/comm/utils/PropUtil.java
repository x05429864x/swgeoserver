package com.siweidg.swgeoserver.comm.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 读写配置文件
 * 路径格式  ： config.properties
 *
 */
public class PropUtil {

//    public static File file;
//
//    static{
//        file = new File(PropUtil.class.getResource("config.properties").getFile());
//    }

    public static String getProperty(String key) {
        String value = "";
        Properties props = new Properties();
        try {
            InputStream resourceAsStream = new FileInputStream(ResourceUtils.getFile("classpath:config.properties"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
            props.load(bf);
            value = props.getProperty(key)!=null?props.getProperty(key):key;
            resourceAsStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }



    public static void setProperty(Map<String, String> data){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(ResourceUtils.getFile("classpath:config.properties"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            prop.load(bf);
            OutputStream out = new FileOutputStream(ResourceUtils.getFile("classpath:config.properties"));
            if (data != null) {
                Iterator<Map.Entry<String,String>> iter = data.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String,String> entry = iter.next();
                    prop.setProperty(entry.getKey().toString(),URLEncoder.encode(entry.getValue().toString(),"UTF-8"));
                }
            }
            prop.store(new OutputStreamWriter(out, "utf-8"),null);
            fis.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getKey(String value){
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(ResourceUtils.getFile("classpath:config.properties"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            prop.load(bf);
//            HashMap<String,String> map = prop
            Map<String, String> map = new HashMap<String, String>((Map) prop);
            String key = null;
            //Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
            for(String getKey: map.keySet()){
                if(map.get(getKey).equals(value)){
                    key = getKey;
                }
            }
            return key!=null?key:value;
        } catch (IOException e) {
            e.printStackTrace();
            return value;
        }


        //这个key肯定是最后一个满足该条件的key.
    }



    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    public static Map<String,String> readXML(String path){
        Map<String,String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            File file = new ClassPathResource(path).getFile();
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(file);
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Element srs = bookStore.element("srs");
            Element nativeBoundingBox = bookStore.element("nativeBoundingBox");
            Element minx = nativeBoundingBox.element("minx");
            Element maxx = nativeBoundingBox.element("maxx");
            Element miny = nativeBoundingBox.element("miny");
            Element maxy = nativeBoundingBox.element("maxy");
            Iterator it = bookStore.elementIterator();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}