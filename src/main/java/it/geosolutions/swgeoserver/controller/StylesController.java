package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/styles")
@Api(tags = "样式接口",description = "样式接口")
public class StylesController extends BaseGeoserverREST {

    //Content-Type : application/vnd.ogc.sld+xml
    //http://192.168.8.229:8088/geoserver/rest/styles?name=touminggd
    @PostMapping(value = "/publishWorkStyles")
    @ApiOperation(value = "发布样式",notes = "样式")
    @Transactional
    public Object publishWorkStyles(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"nameCn\": \"中文名称\",\n" +
            "  \"nameEn\":\"英文名称\"\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> paramMap){
        File file = new File("book.xml");
        publisher.publishStyle(file,"a");
        return null;
    }
}
