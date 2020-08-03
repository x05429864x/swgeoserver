package it.geosolutions.swgeoserver.service;

import java.util.HashMap;
import java.util.List;

public interface PostgisService {

    List findId(String dbName);

    List findRoute(HashMap<Object,Object> map);
}
