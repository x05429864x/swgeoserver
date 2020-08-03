package it.geosolutions.swgeoserver.service.serviceImpl;

import it.geosolutions.swgeoserver.dao.PostgisMapper;
import it.geosolutions.swgeoserver.service.PostgisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class PostgisServiceImpl implements PostgisService {

    @Autowired
    private PostgisMapper postgisMapper;

    @Override
    public List findId(String dbName) {
        return postgisMapper.findId(dbName);
    }

    @Override
    public List findRoute(HashMap<Object,Object> map){
        return postgisMapper.findRoute(map);
    }
}