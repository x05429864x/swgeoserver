package com.siweidg.swgeoserver.service;

import java.util.List;
import java.util.Map;

public interface UploadFileService {
    String uploadZip(String zipFileName, String zipFilePath, String suffix);

    int checkTableName(String tableName);

//    String CreateAttributeTable(String TableName, String KeyFieldName, List<String> TheFieldNames);

    List<Map<Object, Object>> readShp2List(String shpPath, String tableName);

    int saveData(List<Map<Object, Object>> list,String tableName);

    int truncateTable(String tableName);

}
