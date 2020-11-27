package it.geosolutions.swgeoserver.comm.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import it.geosolutions.swgeoserver.comm.base.ShpCharset;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static org.geotools.data.Transaction.AUTO_COMMIT;

/**
 * @author wangkang
 * @email iwuang@qq.com
 * @date 2019/1/24 16:27
 */
public class Geotools {

    private static Logger logger = Logger.getLogger(Geotools.class);
    private static DataStore postgisDatasore;

    /**
     * @param postgisDatasore 静态PGDatastore获取默认，实例化PGDatastore指定自定义
     */
    public Geotools(DataStore postgisDatasore) {
        if (postgisDatasore == null) {
            postgisDatasore = PGDatastore.getDefeaultDatastore();

        }
        this.postgisDatasore = postgisDatasore;
    }

    public Geotools() {
        postgisDatasore = PGDatastore.getDefeaultDatastore();
    }

    /**
     * 通用，要素集写入postgis
     *
     * @param featureCollection
     * @param pgtableName       postgis创建的数据表
     * @return
     */
    public static boolean write2pg(FeatureCollection featureCollection, String pgtableName) {
        boolean result = false;
        try {
            if (Utility.isEmpty(featureCollection) || Utility.isEmpty(pgtableName)) {
                logger.error("参数无效");
                return result;
            }
            SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(simpleFeatureType);

            typeBuilder.setName(pgtableName);

            SimpleFeatureType newtype = typeBuilder.buildFeatureType();
            postgisDatasore.createSchema(newtype);

//            FeatureIterator iterator = featureCollection.features();
//            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = postgisDatasore.getFeatureWriterAppend(pgtableName, AUTO_COMMIT);

//            int i = 0;
//            while (iterator.hasNext()) {
//                i++;
//                Feature feature = iterator.next();
//                SimpleFeature simpleFeature = featureWriter.next();
//                Collection<Property> properties = feature.getProperties();
//                Iterator<Property> propertyIterator = properties.iterator();
//                while (propertyIterator.hasNext()) {
//                    Property property = propertyIterator.next();
//                    simpleFeature.setAttribute(property.getName().toString(), property.getValue());
//                }
//                featureWriter.write();
//            }

//            iterator.close();
//            featureWriter.close();
//            logger.info("上传成功！   i="+i);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("失败", e);
        }
        return false;
    }

    /**
     * 通用，要素集写入postgis
     *
     * @param featureCollection
     * @param pgtableName       postgis创建的数据表
     * @return
     */
    public static boolean updateWrite2pg(FeatureCollection featureCollection, String pgtableName) {
        boolean result = false;
        try {
            if (Utility.isEmpty(featureCollection) || Utility.isEmpty(pgtableName)) {
                logger.error("参数无效");
                return result;
            }
            SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(simpleFeatureType);

            typeBuilder.setName(pgtableName);

//            SimpleFeatureType newtype = typeBuilder.buildFeatureType();
//            postgisDatasore.getSchema(pgtableName);

            FeatureIterator iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = postgisDatasore.getFeatureWriterAppend(pgtableName, AUTO_COMMIT);

            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                Iterator<Property> propertyIterator = properties.iterator();
                while (propertyIterator.hasNext()) {
                    Property property = propertyIterator.next();
                    simpleFeature.setAttribute(property.getName().toString(), property.getValue());
                }
                featureWriter.write();
            }
            iterator.close();
            featureWriter.close();
            logger.info("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("失败", e);
        }
        return false;
    }

    /**
     * 通用，要素集写入postgis
     *
     * @param featureCollection
     * @param pgtableName       postgis创建的数据表
     * @return
     */
    public static boolean update2pg(FeatureCollection featureCollection, String pgtableName) {
        boolean result = false;
        try {
            if (Utility.isEmpty(featureCollection) || Utility.isEmpty(pgtableName)) {
                logger.error("参数无效");
                return result;
            }

            //根据表名获取source
            SimpleFeatureSource featureSource = postgisDatasore.getFeatureSource(pgtableName);

            if (featureSource!=null){
                if( featureSource instanceof SimpleFeatureStore){
                    SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
                    SimpleFeatureCollection featureCollection1 = GetFeatureCollection(featureCollection,featureSource);
                    //创建事务
                    Transaction session = new DefaultTransaction("Adding");
                    featureStore.setTransaction( session );
                    try {
                        List<FeatureId> added = featureStore.addFeatures( featureCollection1 );
                        System.out.println( "Added "+added );
                        //提交事务
                        session.commit();
                    }
                    catch (Throwable t){
                        System.out.println( "Failed to add features: "+t );
                        try {
                            //事务回归
                            session.rollback();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }


                result = true;
                return result;
            }else{
                return result;
            }

//            SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
//            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
//            typeBuilder.init(simpleFeatureType);
//
//            typeBuilder.setName(pgtableName);
//
//            SimpleFeatureType newtype = typeBuilder.buildFeatureType();
//            postgisDatasore.createSchema(newtype);
//
//            FeatureIterator iterator = featureCollection.features();
//            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = postgisDatasore.getFeatureWriterAppend(pgtableName, AUTO_COMMIT);
//
//            while (iterator.hasNext()) {
//                Feature feature = iterator.next();
//                SimpleFeature simpleFeature = featureWriter.next();
//                Collection<Property> properties = feature.getProperties();
//                Iterator<Property> propertyIterator = properties.iterator();
//                while (propertyIterator.hasNext()) {
//                    Property property = propertyIterator.next();
//                    simpleFeature.setAttribute(property.getName().toString(), property.getValue());
//                }
//                featureWriter.write();
//            }
//            iterator.close();
//            featureWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("失败", e);
        }
        return false;
    }

    /**
     * @param featureSource:要素源
     * @return SimpleFeatureCollection类型
     */
    public static SimpleFeatureCollection GetFeatureCollection (FeatureCollection featureCollection,SimpleFeatureSource featureSource) {
        GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureCollection collection =null;
        //获取类型
        SimpleFeatureType TYPE = featureSource.getSchema();
        System.out.println(TYPE);
        //创建要素集合
        List<SimpleFeature> features = new ArrayList<>();
        //创建要素模板
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        //创建要素并添加道集合
        double latitude = Double.parseDouble("39.9");
        double longitude = Double.parseDouble("116.3");
        String name ="beijing";
        int number = Integer.parseInt("16");
        //创建一个点geometry
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        //添加的数据一定按照SimpleFeatureType给的字段顺序进行赋值
        //添加name属性
        featureBuilder.add(name);
        //添加number属性
        featureBuilder.add(number);
        //添加geometry属性
        featureBuilder.add(point);
        //构建要素
        SimpleFeature feature = featureBuilder.buildFeature(null);
        System.out.println(feature);
        //将要素添加到要素几何中
        features.add(feature);
        collection = new ListFeatureCollection(TYPE, features);
        return collection;
    }

    /**
     * featureCollection写入到shp的datastore
     *
     * @param featureCollection
     * @param shpDataStore
     * @param geomFieldName     featureCollectio中的矢量字段，postgis可以修改使用不同的表名，默认为geom
     * @return
     */
    public static boolean write2shp(FeatureCollection featureCollection, ShapefileDataStore shpDataStore, String geomFieldName) {
        boolean result = false;
        if (Utility.isEmpty(geomFieldName)) {
            geomFieldName = featureCollection.getSchema().getGeometryDescriptor().getType().getName().toString();
        }
        try {
            FeatureIterator<SimpleFeature> iterator = featureCollection.features();
            //shp文件存储写入
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = shpDataStore.getFeatureWriter(shpDataStore.getTypeNames()[0], AUTO_COMMIT);
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                Iterator<Property> propertyIterator = properties.iterator();

                while (propertyIterator.hasNext()) {
                    Property property = propertyIterator.next();
                    if (property.getName().toString().equalsIgnoreCase(geomFieldName)) {
                        simpleFeature.setAttribute("the_geom", property.getValue());
                    } else {
                        simpleFeature.setAttribute(property.getName().toString(), property.getValue());
                    }
                }
                featureWriter.write();
            }
            iterator.close();
            featureWriter.close();
            shpDataStore.dispose();
        } catch (Exception e) {
            logger.error("失败", e);
        }
        return false;
    }

    /**
     * 方法重载，默认使用UTF-8的Shp文件
     * @param geojsonPath
     * @param shpfilepath
     * @return
     */
    public boolean geojson2shp(String geojsonPath, String shpfilepath) {
        return geojson2shp(geojsonPath, shpfilepath, ShpCharset.UTF_8);
    }

    /**
     * Geojson转成shpfile文件
     *
     * @param geojsonPath
     * @param shpfilepath
     * @return
     */
    public boolean geojson2shp(String geojsonPath, String shpfilepath,Charset shpChart) {
        boolean result = false;
        try {
            Utility.valiFileForRead(geojsonPath);
            FeatureJSON featureJSON = new FeatureJSON();
            featureJSON.setEncodeNullValues(true);
            FeatureCollection featureCollection = featureJSON.readFeatureCollection(new InputStreamReader(new FileInputStream(geojsonPath),"utf-8"));

            File file = new File(shpfilepath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

            //postgis获取的Featuretype获取坐标系代码
            SimpleFeatureType pgfeaturetype = (SimpleFeatureType) featureCollection.getSchema();

            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(pgfeaturetype);
            typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
            pgfeaturetype = typeBuilder.buildFeatureType();
            //设置成utf-8编码
            shpDataStore.setCharset(shpChart);
            shpDataStore.createSchema(pgfeaturetype);
            write2shp(featureCollection, shpDataStore, "");
            result = true;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * geojson文件写入到postgis里
     *
     * @param geojsonPath
     * @param pgtableName
     * @return
     */
    public boolean geojson2pgtable(String geojsonPath, String pgtableName) {
        boolean result = false;
        try {
            if (Utility.isEmpty(geojsonPath) || Utility.isEmpty(pgtableName)) {
                return result;
            }
            FeatureJSON featureJSON = new FeatureJSON();
            FeatureCollection featureCollection = featureJSON.readFeatureCollection(new FileInputStream(geojsonPath));
            write2pg(featureCollection, pgtableName);
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 重载方法，默认UTF-8编码SHP文件
     *
     * @param shpPath
     * @param geojsonPath
     * @return
     */
    public boolean shp2geojson(String shpPath, String geojsonPath) {
        return shp2geojson(shpPath, geojsonPath, ShpCharset.UTF_8);
    }

    /**
     * shp转成geojson，保留15位小数
     *
     * @param shpPath     shp的路径
     * @param geojsonPath geojson的路径
     * @return
     */
    public boolean shp2geojson(String shpPath, String geojsonPath, Charset shpCharset) {
        boolean result = false;
        try {
            if (!Utility.valiFileForRead(shpPath) || Utility.isEmpty(geojsonPath)) {
                return result;
            }
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            ContentFeatureSource featureSource = shapefileDataStore.getFeatureSource();
            ContentFeatureCollection contentFeatureCollection = featureSource.getFeatures();
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
            Utility.valiFileForWrite(geojsonPath);
            featureJSON.writeFeatureCollection(contentFeatureCollection, new File(geojsonPath));
            shapefileDataStore.dispose();
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean shp2pgtable(String shpPath, String pgtableName,int i) {
        return shp2pgtable(shpPath, pgtableName, ShpCharset.UTF_8,i);
    }

    /**
     * shpfile文件导入到postgis中
     *
     * @param shpPath
     * @param pgtableName
     * @return
     */
    public static boolean shp2pgtable(String shpPath, String pgtableName, Charset shpCharset,int i) {
        boolean result = false;
        try {
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            String typeName = shapefileDataStore.getTypeNames()[0];
            FeatureType schema = shapefileDataStore.getSchema();
            SimpleFeatureSource simpleFeatureSource = shapefileDataStore.getFeatureSource(pgtableName);
            FeatureCollection featureCollection = shapefileDataStore.getFeatureSource().getFeatures();
            if (i==0){
                write2pg(featureCollection, pgtableName);
            }else{
                List shpAttrList = new ArrayList<>();
                List tableAttrList = new ArrayList<>();
                List<AttributeDescriptor> shpAttr = shapefileDataStore.getFeatureSource().getSchema()
                        .getAttributeDescriptors();
                for (AttributeDescriptor attr : shpAttr) {
                    shpAttrList.add(attr.getLocalName());
                }
                System.out.println(shpAttrList);

                SimpleFeatureType pgschema = postgisDatasore.getSchema(pgtableName);
                List<AttributeDescriptor> tableAttr = pgschema.getAttributeDescriptors();
                for (AttributeDescriptor attr : tableAttr) {
                    tableAttrList.add(attr.getLocalName());
                }
                System.out.println(tableAttrList);

                for (Object object : shpAttrList) {
                    if (!tableAttrList.contains(object)){
                        logger.error("列名错误!");
                        return true;
                    }
                }

                updateWrite2pg(featureCollection, pgtableName);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * postgis数据表导出到成shpfile
     *
     * @param pgtableName
     * @param shpPath
     * @param geomField   postgis里的字段
     * @return
     */
    public boolean pgtable2shp(String pgtableName, String shpPath, String geomField) {
        boolean result = false;
        try {

            FeatureSource featureSource = postgisDatasore.getFeatureSource(pgtableName);

            // 初始化 ShapefileDataStore
            File file = new File(shpPath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

            //postgis获取的Featuretype获取坐标系代码
            SimpleFeatureType pgfeaturetype = ((SimpleFeatureSource) featureSource).getSchema();
            String srid = pgfeaturetype.getGeometryDescriptor().getUserData().get("nativeSRID").toString();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(pgfeaturetype);
            if (!srid.equals("0")) {
                CoordinateReferenceSystem crs = CRS.decode("EPSG:" + srid, true);
                typeBuilder.setCRS(crs);
            }
            pgfeaturetype = typeBuilder.buildFeatureType();
            //设置成utf-8编码
            shpDataStore.setCharset(Charset.forName("utf-8"));
            shpDataStore.createSchema(pgfeaturetype);
            write2shp(featureSource.getFeatures(), shpDataStore, geomField);
            result = true;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * postgis指定的数据表转成geojson文件保留15位小数
     *
     * @param pgtableName 表名
     * @param geojsonpath geojson存放位置
     * @return
     */
    public boolean pgtable2geojson(String pgtableName, String geojsonpath) {
        boolean result = false;
        try {
            FeatureSource featureSource = postgisDatasore.getFeatureSource(pgtableName);
            FeatureCollection featureCollection = featureSource.getFeatures();

            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
            featureJSON.setEncodeNullValues(true);

            String s = featureJSON.toString(featureCollection);
            FileUtils.writeStringToFile(new File(geojsonpath), s, Charsets.toCharset("utf-8"), false);
            result = true;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public boolean deletePgtable(String pgtableName) {
        boolean result = false;
        try {
            postgisDatasore.removeSchema(pgtableName);
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /*
    //    测试调试专用，成功清除所有的sw开头的表（用来存储矢量数据的表）
        public boolean clearSWTable() throws Exception {
            postgisDatasore.removeSchema();
            //relkind char r = 普通表，i = 索 引， S = 序列，v = 视 图， m = 物化视图， c = 组合类型，t = TOAST表， f = 外部 表
            String strtables = " select string_agg(relname ,\',\') from pg_class where relname like \'%sw_%\'  and relkind=\'r\' ";
            List list =  postgisDatasore.getSessionFactory().getCurrentSession().createQuery(strtables).list();
            list.get(0).toString();
            Integer integer = 0;
            if (list.size() > 0) {
                integer = temp.getSessionFactory().getCurrentSession().createQuery("drop table " + strtables).executeUpdate();
            }
    //        与表有关联的其他序列自动删除
            String sequence = " select string_agg(relname ,\',\') from pg_class where relname like \'%sw_%\' and relkind=\'S\' and relname!=\'txsw_seq\'";
            resultSet = st.executeQuery(sequence);
            while (resultSet.next()) {
                sequence = resultSet.getString(1);
            }
            System.out.println("所有非txsw_seq的序列：" + sequence);
            i = st.executeUpdate("drop SEQUENCE " + strtables);
            return integer == 0 ? true : false;
        }
    */
    public static boolean testCreatFeature(String featurePath) {
        boolean result = false;
        try {


            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }











    /**
     * 读取shp文件
     *
     * @param shpFile
     * @return
     */
    public static SimpleFeatureSource readSHP(String shpFile) {
        SimpleFeatureSource featureSource = null;
        try {
            File file = new File(shpFile);
            ShapefileDataStore shpDataStore = null;

            shpDataStore = new ShapefileDataStore(file.toURI().toURL());
            //设置编码
            Charset charset = Charset.forName("UTF-8");
//            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String tableName = shpDataStore.getTypeNames()[0];
            featureSource = shpDataStore.getFeatureSource(tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return featureSource;
    }

    /**
     * 建表
     * @param shpPath
     * @param tableName
     * @param spatialName
     * @return
     */
    public static DataStore createTable(String shpPath, String fileName,String tableName,String spatialName) {
        try {
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath+fileName).toURI().toURL());
            shapefileDataStore.setCharset(ShpCharset.UTF_8);
//            FeatureCollection featureCollection = shapefileDataStore.getFeatureSource().getFeatures();

            SimpleFeatureSource featureSource = shapefileDataStore.getFeatureSource(tableName);
            SimpleFeatureType schema = featureSource.getSchema();

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
            simpleFeatureTypeBuilder.setName(("shp_"+tableName).toString().toLowerCase());
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

            //使用新的属性类
            simpleFeatureTypeBuilder.addAll(newAttributeDescriptors);

            //获取新属性值
            schema = simpleFeatureTypeBuilder.buildFeatureType();
            //创建数据表
            postgisDatasore.createSchema(schema);
            it.geosolutions.swgeoserver.comm.utils.FileUtils.delFolder(shpPath);
            logger.info("创建成功！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return postgisDatasore;
    }

    //shp数据写入数据库
    public static void writeShp2DataBin(DataStore ds, SimpleFeatureSource featureSource) {

        SimpleFeatureType schema = featureSource.getSchema();
        //开始写入数据
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(schema.getTypeName().toLowerCase(), Transaction.AUTO_COMMIT);
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureIterator features = featureCollection.features();

            CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
            CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
//            CoordinateReferenceSystem crs = coordinateReferenceSystem;

            //默认转为wgs84
            CoordinateReferenceSystem worldCRS = DefaultGeographicCRS.WGS84;
            boolean lenient = true; // allow for some error due to different datums
            //定义坐标转换
            MathTransform transform = CRS.findMathTransform(crs, worldCRS, lenient);

            while (features.hasNext()) {
                writer.hasNext();
                SimpleFeature next = writer.next();
                SimpleFeature feature = features.next();
                for (int i = 0; i < feature.getAttributeCount(); i++) {
                    next.setAttribute(i, feature.getAttribute(i));
                }

                //坐标系转换
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                Geometry geometry2 = JTS.transform(geometry, transform);
                next.setDefaultGeometry(geometry2);

                //写入数据库
                writer.write();
            }
            writer.close();
            ds.dispose();
            System.out.println("导入成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (FactoryException e) {
            e.printStackTrace();
        } catch (TransformException e) {
            e.printStackTrace();
        }


    }

    //测试代码
    public static void main(String[] args) {

//        OracleSpatialDataSource oracleSpatialDataSource = new OracleSpatialDataSource("postgis",
//                "127.0.0.1", 5432, "xzqh", "postgres", "123456", "public");
//        JDBCDataStore connnection2mysql = getDataStore(oracleSpatialDataSource);
//        SimpleFeatureSource featureSource = readSHP("C:/Users/Administrator/Desktop/folder/T_ZYSJ_RGZL_NXLW.shp");
//        SimpleFeatureSource featureSource = readSHP("C:/Users/Administrator/Desktop/folder/T_ZYSJ_FSYL_NXPY.shp");
//        SimpleFeatureSource featureSource = readSHP("C:/Users/Administrator/Desktop/folder/T_ZYSJ_FSYL_NXPL.shp");
        long shp2pgStart = System.currentTimeMillis();
        DataStore pgds = PGDatastore.getDefeaultDatastore();
        SimpleFeatureSource featureSource = readSHP("C:\\Users\\x\\Desktop\\yunnan02\\yunnan02\\yunnan02.shp");
        DataStore ds = createTable("C:\\Users\\x\\Desktop\\yunnan02\\yunnan02\\","yunnan02.shp","yunnan02","");
//        writeShp2DataBin(ds, featureSource);
        long shp2pgEnd = System.currentTimeMillis();
        System.out.println("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
    }

}
