package com.siweidg.swgeoserver.dao;


import com.siweidg.swgeoserver.entry.TableNames;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TableNamesMapper {

    /**
     * 分页查询
     * @return
     */
    List<TableNames> findTableNames(Map paramMap);

    List<TableNames> getByIds(Long[] ids);

    List<TableNames> getTableNameByNameEn(@Param("nameEn") String nameEn);

    TableNames getByName(@Param("nameCn") String nameCn, @Param("nameEn") String nameEn);

    TableNames getByNameCn(@Param("nameCn") String nameCn);

    int insertTableNames(TableNames t);

    int delete(Long id);

    int updateTableNames(TableNames t);

    int dropTable(@Param("names")String names);

    String getExtent(@Param("taskId")int taskId);
    /*int deleteTableNames(Long[] ids);
    List<String> findTableName();
    int updateState(Long[] ids);*/

}
