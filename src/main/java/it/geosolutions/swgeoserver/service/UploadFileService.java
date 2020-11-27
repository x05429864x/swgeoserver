package it.geosolutions.swgeoserver.service;

import it.geosolutions.swgeoserver.entry.TablesEntity;

import java.util.List;
import java.util.Map;

public interface UploadFileService {
    String uploadZip(String zipFileName, String zipFilePath, String suffix);

    int checkTableName(String tableName);

//    String CreateAttributeTable(String TableName, String KeyFieldName, List<String> TheFieldNames);

    List<Map<String, Object>> readShp2List(String shpPath, String tableName);

    int saveData(List<Map<String, Object>> list,String tableName);

    int truncateTable(String tableName);

}
