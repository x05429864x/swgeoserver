package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.entry.Entity;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.rest.HTTPUtils;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@CrossOrigin
@RestController
@RequestMapping("/wfs")
@Api(tags = "wfs接口",description = "wfs")
public class WfsController extends BaseGeoserverREST {

    //http://192.168.8.254:8080/geoserver/wfs?service=wfs&version=1.1.0&request=GetFeature&typeNames=workspaceTest:hotel_gis&outputFormat=application/json&srsname=EPSG:4326

    //http://192.168.8.254:8080/geoserver/rest/workspaces/wfs?service=wfs&version=1.1.0&request=GetFeature&typeNames=workspaceTest:hotel_gis&outputFormat=application/json&srsname=EPSG:4326

    @PostMapping(value = "/{layer}")
    @ApiOperation(value = "wfs服务",notes = "传入 工作区:图层 wfs服务")
//    @ApiImplicitParam(name="layer",value = "workspace:layer",required=true,paramType = "query",dataType="String")
    public Object wfs(@ApiParam(name = "layer",value = "例:workspace:layer",required = true) @PathVariable String layer) {
        String response = "";
        try {
            response = HTTPUtils.get(URL_ADD+"wfs?service=wfs&version=1.1.0&request=GetFeature&typeNames="+layer+"&outputFormat=application/json&srsname=EPSG:4326");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ReturnFormat.retParam(0,response);
    }
}
