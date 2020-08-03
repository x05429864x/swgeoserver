package it.geosolutions.swgeoserver.controller;

import it.geosolutions.swgeoserver.controller.base.BaseController;
import it.geosolutions.swgeoserver.service.PostgisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/pg")
public class PostgisController extends BaseController {

    @Autowired
    private PostgisService postgisService;

    @GetMapping(value = "/findId", produces = "application/json;charset=UTF-8")
    public List findId(@RequestParam(required = true) Map<String, String> paramMap){

        List list = new ArrayList();
        String dbName = paramMap.get("dbName");
        try {
            list = postgisService.findId(dbName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @GetMapping(value = "/findRoute", produces = "application/json;charset=UTF-8")
    public List findRoute(@RequestParam(required = true) Map<String, String> paramMap){

        List list = new ArrayList();
        String dbName = paramMap.get("dbName");
        String x1 = paramMap.get("x1");
        String y1 = paramMap.get("y1");
        String x2 = paramMap.get("x2");
        String y2 = paramMap.get("y2");
        HashMap<Object,Object> map = new HashMap<Object,Object>();

        map.put("dbName",dbName);
        map.put("x1",Double.parseDouble(x1));
        map.put("y1",Double.parseDouble(y1));
        map.put("x2",Double.parseDouble(x2));
        map.put("y2",Double.parseDouble(y2));
        try {
            list = postgisService.findRoute(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @PostMapping(value = "/insert", produces = "application/json;charset=UTF-8")
    public void insert(@RequestParam(required = true) Map<String, String> paramMap){

        List list = new ArrayList();
        String dbName = paramMap.get("dbName");
        String x1 = paramMap.get("x1");
        String y1 = paramMap.get("y1");
        String x2 = paramMap.get("x2");
        String y2 = paramMap.get("y2");
        HashMap<Object,Object> map = new HashMap<Object,Object>();

        map.put("dbName",dbName);
        map.put("x1",Double.parseDouble(x1));
        map.put("y1",Double.parseDouble(y1));
        map.put("x2",Double.parseDouble(x2));
        map.put("y2",Double.parseDouble(y2));
        try {
            list = postgisService.findRoute(map);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
