package it.geosolutions.swgeoserver.service;

import it.geosolutions.swgeoserver.entry.StyleType;

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
}
