package com.siweidg.swgeoserver.service;

import com.siweidg.swgeoserver.comm.utils.PageRequest;
import com.siweidg.swgeoserver.comm.utils.PageResult;
import com.siweidg.swgeoserver.entry.Style;
import com.siweidg.swgeoserver.entry.WorkspaceType;

import java.util.Map;

/**
 * \* User: x
 * \* Date: 2020/11/11
 * \* Time: 14:26
 * \* Description:
 * \
 */
public interface WorkspaceTypeService {

    int deleteByPrimaryKey(Long id);

    int insert(WorkspaceType record);

    int insertSelective(WorkspaceType record);

    WorkspaceType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkspaceType record);

    int updateByPrimaryKey(WorkspaceType record);

    WorkspaceType getByName(String nameCn, String nameEn);

    WorkspaceType getByParams(Map map);

    PageResult findPage(PageRequest pageRequest);

    int deleteByids(Long[] ids);
}
