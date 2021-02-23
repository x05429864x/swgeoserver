package com.siweidg.swgeoserver.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.siweidg.swgeoserver.comm.utils.PageRequest;
import com.siweidg.swgeoserver.comm.utils.PageResult;
import com.siweidg.swgeoserver.comm.utils.PageUtils;
import com.siweidg.swgeoserver.dao.StyleTypeMapper;
import com.siweidg.swgeoserver.entry.Style;
import com.siweidg.swgeoserver.entry.StyleType;
import com.siweidg.swgeoserver.service.StyleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StyleTypeServiceImpl implements StyleTypeService {
    @Autowired
    private StyleTypeMapper styleTypeMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return styleTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(StyleType record) {
        return styleTypeMapper.insert(record);
    }

    @Override
    public int insertSelective(StyleType record) {
        return styleTypeMapper.insertSelective(record);
    }

    @Override
    public StyleType selectByPrimaryKey(Long id) {
        return styleTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(StyleType record) {
        return styleTypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(StyleType record) {
        return styleTypeMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest,getPageInfo(pageRequest));
    }

    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<StyleType> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        String orderBy = pageRequest.getOrder();
        String sort = pageRequest.getSort();
        PageHelper.startPage(pageNum, pageSize,sort +" "+orderBy);
        List<StyleType> styles = styleTypeMapper.findAll(pageRequest.getParams());
        return new PageInfo<StyleType>(styles);
    }

}
