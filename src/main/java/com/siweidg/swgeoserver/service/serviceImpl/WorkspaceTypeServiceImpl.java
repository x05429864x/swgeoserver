package com.siweidg.swgeoserver.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.siweidg.swgeoserver.comm.utils.PageRequest;
import com.siweidg.swgeoserver.comm.utils.PageResult;
import com.siweidg.swgeoserver.comm.utils.PageUtils;
import com.siweidg.swgeoserver.dao.WorkspaceTypeMapper;
import com.siweidg.swgeoserver.entry.Style;
import com.siweidg.swgeoserver.entry.WorkspaceType;
import com.siweidg.swgeoserver.service.WorkspaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WorkspaceTypeServiceImpl implements WorkspaceTypeService {
    @Autowired
    private WorkspaceTypeMapper workspaceTypeMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return workspaceTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WorkspaceType record) {
        return workspaceTypeMapper.insert(record);
    }

    @Override
    public int insertSelective(WorkspaceType record) {
        return workspaceTypeMapper.insertSelective(record);
    }

    @Override
    public WorkspaceType selectByPrimaryKey(Long id) {
        return workspaceTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(WorkspaceType record) {
        return workspaceTypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WorkspaceType record) {
        return workspaceTypeMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest,getPageInfo(pageRequest));
    }

    @Override
    public int deleteByids(Long[] ids) {
        return workspaceTypeMapper.deleteByIds(ids);
    }

    @Override
    public WorkspaceType getByName(String nameCn,String nameEn) {
        return workspaceTypeMapper.getByName(nameCn,nameEn);
    }


    @Override
    public WorkspaceType getByParams(Map map) {
        return workspaceTypeMapper.getByParams(map);
    }

    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<WorkspaceType> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        String orderBy = pageRequest.getOrder();
        String sort = pageRequest.getSort();
        PageHelper.startPage(pageNum, pageSize,sort +" "+orderBy);
        List<WorkspaceType> workspaceTypes = workspaceTypeMapper.findAll(pageRequest.getParams());
        return new PageInfo<WorkspaceType>(workspaceTypes);
    }
}
