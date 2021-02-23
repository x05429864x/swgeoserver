package com.siweidg.swgeoserver.comm.utils;

import com.siweidg.swgeoserver.entry.StyleType;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 *  xml解析工具类
 * </p>
 *
 * @author liugh
 * @since 2018/4/3
 */
public class XmlUtil {

//    private static String GEO_SERVER_PATH = ResourceBundle.getBundle("constant").getString("geoServer-dir");
//
//    private static String BUNDLE_URL = ResourceBundle.getBundle("constant").getString("geoServer-url");

    private static String XML_ELEMENT_NAME="latLonBoundingBox";

    public static void main(String[] args)throws Exception {
//        getMapUrl("resttestshp","ceshi2");
//        createMainXml();
        StyleType style = new StyleType();
        style.setDtype(6l);
        style.setFillcolor("#CCCCCC");
        style.setFillopacity("0.5");
        style.setLinetype("dash");
        style.setLinecolor("#FF6600");
        style.setLinewidth("2");
        style.setLineopacity("0.5");
        modifyStyle("E:\\usr\\local\\shpfile\\siweidg_swgeoserver_apache\\style\\",style);
    }

    //获取图集发布地址
    public static String getMapUrl(String layerId,String workspace)throws Exception{
        File file =new File("http://192.168.8.254:8080/geoserver/rest/workspaces/"+workspace);
        String[] fileList = file.list();
        StringBuilder mapUrl = new StringBuilder();
        mapUrl.append("http://192.168.8.254:8080/geoserver/rest/workspaces"+workspace)
                .append("/wms?service=WMS&version=1.1.0&request=GetMap&layers=").append(workspace+":"+layerId).append("&styles=&bbox=");
        if(fileList.length>0){
            for (String fileName:fileList) {
                if(fileName.equals(layerId)){
                    String []  coordinates = readXMLDocument(layerId,workspace);
                    mapUrl.append(coordinates[0]+","+coordinates[2]+","+coordinates[1]+","+coordinates[3]).append("&width=768&height=437&srs=").append(coordinates[4]);
                }
            }
        }else{
            return null;
        }
        return  mapUrl.toString();
    }

    private static String []  readXMLDocument(String layerId, String workspace){
        File file = new File("http://192.168.8.254:8080/geoserver/rest/workspaces"+File.separator+workspace+
                File.separator+layerId+File.separator+layerId+File.separator+"featuretype.xml");
        if (!file.exists()) {
            try {
                throw new IOException("Can't find the path");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //创建SAXReader对象
        SAXReader saxReader = new SAXReader();
        Document document;
        try {
            //读取文件 转换成Document
            document = saxReader.read(file);
            //获取根节点元素对象  遍历当前节点下的所有节点
            for (Iterator iter = document.getRootElement().elementIterator(); iter.hasNext();){
                //获取节点
                Element e1 = (Element) iter.next();

                //如果过节点的名称等于beanName那么继续进入循环读取beanName节点下的所有节点
                if(e1.getName().equalsIgnoreCase(XML_ELEMENT_NAME)){
                    String [] ss = new String[5];
                    int i =0;
                    //遍历beanName当前节点下的所有节点
                    for (Iterator iter1 = e1.elementIterator(); iter1.hasNext();){
                        Element e2 = (Element) iter1.next();
                        ss[i]= e2.getStringValue();
                        i++;
                    }
                    return ss;
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static File createMainXml(String path,StyleType styleType) throws Exception {
        File xmlfile = null;
        try {
            xmlfile = new File(path+"workStyle.sld");//具体文件路径
            if (xmlfile.exists()) {
//                FileUtils.delAllFile(path+"workStyle.sld");
            }
            Document doc = DocumentHelper.createDocument();
            //增加根节点
            Element styledLayerDescriptor = doc.addElement("StyledLayerDescriptor");
            Element namedLayer = styledLayerDescriptor.addElement("NamedLayer");
            Element userStyle = namedLayer.addElement("UserStyle");
            Element featureTypeStyle = userStyle.addElement("FeatureTypeStyle");
            Element rule = featureTypeStyle.addElement("Rule");
            Element title = rule.addElement("Title");
            Element polygonSymbolizer = rule.addElement("PolygonSymbolizer");
            Element fill = polygonSymbolizer.addElement("Fill");
            Element fillColer = fill.addElement("CssParameter");
            Element fillOpacity = fill.addElement("CssParameter");
            Element stroke = polygonSymbolizer.addElement("Stroke");
            Element strokeWidth = stroke.addElement("CssParameter");
            Element strokeColer = stroke.addElement("CssParameter");
            Element strokeOpacity = stroke.addElement("CssParameter");
            Element strokeDasharray = stroke.addElement("CssParameter");
            //节点增加值
            title.setText("workStyle");
            //节点增加属性
            styledLayerDescriptor.addAttribute("xmlns", "http://www.opengis.net/sld");
            styledLayerDescriptor.addAttribute("xmlns:sld", "http://www.opengis.net/sld");
            styledLayerDescriptor.addAttribute("xmlns:gml", "http://www.opengis.net/gml");
            styledLayerDescriptor.addAttribute("xmlns:ogc", "http://www.opengis.net/ogc");
            styledLayerDescriptor.addAttribute("version", "1.0.0");

            fillColer.addAttribute("name", "fill");
            fillOpacity.addAttribute("name","fill-opacity");
            strokeWidth.addAttribute("name", "stroke-width");
            strokeColer.addAttribute("name", "stroke");
            strokeOpacity.addAttribute("name", "stroke-opacity");
            strokeDasharray.addAttribute("name", "stroke-dasharray");
            //根据参数设置样式值

            fillColer.setText(styleType.getFillcolor());
            fillOpacity.setText(styleType.getFillopacity());
            strokeWidth.setText(styleType.getLinewidth());
            strokeColer.setText(styleType.getLinecolor());
            strokeOpacity.setText(styleType.getLineopacity());

            if("dot".equals(styleType.getLinetype())){
                strokeDasharray.setText("2 3");
            } else if("dash".equals(styleType.getLinetype())){
                strokeDasharray.setText("10 5");
            }

            //实例化输出格式对象
            OutputFormat format = OutputFormat.createPrettyPrint();
            //设置输出编码
            format.setEncoding("UTF-8");
            //创建需要写入的File对象
            xmlfile = new File(path+"workStyle.sld");//输出文件路径
            if (!xmlfile.getParentFile().exists()) {
                xmlfile.getParentFile().mkdirs();
            }
            xmlfile.createNewFile();
            //生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式

            XMLWriter writer = new XMLWriter(new FileOutputStream(xmlfile), format);
            //开始写入，write方法中包含上面创建的Document对象
            writer.write(doc);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return xmlfile;

    }


    public static File modifyStyle(String path,StyleType styleType) throws Exception {
        File xmlfile = null;
        try {
            xmlfile = new File(path+"workStyle.sld");//具体文件路径
//            if (xmlfile.exists()) {
////                FileUtils.delAllFile(path+"workStyle.sld");
//            }
            //创建dom4j解析器
            SAXReader reader = new SAXReader();
            //加载document对象
            Document doc = reader.read(xmlfile);
            //获取根节点
            Element styledLayerDescriptor = doc.getRootElement();
            Element namedLayer = styledLayerDescriptor.element("NamedLayer");
            Element userStyle = namedLayer.element("UserStyle");
            Element featureTypeStyle = userStyle.element("FeatureTypeStyle");
            boolean insert = true;
            List<Element> rules = featureTypeStyle.elements("Rule");
            for (int i=2;i<rules.size();i++){
                Element rule = rules.get(i);
                Element name = rule.element("Name");
//                System.out.println("第"+i+"次属性名称：" + name + "属性值：" + value);
                if(styleType.getDtype().toString().equals(name.getText())){
                    insert = false;
                    Element title = rule.element("Title");
                    title.setText(styleType.getStyleTypeName());
                    Element polygonSymbolizer = rule.element("PolygonSymbolizer");
                    Element fill = polygonSymbolizer.element("Fill");
                    Element stroke = polygonSymbolizer.element("Stroke");
                    //填充
                    List<Element> fillElement = fill.elements();
                    for (Element ele:fillElement){
//                        System.out.println(ele.getName() + ": " + ele.getText());
                        List<Attribute> attributeList = ele.attributes();
                        for (Attribute attr : attributeList) {
                            System.out.println(attr.getName() + ": " + attr.getValue());
                            if(attr.getValue().equals("fill")){
                                ele.setText(styleType.getFillcolor());
                            }
                            if(attr.getValue().equals("fill-opacity")){
                                ele.setText(styleType.getFillopacity());
                            }
                        }
                    }
                    //边框线
                    List<Element> strokeElement = stroke.elements();
                    for (Element ele:strokeElement){
                        System.out.println(ele.getName() + ": " + ele.getText());
                        List<Attribute> attributeList = ele.attributes();
                        for (Attribute attr : attributeList) {
                            System.out.println(attr.getName() + ": " + attr.getValue());
                            if(attr.getValue().equals("stroke")){
                                ele.setText(styleType.getLinecolor());
                            }
                            if(attr.getValue().equals("stroke-width")){
                                ele.setText(styleType.getLinewidth());
                            }
                            if(attr.getValue().equals("stroke-opacity")){
                                ele.setText(styleType.getLineopacity());
                            }
                            if(attr.getValue().equals("stroke-dasharray")){
                                if("dash".equals(styleType.getLinetype())){
                                    ele.setText("10 10");
                                }else if("dot".equals(styleType.getLinetype())){
                                    ele.setText("2 3");
                                }else if("solid".equals(styleType.getLinetype())){
                                    ele.setText("10 0");
                                }
                            }
                        }
                    }
                }
            }
            //添加类型
            if(insert){
                Element rule = featureTypeStyle.addElement("Rule");
                Element name = rule.addElement("Name");
                name.setText(styleType.getDtype().toString());
                Element title = rule.addElement("Title");
                title.setText(styleType.getDtype().toString());
                Element filter = rule.addElement("Filter");
                Element polygonSymbolizer = rule.addElement("PolygonSymbolizer");
                Element propertyIsEqualTo = filter.addElement("PropertyIsEqualTo");
                Element propertyName = propertyIsEqualTo.addElement("PropertyName");
                Element literal = propertyIsEqualTo.addElement("Literal");
                propertyName.setText("type_id");
                literal.setText(styleType.getDtype().toString());

                Element fill = polygonSymbolizer.addElement("Fill");
                Element fillColor = fill.addElement("CssParameter");
                Element fillOpacity = fill.addElement("CssParameter");
                fillColor.addAttribute("name", "fill");
                fillOpacity.addAttribute("name","fill-opacity");
                fillColor.setText(styleType.getFillcolor());
                fillOpacity.setText(styleType.getFillopacity());

                Element stroke = polygonSymbolizer.addElement("Stroke");
                Element strokeWidth = stroke.addElement("CssParameter");
                Element strokeColer = stroke.addElement("CssParameter");
                Element strokeOpacity = stroke.addElement("CssParameter");
                Element strokeDasharray = stroke.addElement("CssParameter");

                strokeWidth.addAttribute("name", "stroke-width");
                strokeColer.addAttribute("name", "stroke");
                strokeOpacity.addAttribute("name", "stroke-opacity");
                strokeDasharray.addAttribute("name", "stroke-dasharray");

                strokeWidth.setText(styleType.getLinewidth());
                strokeColer.setText(styleType.getLinecolor());
                strokeOpacity.setText(styleType.getLineopacity());
                if("dot".equals(styleType.getLinetype())){
                    strokeDasharray.setText("2 3");
                } else if("dash".equals(styleType.getLinetype())){
                    strokeDasharray.setText("10 5");
                } else if("solid".equals(styleType.getLinetype())){
                    strokeDasharray.setText("10 0");
                }
            }
//            getNodes(styledLayerDescriptor,styleType);

            /*Element namedLayer = styledLayerDescriptor.addElement("NamedLayer");
            Element userStyle = namedLayer.addElement("UserStyle");
            Element featureTypeStyle = userStyle.addElement("FeatureTypeStyle");
            Element rule = featureTypeStyle.addElement("Rule");
            Element title = rule.addElement("Title");
            Element polygonSymbolizer = rule.addElement("PolygonSymbolizer");
            Element fill = polygonSymbolizer.addElement("Fill");
            Element fillColer = fill.addElement("CssParameter");
            Element fillOpacity = fill.addElement("CssParameter");
            Element stroke = polygonSymbolizer.addElement("Stroke");
            Element strokeWidth = stroke.addElement("CssParameter");
            Element strokeColer = stroke.addElement("CssParameter");
            Element strokeOpacity = stroke.addElement("CssParameter");
            Element strokeDasharray = stroke.addElement("CssParameter");

            //节点增加值
            title.setText("workStyle");
            //节点增加属性
            styledLayerDescriptor.addAttribute("xmlns", "http://www.opengis.net/sld");
            styledLayerDescriptor.addAttribute("xmlns:sld", "http://www.opengis.net/sld");
            styledLayerDescriptor.addAttribute("xmlns:gml", "http://www.opengis.net/gml");
            styledLayerDescriptor.addAttribute("xmlns:ogc", "http://www.opengis.net/ogc");
            styledLayerDescriptor.addAttribute("version", "1.0.0");

            fillColer.addAttribute("name", "fill");
            fillOpacity.addAttribute("name","fill-opacity");
            strokeWidth.addAttribute("name", "stroke-width");
            strokeColer.addAttribute("name", "stroke");
            strokeOpacity.addAttribute("name", "stroke-opacity");
            strokeDasharray.addAttribute("name", "stroke-dasharray");
            //根据参数设置样式值

            styleType.getDtype();
            fillColer.setText(styleType.getFillcolor());
            fillOpacity.setText(styleType.getFillopacity());
            strokeWidth.setText(styleType.getLinewidth());
            strokeColer.setText(styleType.getLinecolor());
            strokeOpacity.setText(styleType.getLineopacity());

            if("dot".equals(styleType.getLinetype())){
                strokeDasharray.setText("2 3");
            } else if("dash".equals(styleType.getLinetype())){
                strokeDasharray.setText("10 5");
            }*/

            //实例化输出格式对象
            OutputFormat format = OutputFormat.createPrettyPrint();
            //设置输出编码
            format.setEncoding("UTF-8");
            //创建需要写入的File对象
            xmlfile = new File(path+"workStyle.sld");//输出文件路径
            if (!xmlfile.getParentFile().exists()) {
                xmlfile.getParentFile().mkdirs();
            }
//            xmlfile.createNewFile();
            //生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式

            XMLWriter writer = new XMLWriter(new FileOutputStream(xmlfile), format);
            //开始写入，write方法中包含上面创建的Document对象
            writer.write(doc);
            writer.close();
            return xmlfile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        }


    }


    public static void getNodes(Element node,StyleType styleType) {
        System.out.println("--------------------");

        //当前节点的名称、文本内容和属性
        System.out.println("当前节点名称：" + node.getName());//当前节点名称
        System.out.println("当前节点的内容：" + node.getTextTrim());//当前节点名称

        List<Attribute> listAttr = node.attributes();//当前节点的所有属性的list
        for (Attribute attr : listAttr) {//遍历当前节点的所有属性
            String name = attr.getName();//属性名称
            String value = attr.getValue();//属性的值
            System.out.println("属性名称：" + name + "属性值：" + value);
            if(node.getName().equals("Rule")){
                if(styleType.getDtype().equals(value)){

                }
            }

        }
        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for (Element e : listElement) {//遍历所有一级子节点
            getNodes(e,styleType);//递归
        }



    }

}
