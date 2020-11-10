package it.geosolutions.swgeoserver.service;


import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.comm.utils.PageResult;
import it.geosolutions.swgeoserver.entry.TableNames;

import java.util.List;
import java.util.Map;

public interface TableNamesService {

    List<TableNames> findTableNames(Map<String ,Object> paramMap);

    /**
     * 分页查询接口
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPage(PageRequest pageRequest);

    List<String> findTableName();

    List<TableNames> getByIds(Long[] ids);

    TableNames getTableNameByNameEn(String nameEn);

    TableNames getByName(String nameCn,String nameEn);

    TableNames getByNameCn(String nameCn);

    int insertTableNames(TableNames tableNames);

    int deleteTableNames(Long[] ids);

    int updateTableNames(TableNames tableNames);

    int updateState(Long[] ids);

    int dropTable(String names);

//    TableNames getByEn(String nameEn);

}
