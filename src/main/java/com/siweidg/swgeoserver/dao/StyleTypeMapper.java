package com.siweidg.swgeoserver.dao;

import com.siweidg.swgeoserver.entry.Style;
import com.siweidg.swgeoserver.entry.StyleType;

import java.util.List;
import java.util.Map;

public interface StyleTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(StyleType record);

    int insertSelective(StyleType record);

    StyleType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StyleType record);

    int updateByPrimaryKey(StyleType record);

    List<StyleType> findAll(Map paramMap);
}