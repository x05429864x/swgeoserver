package it.geosolutions.swgeoserver.dao;


import it.geosolutions.swgeoserver.entry.TableNames;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TableNamesMapper {

    /**
     * 查询全部用户
     * @return
     */
    List<TableNames> getAll(Long state);

    /**
     * 分页查询用户
     * @return
     */
    List<TableNames> getPage();

//    List<TableNames> getAll(Long state);

    List<TableNames> getByIds(Long[] ids);

    List<TableNames> getTableNameByNameEn(@Param("nameEn") String nameEn);

    List<String> findTableName();

    TableNames getByName(@Param("nameCn") String nameCn, @Param("nameEn") String nameEn);

    int insertTableNames(TableNames t);

    int deleteTableNames(Long[] ids);

    int updateTableNames(TableNames t);

    int updateState(Long[] ids);

    int dropTable(@Param("names")String names);

}
