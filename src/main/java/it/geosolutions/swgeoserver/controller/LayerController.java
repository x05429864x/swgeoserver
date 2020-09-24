package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.*;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.comm.init.Constants;
import it.geosolutions.swgeoserver.comm.utils.FileUtils;
import it.geosolutions.swgeoserver.comm.utils.PageData;
import it.geosolutions.swgeoserver.comm.utils.PropUtil;
import it.geosolutions.swgeoserver.comm.utils.SNUtil;
import it.geosolutions.swgeoserver.entry.Entity;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.rest.HTTPUtils;
import it.geosolutions.swgeoserver.rest.decoder.*;
import it.geosolutions.swgeoserver.rest.decoder.utils.NameLinkElem;
import it.geosolutions.swgeoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.swgeoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;


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

    @Override
    public PageData getPageData() {
        return super.getPageData();
    }

    @GetMapping(value = "/{workspace}/{layerName}")
    @ApiOperation(value = "查询图层",notes = "图层详情")
    public Object getLayer(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
                           @ApiParam(name = "layerName",value = "图层名称",required = true) @PathVariable String layerName){
        TableNames tableNames = tableNamesService.getTableNameByNameEn(layerName);
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
    }

    @GetMapping(value = "/layers")
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
                TableNames tableNames = tableNamesService.getTableNameByNameEn(layerName);
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
                TableNames tableNames = tableNamesService.getTableNameByNameEn(layerName);
                map.put("id",tableNames.getId());
                map.put("layerName_CN",tableNames.getNameCn());
                map.put("layerName_EN",workspace+":"+layerName);
                map.put("center",center);
//                map.put("crs",crs);
                allList.add(map);
            }
        }
        return ReturnFormat.retParam(0,allList);
    }


    /**
     * 发布图层PostGis
     * @param entity
     * @return
     */
    @PostMapping(value = "/publishGis")
    @ApiOperation(value = "发布PostGIS Database",notes = "查询列表")
    @Transactional
    public Object publishGis(@ApiParam(name = "模型",value = "模型表名",required = true) @RequestBody Entity entity){
        String ws = entity.getWorkSpace();
        String store_name =  entity.getDataStore();
        String layerName = entity.getLayer();
        String layerNameCn = entity.getLayerCn();
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
                boolean publish = manager.getPublisher().publishDBLayer(ws, store_name,  pds, layerEncoder);
                System.out.println("publish : " + publish);
                /*layer = reader.getLayer(ws, layerName);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTFeatureType featureType = reader.getFeatureType(layer);
                String crs = featureType.getCRS();
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
//            bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                StringBuffer center = new StringBuffer("");
                center.append((nativeBoundingBox.getMinX()+nativeBoundingBox.getMaxX())/2)
                        .append(",")
                        .append((nativeBoundingBox.getMinY()+nativeBoundingBox.getMaxY())/2);
                TableNames tableNames = tableNamesService.getTableNameByNameEn(layerName);
                tableNames.setCenter(center.toString());
                tableNames.setCreateTime(new Date());
                tableNames.setIsPublish(1l);
                tableNamesService.updateTableNames(tableNames);
                map.put("layerName_CN",layerNameCn);
                map.put("layerName_EN",ws+":"+featureType.getName());
                map.put("center",center.toString());
//            map.put("type","shp");
//            map.put("layerType","1");
                allList.add(map);*/
                return ReturnFormat.retParam(0,allList);
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
     * 发布图层MBTiles
     * @return
     */
    @RequestMapping(value = "/publishMBTiles", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ApiOperation(value = "发布MBTiles",notes = "发布MBTiles")
    @Transactional
    public Object publishMBTiles(@ApiParam(name = "模型",value = "模型表名",required = true) @RequestBody Entity entity){
        try{
//            String zipFileName = uploadFile.getOriginalFilename();
//            String store_name =  zipFileName;
//            String layerName = zipFileName;
//            String name = FileUtils.getFileNameNoEx(zipFileName);
//            String newPath = new File(uploadFilePath).getAbsolutePath() +"/" + zipFileName;
//            File file = new File(newPath);
//            //检测是否存在目录
//            if(!file.getParentFile().exists()){
//                file.getParentFile().mkdirs();
//            }
//            long startTime = System.currentTimeMillis();
//
//            uploadFile.transferTo(file);
//            long endTime = System.currentTimeMillis();
//            System.out.println("文件上传运行时间：" + (endTime - startTime) + "ms");
            String workspace = entity.getWorkSpace();
            String store_name = entity.getDataStore();
            String layerName = entity.getLayer();
            String path = entity.getPath();
            File file = new File(path);
            List allList = new ArrayList();
            //判断图层是否已经存在，不存在则创建并发布
            RESTLayer layer = manager.getReader().getLayer(workspace, layerName);
            if(layer == null){
                boolean publish = publisher.publishGeoMBTILES(workspace, store_name,store_name, file);
                System.out.println("publish : " + publish);
                /*layer = reader.getLayer(workspace, layerName);
                RESTCoverage coverage = reader.getCoverage(layer);
                Map map = new HashMap();
                RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                StringBuffer center = new StringBuffer("");
                center.append((nativeBoundingBox.getMinX()+nativeBoundingBox.getMaxX())/2)
                        .append(",")
                        .append((nativeBoundingBox.getMinY()+nativeBoundingBox.getMaxY())/2);
                TableNames tableNames = tableNamesService.getTableNameByNameEn(layerName);
                tableNames.setCenter(center.toString());
                tableNames.setCreateTime(new Date());
                tableNames.setIsPublish(1l);
                tableNamesService.updateTableNames(tableNames);
                map.put("layerName_CN",tableNames.getNameCn());
                map.put("layerName_EN",workspace+":"+layerName);
                map.put("center",center);
    //                map.put("crs",crs);
                allList.add(map);*/
                return ReturnFormat.retParam(0,allList);
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
     * 删除CoverageStore
     * @param entity
     * @return
     */
    @DeleteMapping(value = "/removeCoverageStore")
    @ApiOperation(value = "删除栅格图层",notes = "删除栅格图层")
    public Object removeLayer(@ApiParam(name = "模型",value = "模型表名",required = true) @RequestBody Entity entity){
        String ws = entity.getWorkSpace();
        String layername = entity.getLayer();
        publisher.removeCoverageStore(ws, layername,true);
        return ReturnFormat.retParam(0,null);
    }



    /**
     * layerType :0非查看属性，1查看属性，2编辑wfs，3wmts服务,4快视图
     * @param workspace
     * @param type
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/toList/{workspace},{type}")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public List toList(@ApiParam(name = "entity",value = "entity",required = true) @PathVariable String workspace,
                       @ApiParam(name = "type",value = "type",required = true) @PathVariable String type) {
        List dataStoreList = new ArrayList();
//        String type = entity.getType();
//        String workspace = entity.getWorkSpace();
        List allList = new ArrayList();
        if ("shp".equals(type)){
            RESTLayerGroupList groupList = reader.getLayerGroups(workspace);
            for (NameLinkElem nameLinkElem : groupList) {
                Map map = new HashMap();
                RESTLayerGroup group = reader.getLayerGroup(workspace,nameLinkElem.getName());
                StringBuffer bbox = new StringBuffer("");
                bbox.append(group.getMinX()+",").append(group.getMinY()+",").append(group.getMaxX()+",").append(group.getMaxY()+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey(nameLinkElem.getName()));
                map.put("layerName_EN",workspace+":"+nameLinkElem.getName());
                map.put("bbox",bbox);
                map.put("crs",group.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            RESTLayer hebeishanxi =reader.getLayer(workspace,"hebeishanxi");
            if(hebeishanxi!=null){
                RESTFeatureType featureType = reader.getFeatureType(hebeishanxi);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("hebeishanxi"));
                map.put("layerName_EN",workspace+":"+"hebeishanxi");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }else if("route".equals(type)){
            RESTLayer expressway_02 =reader.getLayer(workspace,"expressway_02");
            if(expressway_02!=null){
                RESTFeatureType featureType = reader.getFeatureType(expressway_02);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("expressway_02"));
                map.put("layerName_EN",workspace+":"+"expressway_02");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            RESTLayer yunnan =reader.getLayer(workspace,"yunnan");
            if(yunnan!=null){
                RESTFeatureType featureType = reader.getFeatureType(yunnan);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("yunnan"));
                map.put("layerName_EN",workspace+":"+"yunnan");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        } else if("draw".equals(type)){
            RESTFeatureTypeList ftList = reader.getFeatureTypes(workspace,"gis");
            for (NameLinkElem nameLinkElem : ftList ) {
                if(nameLinkElem.getName().contains("gis")){
                    RESTLayer layer = reader.getLayer(workspace,nameLinkElem.getName());
                    RESTFeatureType featureType = reader.getFeatureType(layer);
                    Map map = new HashMap();
                    StringBuffer bbox = new StringBuffer("");
                    bbox.append(featureType.getMinX()+",").append(featureType.getMinY()+",").append(featureType.getMaxX()+",").append(featureType.getMaxY()+"");
                    map.put("layerName_CN",workspace+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN",workspace+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",featureType.getCRS());
                    map.put("type","shp");
                    map.put("layerType","2");
                    dataStoreList.add(map);
                }
            }
            allList.addAll(dataStoreList);
        }else if("tif".equals(type)){
            List coverageStoreList = new ArrayList();
            for (NameLinkElem coverageStore : reader.getCoverageStores(workspace)) {
                String response = HTTPUtils.get(URL_ADD+"rest/workspaces/" +workspace+"/coveragestores/"+ coverageStore.getName()+"/coverages.xml", USER_NAME, PASSWORLD);
                RESTCoverageList restCoverageList = RESTCoverageList.build(response);
                for (NameLinkElem nameLinkElem : restCoverageList) {
                    Map map = new HashMap();
                    System.out.println(nameLinkElem.getName());
                    StringBuffer bbox = new StringBuffer("");
                    RESTCoverage coverage = reader.getCoverage(workspace,nameLinkElem.getName(),nameLinkElem.getName());
                    String crs = coverage.getCRS();
                    RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                    bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
                    map.put("layerName_CN",workspace+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN",workspace+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",crs);
                    map.put("type","tif");
                    map.put("layerType","1");
                    coverageStoreList.add(map);
                }
            }
            allList.addAll(coverageStoreList);
        }else if("boundary".equals(type)){
            RESTLayer guangdong =reader.getLayer(workspace,"gd_city");
            if(guangdong!=null){
                RESTFeatureType featureType = reader.getFeatureType(guangdong);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("gd_city"));
                map.put("layerName_EN",workspace+":"+"gd_city");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            /*RESTLayer china =reader.getLayer(workspace,"china");
            if(china!=null){
                RESTFeatureType featureType = reader.getFeatureType(china);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("china"));
                map.put("layerName_EN",workspace+":"+"china");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }*/
            RESTLayer china =reader.getLayer(workspace,"china");
            if(china!=null){
                RESTFeatureType featureType = reader.getFeatureType(china);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("china"));
                map.put("layerName_EN",workspace+":"+"china");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }else if("heatMap".equals(type)){
            RESTLayer guangdong =reader.getLayer(workspace,"gd_city");
            if(guangdong!=null){
                RESTFeatureType featureType = reader.getFeatureType(guangdong);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("gd_city"));
                map.put("layerName_EN",workspace+":"+"gd_city");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            RESTLayer china =reader.getLayer(workspace,"china");
            if(china!=null){
                RESTFeatureType featureType = reader.getFeatureType(china);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("china"));
                map.put("layerName_EN",workspace+":"+"china");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }else if("quickView".equals(type)){
            RESTLayer quickview =reader.getLayer(workspace,"test_quickview_attribute");
            if(quickview!=null){
                RESTFeatureType featureType = reader.getFeatureType(quickview);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("test_quickview_attribute"));
                map.put("layerName_EN",workspace+":"+"test_quickview_attribute");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","4");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);

        }else if("mapCompare".equals(type)){
            Map wordspaceMap = new HashMap();
            List coverageStoreList = new ArrayList();
            for (NameLinkElem coverageStore : reader.getCoverageStores("mapCompare")) {
//                wordspaceMap.put("workspaceName","mapCompare");
                String response = HTTPUtils.get(URL_ADD+"rest/workspaces/" +"mapCompare"+"/coveragestores/"+ coverageStore.getName()+"/coverages.xml", USER_NAME, PASSWORLD);
                RESTCoverageList restCoverageList = RESTCoverageList.build(response);
                for (NameLinkElem nameLinkElem : restCoverageList) {
                    Map map = new HashMap();
                    System.out.println(nameLinkElem.getName());
                    StringBuffer bbox = new StringBuffer("");
                    RESTCoverage coverage = reader.getCoverage("mapCompare",nameLinkElem.getName(),nameLinkElem.getName());
                    String crs = coverage.getCRS();
                    RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                    bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
                    map.put("layerName_CN","mapCompare"+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN","mapCompare"+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",crs);
                    map.put("type","tif");
                    map.put("layerType","1");
                    coverageStoreList.add(map);
                }
                wordspaceMap.put("mapCompare",coverageStoreList);
//                coverageStoreList.add(wordspaceMap);
            }
            List coverageStoreList1 = new ArrayList();
            for (NameLinkElem coverageStore : reader.getCoverageStores("workspaceTest")) {
//                wordspaceMap.put("workspaceName","mapCompare");
                String response = HTTPUtils.get(URL_ADD+"rest/workspaces/" +"workspaceTest"+"/coveragestores/"+ coverageStore.getName()+"/coverages.xml", USER_NAME, PASSWORLD);
                RESTCoverageList restCoverageList = RESTCoverageList.build(response);
                for (NameLinkElem nameLinkElem : restCoverageList) {
                    Map map = new HashMap();
                    System.out.println(nameLinkElem.getName());
                    StringBuffer bbox = new StringBuffer("");
                    RESTCoverage coverage = reader.getCoverage("workspaceTest",nameLinkElem.getName(),nameLinkElem.getName());
                    String crs = coverage.getCRS();
                    RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                    bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
                    map.put("layerName_CN","workspaceTest"+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN","workspaceTest"+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",crs);
                    map.put("type","tif");
                    map.put("layerType","1");
                    coverageStoreList1.add(map);
                }
                wordspaceMap.put("wordspaceTest",coverageStoreList1);
//                coverageStoreList.add(wordspaceMap);
            }
            allList.add(wordspaceMap);
        }else if ("wmts".equals(type)){
            RESTLayer waterways4326 =reader.getLayer(workspace,"waterways4326");
            if(waterways4326!=null){
                RESTFeatureType featureType = reader.getFeatureType(waterways4326);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("waterways4326"));
                map.put("layerName_EN",workspace+":"+"waterways4326");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","wmts");
                map.put("layerType","3");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }
        return allList;
    }



//    @PostMapping(value = "/uploadGroup")
//    public List uploadPublishGroup(@RequestParam(value="file") MultipartFile file, @RequestParam() Map<String, String> paramMap)  {
//        String defaultStyle = paramMap.get("defaultStyle")!=null?paramMap.get("defaultStyle"):"";//sld样式
//        String workspace = paramMap.get("workspace");
//        String storeName = paramMap.get("storeName");
//        List dataStoreList = new ArrayList();
//        List coverageStoreList = new ArrayList();
//        List allList = new ArrayList();
//        if(!reader.getWorkspaceNames().contains(workspace)) {
//            assertTrue(publisher.createWorkspace(workspace));
//        } else {
//            System.out.println("工作区："+workspace+",已存在！");
//        }
//        String fileName = file.getOriginalFilename();
//        String suffix = (fileName.substring(fileName.lastIndexOf(".") + 1)).equals("tif")?"tif":"shp";
//        String name = fileName.substring(0,fileName.lastIndexOf("."));
//        String filePath = "/usr/local/swgeoserver/upload/"+suffix+"/";
//        File dest = new File(new File(filePath).getAbsolutePath()+ "/" + fileName);
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//        try {
//            file.transferTo(dest);
//            if ("shp".equals(suffix)){
//                RESTLayer layer = reader.getLayer(workspace, name);
//                if(layer==null){
//                    publisher.publishShp(workspace, storeName, name,dest,"EPSG:4326",defaultStyle);
//                    layer = reader.getLayer(workspace, name);
//                }
//                Map map = new HashMap();
//                StringBuffer bbox = new StringBuffer("");
//                RESTFeatureType featureType = reader.getFeatureType(layer);
//                String crs = featureType.getCRS();
//                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
//                bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
//                map.put("layerName",workspace+":"+name);
//                map.put("bbox",bbox);
//                map.put("crs",crs);
//                map.put("type","shp");
//                dataStoreList.add(map);
//                allList.addAll(dataStoreList);
//            }else if("tif".equals(suffix)){
//                RESTCoverage coverage = reader.getCoverage(workspace,name,name);
//                if(coverage==null){
//                    publisher.publishGeoTIFF(workspace, name, dest);
//                    coverage = reader.getCoverage(workspace,name,name);
//                }
//                Map map = new HashMap();
//                System.out.println(name);
//                StringBuffer bbox = new StringBuffer("");
//                String crs = coverage.getCRS();
//                RESTBoundingBox nativeBoundingBox = coverage.getNativeBoundingBox();
//                bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
//                map.put("layerName",workspace+":"+name);
//                map.put("bbox",bbox);
//                map.put("crs",crs);
//                map.put("type","tif");
//                coverageStoreList.add(map);
//                allList.addAll(coverageStoreList);
//            }
//            System.out.println(allList);
//            return allList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return allList;
//        }
//    }

//    @PostMapping(value = "/upload")
//    public List uploadPublish(@RequestParam(value="file") MultipartFile file, @RequestParam() Map<String, String> paramMap)  {
//        String defaultStyle = paramMap.get("defaultStyle")!=null?paramMap.get("defaultStyle"):"";//sld样式
//        List dataStoreList = new ArrayList();
//        List coverageStoreList = new ArrayList();
//        List allList = new ArrayList();
//        if(!reader.getWorkspaceNames().contains(workspace)) {
//            assertTrue(publisher.createWorkspace(workspace));
//        } else {
//            System.out.println("工作区："+workspace+",已存在！");
//        }
//        String fileName = file.getOriginalFilename();
//        String suffix = (fileName.substring(fileName.lastIndexOf(".") + 1)).equals("tif")?"tif":"shp";
//        String name = fileName.substring(0,fileName.lastIndexOf("."));
//        String filePath = "/usr/local/swgeoserver/upload/"+suffix+"/";
//        File dest = new File(new File(filePath).getAbsolutePath()+ "/" + fileName);
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//
//        }
//        try {
//            file.transferTo(dest); // 保存文件
////            ZipFile zipFile = new ZipFile(dest.getAbsolutePath().toString());
////            for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();){
////                ZipEntry entry=e.nextElement();
////                System.out.println("文件名:"+entry.getName().substring(0,entry.getName().lastIndexOf(".")));
////            }
//            if ("shp".equals(suffix)){
////                RESTLayer layer;
//                RESTLayer layer = reader.getLayer(workspace, PropUtil.getProperty(name));
//                if(layer==null){
////                if("".equals(PropUtil.getProperty(name))){
////                    Map<String,String> m = new HashMap();
////                    m.put(name,name);
////                    PropUtil.setProperty(m);
////                    publisher.publishShp(workspace, PropUtil.getProperty(name), PropUtil.getProperty(name),dest,"EPSG:4326",defaultStyle);
//                    boolean published = publisher.publishShp(workspace, PropUtil.getProperty(name), new NameValuePair[]{
//                                    new NameValuePair("charset", "UTF-8")},PropUtil.getProperty(name), UploadMethod.FILE,
//                            dest.toURI(), "EPSG:4326",GSCoverageEncoderTest.WGS84_NEW,ProjectionPolicy.REPROJECT_TO_DECLARED,defaultStyle);
////                    publisher.publishShp(workspace, PropUtil.getProperty(name), PropUtil.getProperty(name),dest,"EPSG:4326",defaultStyle);
//
////                    boolean published = publisher.publishShp(DEFAULT_WS, storeName, new NameValuePair[]{new NameValuePair("charset", "UTF-8")},datasetName, UploadMethod.FILE, zipFile.toURI(), "EPSG:4326", GSCoverageEncoderTest.WGS84,ProjectionPolicy.REPROJECT_TO_DECLARED,"polygon");
//
//                    layer = reader.getLayer(workspace, PropUtil.getProperty(name));
//                }
//                layer = reader.getLayer(workspace, PropUtil.getProperty(name));
//                Map map = new HashMap();
//                StringBuffer bbox = new StringBuffer("");
//                RESTFeatureType featureType = reader.getFeatureType(layer);
//                String crs = featureType.getCRS();
//                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
//                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
//                map.put("layerName_CN",workspace+":"+name);
//                map.put("layerName_EN",workspace+":"+featureType.getName());
//                map.put("bbox",bbox);
//                map.put("crs",crs);
//                map.put("type","shp");
//                map.put("layerType","1");
//                dataStoreList.add(map);
//                allList.addAll(dataStoreList);
//            }else if("tif".equals(suffix)){
//                Map<String,String> m = new HashMap();
//                m.put(name,name);
//                PropUtil.setProperty(m);
//                RESTCoverage coverage;
//                coverage = reader.getCoverage(workspace,PropUtil.getProperty(name),PropUtil.getProperty(name));
//                if(coverage==null){
//                    publisher.publishGeoTIFF(workspace, PropUtil.getProperty(name), dest);
//                    coverage = reader.getCoverage(workspace,PropUtil.getProperty(name),PropUtil.getProperty(name));
//                }
//
//                Map map = new HashMap();
//                System.out.println(name);
//                StringBuffer bbox = new StringBuffer("");
//                String crs = coverage.getCRS();
//                RESTBoundingBox nativeBoundingBox = coverage.getNativeBoundingBox();
//                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
//                map.put("layerName_CN",workspace+":"+name);
//                map.put("layerName_EN",workspace+":"+coverage.getName());
//                map.put("bbox",bbox);
//                map.put("crs",crs);
//                map.put("type","tif");
//                map.put("layerType","1");
//                coverageStoreList.add(map);
//                allList.addAll(coverageStoreList);
//            }
//            System.out.println(allList);
//            return allList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return allList;
//        }
//    }


    /**
     * layerType :0非查看属性，1查看属性，2编辑wfs，3wmts服务,4快视图
     * @param entity
     * @return
     * @throws Exception
     */
//    @GetMapping(value = "/toList/{workspace},{type}")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
    /*public List toList(@ApiParam(name = "entity",value = "entity",required = true) @RequestBody Entity entity) {
        List dataStoreList = new ArrayList();
        String type = entity.getType();
        String workspace = entity.getWorkSpace();
        List allList = new ArrayList();
        if ("shp".equals(type)){
            RESTLayerGroupList groupList = reader.getLayerGroups(workspace);
            for (NameLinkElem nameLinkElem : groupList) {
                Map map = new HashMap();
                RESTLayerGroup group = reader.getLayerGroup(workspace,nameLinkElem.getName());
                StringBuffer bbox = new StringBuffer("");
                bbox.append(group.getMinX()+",").append(group.getMinY()+",").append(group.getMaxX()+",").append(group.getMaxY()+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey(nameLinkElem.getName()));
                map.put("layerName_EN",workspace+":"+nameLinkElem.getName());
                map.put("bbox",bbox);
                map.put("crs",group.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            RESTLayer hebeishanxi =reader.getLayer(workspace,"hebeishanxi");
            if(hebeishanxi!=null){
                RESTFeatureType featureType = reader.getFeatureType(hebeishanxi);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("hebeishanxi"));
                map.put("layerName_EN",workspace+":"+"hebeishanxi");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }else if("route".equals(type)){
            RESTLayer expressway_02 =reader.getLayer(workspace,"expressway_02");
            if(expressway_02!=null){
                RESTFeatureType featureType = reader.getFeatureType(expressway_02);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("expressway_02"));
                map.put("layerName_EN",workspace+":"+"expressway_02");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            RESTLayer yunnan =reader.getLayer(workspace,"yunnan");
            if(yunnan!=null){
                RESTFeatureType featureType = reader.getFeatureType(yunnan);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("yunnan"));
                map.put("layerName_EN",workspace+":"+"yunnan");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","shp");
                map.put("layerType","1");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        } else if("draw".equals(type)){
            RESTFeatureTypeList ftList = reader.getFeatureTypes(workspace,"gis");
            for (NameLinkElem nameLinkElem : ftList ) {
                if(nameLinkElem.getName().contains("gis")){
                    RESTLayer layer = reader.getLayer(workspace,nameLinkElem.getName());
                    RESTFeatureType featureType = reader.getFeatureType(layer);
                    Map map = new HashMap();
                    StringBuffer bbox = new StringBuffer("");
                    bbox.append(featureType.getMinX()+",").append(featureType.getMinY()+",").append(featureType.getMaxX()+",").append(featureType.getMaxY()+"");
                    map.put("layerName_CN",workspace+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN",workspace+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",featureType.getCRS());
                    map.put("type","shp");
                    map.put("layerType","2");
                    dataStoreList.add(map);
                }
            }
            allList.addAll(dataStoreList);
        }else if("tif".equals(type)){
            List coverageStoreList = new ArrayList();
            for (NameLinkElem coverageStore : reader.getCoverageStores(workspace)) {
                String response = HTTPUtils.get(URL_ADD+"rest/workspaces/" +workspace+"/coveragestores/"+ coverageStore.getName()+"/coverages.xml", USER_NAME, PASSWORLD);
                RESTCoverageList restCoverageList = RESTCoverageList.build(response);
                for (NameLinkElem nameLinkElem : restCoverageList) {
                    Map map = new HashMap();
                    System.out.println(nameLinkElem.getName());
                    StringBuffer bbox = new StringBuffer("");
                    RESTCoverage coverage = reader.getCoverage(workspace,nameLinkElem.getName(),nameLinkElem.getName());
                    String crs = coverage.getCRS();
                    RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                    bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
                    map.put("layerName_CN",workspace+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN",workspace+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",crs);
                    map.put("type","tif");
                    map.put("layerType","1");
                    coverageStoreList.add(map);
                }
            }
            allList.addAll(coverageStoreList);
        }else if("boundary".equals(type)){
            RESTLayer guangdong =reader.getLayer(workspace,"gd_city");
            if(guangdong!=null){
                RESTFeatureType featureType = reader.getFeatureType(guangdong);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("gd_city"));
                map.put("layerName_EN",workspace+":"+"gd_city");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            *//*RESTLayer china =reader.getLayer(workspace,"china");
            if(china!=null){
                RESTFeatureType featureType = reader.getFeatureType(china);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("china"));
                map.put("layerName_EN",workspace+":"+"china");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }*//*
            RESTLayer china =reader.getLayer(workspace,"china");
            if(china!=null){
                RESTFeatureType featureType = reader.getFeatureType(china);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("china"));
                map.put("layerName_EN",workspace+":"+"china");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }else if("heatMap".equals(type)){
            RESTLayer guangdong =reader.getLayer(workspace,"gd_city");
            if(guangdong!=null){
                RESTFeatureType featureType = reader.getFeatureType(guangdong);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("gd_city"));
                map.put("layerName_EN",workspace+":"+"gd_city");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            RESTLayer china =reader.getLayer(workspace,"china");
            if(china!=null){
                RESTFeatureType featureType = reader.getFeatureType(china);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("china"));
                map.put("layerName_EN",workspace+":"+"china");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","0");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }else if("quickView".equals(type)){
            RESTLayer quickview =reader.getLayer(workspace,"test_quickview_attribute");
            if(quickview!=null){
                RESTFeatureType featureType = reader.getFeatureType(quickview);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
                bbox.append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMinY())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxX())+",").append(SNUtil.NonScientificNotation(nativeBoundingBox.getMaxY())+"");
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("test_quickview_attribute"));
                map.put("layerName_EN",workspace+":"+"test_quickview_attribute");
                map.put("bbox",bbox);
                map.put("crs","EPSG:4326");
                map.put("type","shp");
                map.put("layerType","4");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);

        }else if("mapCompare".equals(type)){
            Map wordspaceMap = new HashMap();
            List coverageStoreList = new ArrayList();
            for (NameLinkElem coverageStore : reader.getCoverageStores("mapCompare")) {
//                wordspaceMap.put("workspaceName","mapCompare");
                String response = HTTPUtils.get(URL_ADD+"rest/workspaces/" +"mapCompare"+"/coveragestores/"+ coverageStore.getName()+"/coverages.xml", USER_NAME, PASSWORLD);
                RESTCoverageList restCoverageList = RESTCoverageList.build(response);
                for (NameLinkElem nameLinkElem : restCoverageList) {
                    Map map = new HashMap();
                    System.out.println(nameLinkElem.getName());
                    StringBuffer bbox = new StringBuffer("");
                    RESTCoverage coverage = reader.getCoverage("mapCompare",nameLinkElem.getName(),nameLinkElem.getName());
                    String crs = coverage.getCRS();
                    RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                    bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
                    map.put("layerName_CN","mapCompare"+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN","mapCompare"+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",crs);
                    map.put("type","tif");
                    map.put("layerType","1");
                    coverageStoreList.add(map);
                }
                wordspaceMap.put("mapCompare",coverageStoreList);
//                coverageStoreList.add(wordspaceMap);
            }
            List coverageStoreList1 = new ArrayList();
            for (NameLinkElem coverageStore : reader.getCoverageStores("workspaceTest")) {
//                wordspaceMap.put("workspaceName","mapCompare");
                String response = HTTPUtils.get(URL_ADD+"rest/workspaces/" +"workspaceTest"+"/coveragestores/"+ coverageStore.getName()+"/coverages.xml", USER_NAME, PASSWORLD);
                RESTCoverageList restCoverageList = RESTCoverageList.build(response);
                for (NameLinkElem nameLinkElem : restCoverageList) {
                    Map map = new HashMap();
                    System.out.println(nameLinkElem.getName());
                    StringBuffer bbox = new StringBuffer("");
                    RESTCoverage coverage = reader.getCoverage("workspaceTest",nameLinkElem.getName(),nameLinkElem.getName());
                    String crs = coverage.getCRS();
                    RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                    bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
                    map.put("layerName_CN","workspaceTest"+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN","workspaceTest"+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",crs);
                    map.put("type","tif");
                    map.put("layerType","1");
                    coverageStoreList1.add(map);
                }
                wordspaceMap.put("wordspaceTest",coverageStoreList1);
//                coverageStoreList.add(wordspaceMap);
            }
            allList.add(wordspaceMap);
        }else if ("wmts".equals(type)){
            RESTLayer waterways4326 =reader.getLayer(workspace,"waterways4326");
            if(waterways4326!=null){
                RESTFeatureType featureType = reader.getFeatureType(waterways4326);
                Map map = new HashMap();
                StringBuffer bbox = new StringBuffer("");
                bbox.append(featureType.getNativeBoundingBox().getMinX()+",").append(featureType.getNativeBoundingBox().getMinY()+",").append(featureType.getNativeBoundingBox().getMaxX()+",").append(featureType.getNativeBoundingBox().getMaxY()+"");

                StringBuffer center = new StringBuffer("");
                BigDecimal getMinX=new BigDecimal(featureType.getNativeBoundingBox().getMinX()+"");
                BigDecimal getMinY=new BigDecimal(featureType.getNativeBoundingBox().getMinY()+"");
                BigDecimal getMaxX=new BigDecimal(featureType.getNativeBoundingBox().getMaxX()+"");
                BigDecimal getMaxY=new BigDecimal(featureType.getNativeBoundingBox().getMaxY()+"");


                BigDecimal x = (getMinX.add(getMaxX)).divide(new BigDecimal(2));
                BigDecimal y = (getMinY.add(getMaxY)).divide(new BigDecimal(2));

                center.append(x+","+y);
                map.put("layerName_CN",workspace+":"+PropUtil.getKey("waterways4326"));
                map.put("layerName_EN",workspace+":"+"waterways4326");
                map.put("bbox",bbox);
                map.put("center",center);
                map.put("crs",featureType.getCRS());
                map.put("type","wmts");
                map.put("layerType","3");
                dataStoreList.add(map);
            }
            allList.addAll(dataStoreList);
        }
        return allList;
    }*/



    /*    @GetMapping(value = "/names/{workspace}/{datastore}")
    @ApiOperation(value = "根据datastore查询图层",notes = "根据datastore查询")
    public Object getLayerByDS(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
                           @ApiParam(name = "datastore",value = "数据存储名称",required = true) @PathVariable String datastore){
        List layerList = reader.getLayerNames(workspace,datastore);
        List allList = new ArrayList();
        for (int i=0;i<layerList.size();i++){
            Map map = new HashMap();
            String nameCn = tableNamesService.getNameCn(layerList.get(i).toString());
            map.put("layerName_CN",nameCn);
            map.put("layerName_EN",layerList.get(i).toString());
            allList.add(map);
        }
        return ReturnFormat.retParam(0,allList);
    }*/

    /*@PostMapping(value = "/tif")
    @ApiOperation(value = "tif查询",notes = "tif列表查询")
    public Object getTif(@ApiParam(name = "entity",value = "{workspace:workspaceName}",required = true) @RequestBody Entity entity){
        String workspace  = entity.getWorkSpace();
        List allList = new ArrayList();
        List coverageStoreList = new ArrayList();
        RESTCoverageStoreList coverageStores = reader.getCoverageStores(workspace);
        if(coverageStores!=null){
            for (NameLinkElem coverageStore : coverageStores) {
                String response = HTTPUtils.get(URL_ADD+"rest/workspaces/" +workspace+"/coveragestores/"+ coverageStore.getName()+"/coverages.xml", USER_NAME, PASSWORLD);
                RESTCoverageList restCoverageList = RESTCoverageList.build(response);
                for (NameLinkElem nameLinkElem : restCoverageList) {
                    Map map = new HashMap();
                    System.out.println(nameLinkElem.getName());
                    StringBuffer bbox = new StringBuffer("");
                    RESTCoverage coverage = reader.getCoverage(workspace,nameLinkElem.getName(),nameLinkElem.getName());
                    String crs = coverage.getCRS();
                    RESTBoundingBox nativeBoundingBox = coverage.getLatLonBoundingBox();
                    bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
                    map.put("layerName_CN",workspace+":"+PropUtil.getKey(nameLinkElem.getName()));
                    map.put("layerName_EN",workspace+":"+nameLinkElem.getName());
                    map.put("bbox",bbox);
                    map.put("crs",crs);
                    map.put("type","tif");
                    map.put("layerType","1");
                    coverageStoreList.add(map);
                }
            }
            allList.addAll(coverageStoreList);
            return ReturnFormat.retParam(0,allList);
        }else{
            return ReturnFormat.retParam(4003,null);
        }
    }*/

    /**
     * 发布图层GeoTiff
     * @return
     */
    /*@RequestMapping(value = "/publishTif/{workspace}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ApiOperation(value = "发布Tif",notes = "发布Tif")
    public Object publishTif(@ApiParam(name = "uploadFile",value = "上传文件",required = true) @RequestPart ( value="uploadFile", required = true) MultipartFile uploadFile,
                             @ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable (required = true) String workspace){
        try{
            String zipFileName = uploadFile.getOriginalFilename();
            String store_name =  zipFileName;
            String layerName = zipFileName;
            String name = FileUtils.getFileNameNoEx(zipFileName);
            String newPath = new File(uploadFilePath).getAbsolutePath() +"/" + zipFileName;
            File file = new File(newPath);
            //检测是否存在目录
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            long startTime = System.currentTimeMillis();

            uploadFile.transferTo(file);
            long endTime = System.currentTimeMillis();
            System.out.println("文件上传运行时间：" + (endTime - startTime) + "ms");
            List allList = new ArrayList();
            //判断图层是否已经存在，不存在则创建并发布
            RESTLayer layer = manager.getReader().getLayer(workspace, layerName);
            if(layer == null){
                publisher.publishGeoTIFF(workspace, store_name,store_name, file);
            }else {
                System.out.println("已经发布过图层:" + layerName);
                return ReturnFormat.retParam(4002,null);
            }
            return ReturnFormat.retParam(0,null);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnFormat.retParam(1000,null);
        }

    }*/


}
