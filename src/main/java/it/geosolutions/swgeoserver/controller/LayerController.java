package it.geosolutions.swgeoserver.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.rest.decoder.RESTBoundingBox;
import it.geosolutions.swgeoserver.rest.decoder.RESTCoverage;
import it.geosolutions.swgeoserver.rest.decoder.RESTFeatureType;
import it.geosolutions.swgeoserver.rest.decoder.RESTLayer;
import it.geosolutions.swgeoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.swgeoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/layer")
@Api(tags = "图层接口",description = "图层")
public class LayerController extends BaseGeoserverREST {

    @Value("${upload_file_path}")
    private String uploadFilePath;

    @Value("${upload_extract_path}")
    private String extractFilePath;

    @Autowired
    private TableNamesService tableNamesService;



/*@GetMapping(value = "/{workspace}/{layerName}")
    @ApiOperation(value = "查询图层",notes = "图层详情")
    public Object getLayer(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
                           @ApiParam(name = "layerName",value = "图层名称",required = true) @PathVariable String layerName){
        TableNames tableNames = tableNamesService.getByNameEn(layerName);
        if("".equals(tableNames.getNameCn())){
            return ReturnFormat.retParam(4003,null);
        }
        RESTLayer layer = reader.getLayer(workspace,layerName);
        if(layer!=null){
            RESTFeatureType featureType = reader.getFeatureType(layer);
            List allList = new ArrayList();
            Map map = new HashMap();
            StringBuffer bbox = new StringBuffer("");
            String crs = featureType.getCRS();
            RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
            bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
            map.put("layerName_CN",workspace+":"+tableNames.getNameCn());
            map.put("layerName_EN",workspace+":"+layerName);
            map.put("bbox",bbox);
            map.put("crs",crs);
            map.put("type","shp");
            map.put("layerType","1");
            allList.add(map);
            return ReturnFormat.retParam(0,allList);
        }else{
            return ReturnFormat.retParam(4003,null);
        }
    }*//*


   */
/* @GetMapping(value = "/layers")
    @ApiOperation(value = "图层列表",notes = "图层列表")
    public Object getLayers(){
        List allList = new ArrayList();
        RESTLayerList layers = reader.getLayers();
        for (NameLinkElem elem : layers) {
            String workspace = elem.getName().split(":")[0];
            String layerName = elem.getName().split(":")[1];
            RESTLayer layer = reader.getLayer(workspace,layerName);
            String resourceClass = layer.getResourceClass();
            if("featureType".equals(resourceClass)){
                RESTFeatureType featureType = reader.getFeatureType(layer);
                Map map = new HashMap();
                String crs = featureType.getCRS();
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                StringBuffer center = new StringBuffer("");
                center.append((nativeBoundingBox.getMinX()+nativeBoundingBox.getMaxX())/2)
                        .append(",")
                        .append((nativeBoundingBox.getMinY()+nativeBoundingBox.getMaxY())/2);
                TableNames tableNames = tableNamesService.getByNameEn(layerName);
                if(tableNames!=null){
                    map.put("id",tableNames.getId());
                    map.put("layerName_CN",tableNames.getNameCn());
                    map.put("layerName_EN",workspace+":"+layerName);
                    map.put("center",center);
                    allList.add(map);
                }
            }else{
                RESTCoverage coverage = reader.getCoverage(layer);
                Map map = new HashMap();
                RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                StringBuffer center = new StringBuffer("");
                center.append((nativeBoundingBox.getMinX()+nativeBoundingBox.getMaxX())/2)
                        .append(",")
                        .append((nativeBoundingBox.getMinY()+nativeBoundingBox.getMaxY())/2);
                TableNames tableNames = tableNamesService.getByNameEn(layerName);
                map.put("id",tableNames.getId());
                map.put("layerName_CN",tableNames.getNameCn());
                map.put("layerName_EN",workspace+":"+layerName);
                map.put("center",center);
//                map.put("crs",crs);
                allList.add(map);
            }
        }
        return ReturnFormat.retParam(0,allList);
    }*//*



    */
/**
     * 发布图层PostGis
     * @param paramMap
     * @return
     */

    @PostMapping(value = "/publishGis")
    @ApiOperation(value = "发布PostGIS Database",notes = "查询列表")
    @Transactional
    public Object publishGis(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"nameCn\": \"中文名称\",\n" +
            "  \"nameEn\":\"英文名称\"\n" +
            "  \"style\":\"样式\"\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> paramMap){
        String ws = paramMap.get("workspace");
        String style = paramMap.get("style");
        String store_name =  "gis";
        String layerName = paramMap.get("nameEn");
        String layerNameCn = paramMap.get("nameCn");
        List allList = new ArrayList();
        //判断图层是否已经存在，不存在则创建并发布
        try{
            RESTLayer layer = manager.getReader().getLayer(ws, layerName);
            if(layer == null){
                GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
                pds.setTitle(layerName);
                pds.setName(layerName);
                pds.setSRS("EPSG:4326");
                GSLayerEncoder layerEncoder = new GSLayerEncoder();
                if(!"".equalsIgnoreCase(style)){
                    layerEncoder.setDefaultStyle(style);
                }
                boolean publish = manager.getPublisher().publishDBLayer(ws, store_name,  pds, layerEncoder);
                System.out.println("publish : " + publish);
                layer = reader.getLayer(ws, layerName);
                RESTFeatureType featureType = reader.getFeatureType(layer);
                String crs = featureType.getCRS();

                StringBuffer center = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getLatLonBoundingBox();
                BigDecimal getMinX=new BigDecimal(nativeBoundingBox.getMinX()+"");
                BigDecimal getMinY=new BigDecimal(nativeBoundingBox.getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(nativeBoundingBox.getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(nativeBoundingBox.getMaxY()+"");
                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));
                center.append(x+","+y);

                StringBuffer bbox = new StringBuffer("");
                bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");

                TableNames tableNames = tableNamesService.getByNameEn(layerName);
                Object metadata = tableNames.getMetadata();
                JSONObject jsonObject = JSONObject.parseObject(metadata.toString());
                jsonObject.put("center",center);
                jsonObject.put("bounds",bbox);
//                String json = JSON.toJSONString(map);
                tableNames.setState(1l);
                tableNames.setCreateTime(new Date());
                tableNames.setMetadata(jsonObject);
                tableNamesService.updateTableNames(tableNames);
                return ReturnFormat.retParam(0,tableNames);
            }else {
                System.out.println("已经发布过图层:" + layerName);
                return ReturnFormat.retParam(4002,null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ReturnFormat.retParam(1000,allList);
        }
    }


/**
     * 变更图层
     * @param paramMap
     * @return
     */

    @PostMapping(value = "/updateLayer")
    @ApiOperation(value = "变更图层",notes = "变更图层")
    @Transactional
    public Object updateLayer(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"nameCn\": \"中文名称\",\n" +
            "  \"nameEn\":\"英文名称\"\n" +
            "  \"style\":\"样式\"\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> paramMap){
        String ws = paramMap.get("workspace");
        String style = paramMap.get("style");
//        String store_name =  "gis";
        String layerName = paramMap.get("nameEn");
        String layerNameCn = paramMap.get("nameCn");
        List allList = new ArrayList();
        //判断图层是否已经存在，不存在则创建并发布
        try{
            RESTLayer layer = manager.getReader().getLayer(ws, layerName);
            if(layer == null){
                System.out.println("图层不存在:" + layerName);
                return ReturnFormat.retParam(4003,null);
            }else {
                TableNames t = tableNamesService.getByNameCn(layerNameCn);
                if(t!=null){
                    return ReturnFormat.retParam(2032,t);
                }
                GSLayerEncoder layerEncoder = new GSLayerEncoder();
                if(null != style){
                    layerEncoder.setDefaultStyle(style);
                }
                boolean publish = manager.getPublisher().configureLayer(ws, layerName, layerEncoder);
                System.out.println("publish : " + publish);
                layer = reader.getLayer(ws, layerName);
                RESTFeatureType featureType = reader.getFeatureType(layer);
                String crs = featureType.getCRS();

                StringBuffer center = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getLatLonBoundingBox();
                BigDecimal getMinX=new BigDecimal(nativeBoundingBox.getMinX()+"");
                BigDecimal getMinY=new BigDecimal(nativeBoundingBox.getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(nativeBoundingBox.getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(nativeBoundingBox.getMaxY()+"");
                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));
                center.append(x+","+y);

                StringBuffer bbox = new StringBuffer("");
                bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");

                TableNames tableNames = tableNamesService.getByNameEn(layerName);
                Object metadata = tableNames.getMetadata();
                JSONObject jsonObject = JSONObject.parseObject(metadata.toString());
//                Map map = JSON.parseObject(metadata,Map.class);
                jsonObject.put("center",center);
                jsonObject.put("bounds",bbox);
//                String json = JSON.toJSONString(map);
                tableNames.setState(1l);
                tableNames.setNameCn(layerNameCn);
                tableNames.setUpdateTime(new Date());
                tableNames.setMetadata(jsonObject);
                tableNamesService.updateTableNames(tableNames);
                return ReturnFormat.retParam(0,tableNames);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ReturnFormat.retParam(1000,allList);
        }
    }


/**
     * 发布图层MBTiles
     * @return
     */

    @RequestMapping(value = "/publishMBTiles", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ApiOperation(value = "发布MBTiles",notes = "发布MBTiles")
    @Transactional
    public Object publishMBTiles(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"datastore\":\"数据存储\",\n" +
            "  \"nameEn\":\"英文名称\",\n" +
            "  \"path\":\"路径\"\n}"
            ,required = true)@RequestBody(required = true) Map<String, String> paramMap){
        try{
            String workspace = paramMap.get("workspace");
            String store_name = paramMap.get("datastore");
            String layerName = paramMap.get("nameEn");
            String path = paramMap.get("path");
            File file = new File(extractFilePath+path);
            //判断图层是否已经存在，不存在则创建并发布
            RESTLayer layer = manager.getReader().getLayer(workspace, layerName);
            if(layer == null){
                boolean publish = publisher.publishGeoMBTILES(workspace, store_name,store_name, file);
                logger.info("mbtile发布"+publish);
                TableNames tableNames = tableNamesService.getByNameEn(layerName);
                tableNames.setState(1l);
                tableNames.setCreateTime(new Date());
                tableNamesService.updateTableNames(tableNames);
                return ReturnFormat.retParam(0,tableNames);
            }else {
                System.out.println("已经发布过图层:" + layerName);
                return ReturnFormat.retParam(4002,null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ReturnFormat.retParam(1000,null);
        }

    }


/**
     * 发布图层Tif
     * @return
     */

    @RequestMapping(value = "/publishTif", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ApiOperation(value = "发布Tif",notes = "发布Tif")
    @Transactional
    public Object publishTif(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"datastore\":\"数据存储\"\n" +
            "  \"nameEn\":\"英文名称\"\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> paramMap){
        try{
            String workspace = paramMap.get("workspace");
            String store_name = paramMap.get("datastore");
            String layerName = paramMap.get("nameEn");
            String path = paramMap.get("path");
            File file = new File(extractFilePath+path);
            //判断图层是否已经存在，不存在则创建并发布
            RESTLayer layer = manager.getReader().getLayer(workspace, layerName);
            if(layer == null){
                boolean publish = publisher.publishGeoTIFF(workspace, store_name,store_name, file);
                logger.info("发布图层layerName---"+layerName+":"+publish);

                RESTCoverage coverage = reader.getCoverage(workspace,store_name,layerName);
                StringBuffer center = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                BigDecimal getMinX=new BigDecimal(nativeBoundingBox.getMinX()+"");
                BigDecimal getMinY=new BigDecimal(nativeBoundingBox.getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(nativeBoundingBox.getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(nativeBoundingBox.getMaxY()+"");
                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));
                center.append(x+","+y);

                StringBuffer bbox = new StringBuffer("");
                bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");

                TableNames tableNames = tableNamesService.getByNameEn(layerName);
                Object metadata = tableNames.getMetadata();
                JSONObject jsonObject = JSONObject.parseObject(metadata.toString());
//                Map map = JSON.parseObject(metadata,Map.class);
                jsonObject.put("center",center);
                jsonObject.put("bounds",bbox);
//                String json = JSON.toJSONString(map);
                tableNames.setState(1l);
                tableNames.setCreateTime(new Date());
                tableNames.setMetadata(jsonObject);
                tableNamesService.updateTableNames(tableNames);
                return ReturnFormat.retParam(0,tableNames);
            }else {
                logger.info("已经发布过图层:" + layerName);
                return ReturnFormat.retParam(4002,null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ReturnFormat.retParam(1000,null);
        }

    }

}

