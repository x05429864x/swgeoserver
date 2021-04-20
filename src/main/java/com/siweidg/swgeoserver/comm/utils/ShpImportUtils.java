package com.siweidg.swgeoserver.comm.utils;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

public class ShpImportUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShpImportUtils.class);

    /**
     * 新建数据库表并导入
     * @param tablename
     * @return
     * @throws IOException
     */
    public static String importShp(String shppath, String tablename) throws IOException {
        try{
            DataStore dataStore = PGDatastore.getDefeaultDatastore();
            if (!validateShp(shppath, true)){
                return "路径不存在";
            }
            ShapefileDataStore shapefileDataStore = null;
            shapefileDataStore = new ShapefileDataStore(new File(shppath).toURI().toURL());
            shapefileDataStore.setCharset(Charset.forName("utf-8"));
            FeatureSource featureSource = shapefileDataStore.getFeatureSource();
            FeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureType shpfeaturetype = shapefileDataStore.getSchema();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(shpfeaturetype);
            typeBuilder.setName(tablename);
            SimpleFeatureType newtype = typeBuilder.buildFeatureType();
            dataStore.createSchema(newtype);

            FeatureIterator iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = dataStore.getFeatureWriterAppend(tablename, Transaction.AUTO_COMMIT);

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
            shapefileDataStore.dispose();
            dataStore.dispose();
//            LOGGER.info("\nshp导入postgis成功");
            return "导入postgis成功";
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }


    public static String updateShp(String shppath, String tablename) throws IOException {
        try{
            DataStore dataStore = PGDatastore.getDefeaultDatastore();
            if (!validateShp(shppath, true)){
                return "路径不存在";
            }
//        DataStore pgDatastore = postgisDataStore.getInstance();
            ShapefileDataStore shapefileDataStore = null;
            shapefileDataStore = new ShapefileDataStore(new File(shppath).toURI().toURL());
            shapefileDataStore.setCharset(Charset.forName("utf-8"));
            FeatureSource featureSource = shapefileDataStore.getFeatureSource();
            FeatureCollection featureCollection = featureSource.getFeatures();
            SimpleFeatureType shpfeaturetype = shapefileDataStore.getSchema();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(shpfeaturetype);
            typeBuilder.setName(tablename);
//            SimpleFeatureType newtype = typeBuilder.buildFeatureType();
//            dataStore.createSchema(newtype);
//        logger.info("\npostgis创建数据表成功");
            FeatureIterator iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = dataStore.getFeatureWriterAppend(tablename, Transaction.AUTO_COMMIT);

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
            shapefileDataStore.dispose();
            dataStore.dispose();
//            LOGGER.info("\nshp导入postgis成功");
            return "导入postgis成功";
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * 验证路径是否存在
     * @param dirPath
     * @param b
     * @return
     */
    private static boolean validateShp(String dirPath,Boolean b) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return false;
        }
        return true;
    }
}
