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
    List<TableNames> findTableNames(Map paramMap);

    /**
     * 分页查询用户
     * @return
     */
    List<TableNames> getPage();

    List<TableNames> getByIds(Long[] ids);

    List<TableNames> getTableNameByNameEn(@Param("nameEn") String nameEn);

    TableNames getByName(@Param("nameCn") String nameCn, @Param("nameEn") String nameEn);

    TableNames getByNameCn(@Param("nameCn") String nameCn);

    int insertTableNames(TableNames t);

    int delete(Long id);

    int updateTableNames(TableNames t);

    int dropTable(@Param("names")String names);

    /*int deleteTableNames(Long[] ids);
    List<String> findTableName();
    int updateState(Long[] ids);*/

}
