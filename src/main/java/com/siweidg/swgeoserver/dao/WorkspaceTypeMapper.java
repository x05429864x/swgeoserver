package com.siweidg.swgeoserver.dao;

import com.siweidg.swgeoserver.entry.WorkspaceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorkspaceTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WorkspaceType record);

    int insertSelective(WorkspaceType record);

    WorkspaceType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkspaceType record);

    int updateByPrimaryKey(WorkspaceType record);

    /**
     * 查询全部用户
     * @return
     */
    List<WorkspaceType> findAll(Map paramMap);

    WorkspaceType getByName(@Param("nameCn") String nameCn, @Param("nameEn") String nameEn);

    WorkspaceType getByParams(@Param("params") Map map);

    int deleteByIds(@Param("ids") Long[] ids);
}