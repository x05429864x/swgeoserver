package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.rest.HTTPUtils;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@CrossOrigin
@RestController
@RequestMapping("/wms")
@Api(tags = "wms接口",description = "wms")
public class WmsController extends BaseGeoserverREST {


    //http://192.168.8.254:8080/geoserver/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true
    // &LAYERS=workspaceTest%3Atest_quickview_attribute&exceptions=application%2Fvnd.ogc.se_inimage
    // &WIDTH=256&HEIGHT=256&SRS=EPSG%3A4326&STYLES=&BBOX=112.5%2C33.75%2C118.125%2C39.375

    @PostMapping(value = "/{layer}")
    @ApiOperation(value = "wms服务",notes = "传入 工作区:图层 wms服务")
//    @ApiImplicitParam(name="layer",value = "workspace:layer",required=true,paramType = "query",dataType="String")
    public Object wfs(@ApiParam(name = "layer",value = "例:workspace:layer",required = true) @PathVariable String layer) {
        String response = "";
        try {
            response = HTTPUtils.get(URL_ADD+"wms?service=wfs&version=1.1.0&request=GetFeature&typeNames="+layer+"&outputFormat=application/json&srsname=EPSG:4326");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ReturnFormat.retParam(0,response);
    }
}
