package com.siweidg.swgeoserver.service;

import com.siweidg.swgeoserver.comm.utils.PageRequest;
import com.siweidg.swgeoserver.comm.utils.PageResult;
import com.siweidg.swgeoserver.entry.Style;

import java.util.Map;

/**
 * \* User: x
 * \* Date: 2020/11/11
 * \* Time: 14:26
 * \* Description:
 * \
 */
public interface StyleService {

    int deleteByPrimaryKey(Long id);

    int insert(Style record);

    int insertSelective(Style record);

    Style selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Style record);

    int updateByPrimaryKey(Style record);

    Style getByName(String nameCn,String nameEn);

    Style getByParams(Map map);

    String getNameEn();

    String getMaxNumber();

    PageResult findPage(PageRequest pageRequest);
}
