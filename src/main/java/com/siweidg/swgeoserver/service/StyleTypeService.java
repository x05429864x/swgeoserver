package com.siweidg.swgeoserver.service;

import com.siweidg.swgeoserver.comm.utils.PageRequest;
import com.siweidg.swgeoserver.comm.utils.PageResult;
import com.siweidg.swgeoserver.entry.StyleType;

/**
 * \* User: x
 * \* Date: 2020/11/11
 * \* Time: 14:26
 * \* Description:
 * \
 */
public interface StyleTypeService {

    int deleteByPrimaryKey(Long id);

    int insert(StyleType record);

    int insertSelective(StyleType record);

    StyleType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StyleType record);

    int updateByPrimaryKey(StyleType record);

    PageResult findPage(PageRequest pageRequest);
}
