package com.siweidg.swgeoserver.dao;

import com.siweidg.swgeoserver.entry.Style;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StyleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Style record);

    int insertSelective(Style record);

    Style selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Style record);

    int updateByPrimaryKey(Style record);

    /**
     * 查询全部用户
     * @return
     */
    List<Style> findAll(Map paramMap);

    Style getByName(@Param("nameCn") String nameCn, @Param("nameEn") String nameEn);

    Style getByParams(@Param("params") Map map);

    String getMaxNumber();

}