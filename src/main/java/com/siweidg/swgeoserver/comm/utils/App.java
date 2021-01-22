package com.siweidg.swgeoserver.comm.utils;


import org.apache.log4j.Logger;
import org.geotools.jdbc.JDBCDataStore;

/**
 * @author wangkang
 * @email iwuang@qq.com
 * @date 2019/1/24 14:46
 */
public class App {

    public static void main(String[] args) {

        PGDatastore pgDatastore = new PGDatastore();
        JDBCDataStore datastore = PGDatastore.getDefeaultDatastore();
        Geotools geotools = new Geotools(datastore);
        String geojsonpath = "C:\\test\\ChinaWorldCitysBigbelin\\chinaCompany2.geojson";
        String shpfilepath = "C:\\Users\\x\\Desktop\\geoserver\\china\\china\\china.shp";
//        String pgtableName = "MuchBigPolygonn";
        String pgtableName = "china";

//        geotools.geojson2pgtable(geojsonpath, pgtableName);
//        geotools.geojson2shp(geojsonpath, shpfilepath);
//        geotools.shp2geojson(shpfilepath, geojsonpath);
        geotools.shp2pgtable(shpfilepath, pgtableName,0);
        utility.tagLast("shp导入postgis");

//        geotools.pgtable2geojson(pgtableName, geojsonpath);
//        geotools.pgtable2shp(pgtableName, shpfilepath, "geom");


    }

    private static Logger logger = Logger.getLogger(App.class);
    private static Utility utility = new Utility();

}
