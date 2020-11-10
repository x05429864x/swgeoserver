package it.geosolutions.swgeoserver.dao;

import it.geosolutions.swgeoserver.entry.TablesEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UploadFileMapper {

    int checkTableName(String tableName);

    String executeBysql(@Param(value="sql") String sql);

    List<Map<String,String>> executeReMap(@Param(value="sql") String sql);

    int saveData(TablesEntity le);
}
