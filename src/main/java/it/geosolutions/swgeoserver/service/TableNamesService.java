package it.geosolutions.swgeoserver.service;


import it.geosolutions.swgeoserver.entry.TableNames;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TableNamesService {

    List<TableNames> getAll(Long state);

    List<String> findTableName();

    List<TableNames> getByIds(Long[] ids);

    String getNameCn(String nameEn);

    TableNames getByName(String nameCn,String nameEn);

    int insertTableNames(TableNames tableNames);

    int deleteTableNames(Long[] ids);

    int updateTableNames(TableNames tableNames);

    int updateState(Long[] ids);

    int dropTable(String names);

//    TableNames getByEn(String nameEn);

}
