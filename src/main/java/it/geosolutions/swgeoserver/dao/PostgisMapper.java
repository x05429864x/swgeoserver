package it.geosolutions.swgeoserver.dao;

import java.util.HashMap;
import java.util.List;

//@Mapper
public interface PostgisMapper {

    List findId(String dbName);

    List findRoute(HashMap<Object,Object> map);

}
