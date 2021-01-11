package test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import it.geosolutions.swgeoserver.comm.base.ShpCharset;
import it.geosolutions.swgeoserver.comm.init.Constants;
import org.apache.commons.lang.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.jdbc.JDBCDataStore;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
public class testGeotools {
    public static SimpleFeatureSource readSHP( String shpfile){
        SimpleFeatureSource featureSource =null;
        try {
            File file = new File(shpfile);
            ShapefileDataStore shpDataStore = null;

            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("UTF-8");
            shpDataStore.setCharset(charset);
            String tableName = shpDataStore.getTypeNames()[0];
            featureSource =  shpDataStore.getFeatureSource (tableName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return featureSource;
    }

    public static JDBCDataStore  connnection2mysql(String host,String dataBase,int port,String userName,String pwd ){
        JDBCDataStore ds=null;
        DataStore dataStore=null;
        //连接数据库参数
        java.util.Map params = new java.util.HashMap();
        params.put(PostgisNGDataStoreFactory.DBTYPE.key, Constants.DBTYPE);
        params.put(PostgisNGDataStoreFactory.HOST.key, Constants.PG_HOST);
        params.put(PostgisNGDataStoreFactory.PORT.key, new Integer(Constants.PG_PROT));
        params.put(PostgisNGDataStoreFactory.DATABASE.key, Constants.PG_DB);
        params.put(PostgisNGDataStoreFactory.SCHEMA.key, Constants.SCHEMA);
        params.put(PostgisNGDataStoreFactory.USER.key, Constants.PG_USERNAME);
        params.put(PostgisNGDataStoreFactory.PASSWD.key, Constants.PG_PWD);
        try {
            dataStore=DataStoreFinder.getDataStore(params);
            if (dataStore!=null) {
                ds=(JDBCDataStore)dataStore;
                System.out.println(dataBase+"连接成功");
            }else{

                System.out.println(dataBase+"连接失败");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return ds;
    }

    public static JDBCDataStore createTable(JDBCDataStore ds, SimpleFeatureSource featureSource){
        SimpleFeatureType schema = featureSource.getSchema();
        try {
//            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath+fileName).toURI().toURL());
//            shapefileDataStore.setCharset(ShpCharset.UTF_8);
//            FeatureCollection featureCollection = shapefileDataStore.getFeatureSource().getFeatures();


//            String[] allTableNames = postgisDatasore.getTypeNames();

            //如果存在,则先删除
            //这里根据需求决定是否删除表
//            if (allTableNames != null && ArrayUtils.contains(allTableNames, schema.getTypeName())) {
//                postgisDatasore.removeSchema(schema.getTypeName());
//                //防止shp文件名大写的问题
//            } else if (ArrayUtils.contains(allTableNames, schema.getTypeName().toLowerCase())) {
//                postgisDatasore.removeSchema(schema.getTypeName().toLowerCase());
//            } else {
//
//            }

            //由于此类属性内部不可变,所以需要获取旧属性,重新赋值给新建属性.
            //获取旧属性
            List<AttributeDescriptor> oldAttributeDescriptors = schema.getAttributeDescriptors();
            //新属性
            List<AttributeDescriptor> newAttributeDescriptors = new ArrayList<>();

            //新建feature构造器
            SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
            //设置表名
            simpleFeatureTypeBuilder.setName("sys_jdxz1".toString().toLowerCase());
            //设置坐标系
            simpleFeatureTypeBuilder.setCRS(schema.getCoordinateReferenceSystem());

            //获取属性
            for (AttributeDescriptor oldAttributeDescriptor : oldAttributeDescriptors) {

                //属性构造器
                AttributeTypeBuilder build = new AttributeTypeBuilder();

                build.init(oldAttributeDescriptor.getType());
                build.setNillable(true);

                //获取字段名,改为小写
                String name = StringUtils.isNotEmpty(oldAttributeDescriptor.getLocalName()) ? oldAttributeDescriptor.getLocalName().toLowerCase() : oldAttributeDescriptor.getLocalName();
//                if (oldAttributeDescriptor instanceof GeometryDescriptor) {
//
//                    //修改空间字段名
////                    name = StringUtils.isNotEmpty(spatialName) ? spatialName : "shape";
//
////                    GeometryTypeImpl geometryDescriptor = (GeometryTypeImpl) oldAttributeDescriptor.getType();
//                    //获取坐标系,用于坐标系转换
////                    coordinateReferenceSystem = geometryDescriptor.getCoordinateReferenceSystem();
//
//                } else {
//                }

                //设置字段名
                build.setName(name);

                //创建新的属性类
                AttributeDescriptor descriptor = build.buildDescriptor(name, oldAttributeDescriptor.getType());

                newAttributeDescriptors.add(descriptor);
            }
            simpleFeatureTypeBuilder.getDefaultGeometry();
            //使用新的属性类
            simpleFeatureTypeBuilder.addAll(newAttributeDescriptors);
            simpleFeatureTypeBuilder.getDefaultGeometry();
            //获取新属性值
            schema = simpleFeatureTypeBuilder.buildFeatureType();
            schema.getGeometryDescriptor();
            //创建数据表
            ds.createSchema(schema);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ds;
    }

    public static void writeShp2Mysql(JDBCDataStore ds, SimpleFeatureSource featureSource ){
        SimpleFeatureType schema = featureSource.getSchema();
        //开始写入数据
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(schema.getTypeName().toLowerCase(), Transaction.AUTO_COMMIT);
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureIterator features = featureCollection.features();
            while (features.hasNext()) {
                writer.hasNext();
                SimpleFeature next = writer.next();
                SimpleFeature feature = features.next();
                for (int i = 0; i < feature.getAttributeCount(); i++) {
                    next.setAttribute(i,feature.getAttribute(i) );
                }
                writer.write();
            }
            writer.close();
            ds.dispose();
            System.out.println("导入成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // SimpleFeatureIterator itertor = featureSource.getFeatures() // .features(); //create the builder SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);



    }

    //测试代码
    public static void main(String[] args) throws IOException {
        JDBCDataStore connnection2mysql = testGeotools.connnection2mysql("localhost", "testjdbc", 3306, "root", "mysql");
        SimpleFeatureSource featureSource = readSHP("C:\\Users\\x\\Desktop\\test_data\\xianzhi\\jdxz\\jd1\\sys_jdxz.shp");
        List shpAttrList = new ArrayList<>();
        List tableAttrList = new ArrayList<>();
//        SimpleFeatureType pgschema = connnection2mysql.getSchema("jd1");
//        List<AttributeDescriptor> tableAttr = pgschema.getAttributeDescriptors();
//        for (AttributeDescriptor attr : tableAttr) {
//            tableAttrList.add(attr.getLocalName());
//        }
//        System.out.println(tableAttrList);
        JDBCDataStore ds = createTable(connnection2mysql, featureSource);
        writeShp2Mysql(connnection2mysql, featureSource);
    }
}
