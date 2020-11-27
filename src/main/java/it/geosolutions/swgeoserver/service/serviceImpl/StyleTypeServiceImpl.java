package it.geosolutions.swgeoserver.service.serviceImpl;

import it.geosolutions.swgeoserver.dao.StyleTypeMapper;
import it.geosolutions.swgeoserver.entry.StyleType;
import it.geosolutions.swgeoserver.service.StyleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
