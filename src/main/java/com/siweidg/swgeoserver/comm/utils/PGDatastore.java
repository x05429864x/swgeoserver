package com.siweidg.swgeoserver.comm.utils;



import com.siweidg.swgeoserver.comm.init.Constants;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.jdbc.JDBCDataStore;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangkang
 * @email iwuang@qq.com
 * @date 2019/1/24 16:28
 */
public class PGDatastore {

    private static DataStore dataStore = null;
    private static Logger logger = Logger.getLogger(PGDatastore.class);


    @NotNull
    private String host;
    @NotNull
    private String port;
    @NotNull
    private String dbname;
    @NotNull
    private String schema;
    @NotNull
    private String username;
    @NotNull
    private String password;

    public PGDatastore() {
    }


    /**
     * 只有get或者Create时才会实际连接数据库
     * @param host
     * @param port
     * @param dbname
     * @param schema
     * @param username
     * @param password
     */
    public PGDatastore(String host, String port, String dbname, String schema, String username, String password) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.schema = schema;
        this.username = username;
        this.password = password;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        PGDatastore.logger = logger;
    }

    public static JDBCDataStore getDefeaultDatastore() {
        JDBCDataStore ds=null;
        DataStore dataStore=null;
        if (dataStore == null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(PostgisNGDataStoreFactory.DBTYPE.key, Constants.DBTYPE);
            params.put(PostgisNGDataStoreFactory.HOST.key, Constants.PG_HOST);
            params.put(PostgisNGDataStoreFactory.PORT.key, new Integer(Constants.PG_PROT));
            params.put(PostgisNGDataStoreFactory.DATABASE.key, Constants.PG_DB);
            params.put(PostgisNGDataStoreFactory.SCHEMA.key, Constants.SCHEMA);
            params.put(PostgisNGDataStoreFactory.USER.key, Constants.PG_USERNAME);
            params.put(PostgisNGDataStoreFactory.PASSWD.key, Constants.PG_PWD);
            try {
                dataStore = DataStoreFinder.getDataStore(params);

                if (dataStore != null) {
                    ds = (JDBCDataStore) dataStore;
                    System.out.println("连接成功");
                } else {

                    System.out.println("连接失败");
                }

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
        return ds;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataStore getDatastore() {
        if (dataStore == null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
            params.put(PostgisNGDataStoreFactory.HOST.key, host);
            params.put(PostgisNGDataStoreFactory.PORT.key, new Integer(port));
            params.put(PostgisNGDataStoreFactory.DATABASE.key, dbname);
            params.put(PostgisNGDataStoreFactory.SCHEMA.key, schema);
            params.put(PostgisNGDataStoreFactory.USER.key, username);
            params.put(PostgisNGDataStoreFactory.PASSWD.key, password);
            try {
                dataStore = DataStoreFinder.getDataStore(params);
            } catch (IOException e) {
                logger.error("Postgis数据库连接失败", e);
            }
        }
        if (dataStore == null) {
            dataStore = getDefeaultDatastore();
        }
        return dataStore;
    }

    public void dispose() {
        dataStore.dispose();
        dataStore = null;
    }
}
