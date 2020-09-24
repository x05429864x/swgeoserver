package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.entry.Entity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/styles")
//@Api(tags = "样式接口",description = "样式接口")
public class StylesController extends BaseGeoserverREST {

    //Content-Type : application/vnd.ogc.sld+xml
    //http://192.168.8.229:8088/geoserver/rest/styles?name=touminggd
    public Object publishStyles(@ApiParam(name = "模型",value = "模型表名",required = true) @RequestBody Entity entity){
//        publisher.publishStyle()
        return null;
    }
}
