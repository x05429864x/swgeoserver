package it.geosolutions.swgeoserver.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.comm.init.Constants;
import it.geosolutions.swgeoserver.comm.utils.FileUtils;
import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.controller.base.BaseController;
import it.geosolutions.swgeoserver.entry.Style;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.rest.decoder.*;
import it.geosolutions.swgeoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.swgeoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import it.geosolutions.swgeoserver.service.StyleService;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

//实现跨域注解
//origin="*"代表所有域名都可访问
//maxAge飞行前响应的缓存持续时间的最大年龄，简单来说就是Cookie的有效期 单位为秒
//若maxAge是负数,则代表为临时Cookie,不会被持久化,Cookie信息保存在浏览器内存中,浏览器关闭Cookie就消失
//@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/layer")
@Api(tags = "图层接口",description = "图层")
public class TableNamesController extends BaseController {

    @Value("${upload_file_path}")
    private String uploadFilePath;

    @Value("${upload_extract_path}")
    private String extractFilePath;
    @Autowired
    private TableNamesService tableNamesService;

    @Autowired
    private StyleService StyleService;

    /*@PostMapping("/findTableNames")
    @ApiOperation(value = "图层查询接口", notes = "图层查询接口")
    public Object findTableNames(@ApiParam(name = "paramMap",value = "{\n" +
                                             "  \"state\": \"状态: 0正常,1作废\",\n" +
                                             "  \"isPublish\":\"0:未发布,1:已发布\"\n" +
                                             "}")@RequestBody Map<String ,Object> paramMap){
        List<TableNames> list = tableNamesService.findTableNames(paramMap);
        return ReturnFormat.retParam(0,list);
    }*/

    /**
     * 发布图层PostGis
     * @param param
     * @return
     */
    @PostMapping(value = "/publishGis")
    @ApiOperation(value = "发布PostGIS Database",notes = "查询列表")
    public Object publishGis(@ApiParam(name = "param",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"dataStore\": \"数据存储\",\n" +
            "  \"nameEn\":\"英文名称\"\n" +
            "  \"styleId\":\"样式ID\"\n" +
            "  \"style\":\"样式\"\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> param){
        String ws = param.get("workspace");
        String style = param.get("style");
        String dataStore =  param.get("dataStore");
        String layerName = param.get("nameEn");
        String styleId = param.get("styleId");
        List allList = new ArrayList();
        //判断图层是否已经存在，不存在则创建并发布
        try{
            RESTLayer layer = BaseGeoserverREST.manager.getReader().getLayer(ws, layerName);
            if(layer == null){
                GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
                pds.setTitle(layerName);
                pds.setName(layerName);
                pds.setSRS("EPSG:4326");

                GSLayerEncoder layerEncoder = new GSLayerEncoder();
                if(style != null){
                    layerEncoder.setDefaultStyle(style);
                }

                RESTDataStore restStore = BaseGeoserverREST.manager.getReader().getDatastore(ws, dataStore);
                if(restStore==null){
                    it.geosolutions.swgeoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder store = new it.geosolutions.swgeoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder(dataStore);
                    store.setHost(Constants.PG_HOST);//设置url
                    store.setPort(Constants.PG_PROT);//设置端口
                    store.setUser(Constants.PG_USERNAME);// 数据库的用户名
                    store.setPassword(Constants.PG_PWD);// 数据库的密码
                    store.setDatabase(Constants.PG_DB);// 那个数据库;
                    store.setSchema("public"); //当前先默认使用public这个schema
                    store.setConnectionTimeout(20);// 超时设置
                    //store.setName(schema);
                    store.setMaxConnections(20); // 最大连接数
                    store.setMinConnections(1);     // 最小连接数
                    store.setExposePrimaryKeys(true);

                    boolean createStore = BaseGeoserverREST.manager.getStoreManager().create(ws, store);
                    System.out.println("create store : " + createStore);
                } else {
                    System.out.println("数据存储已经存在了,store:" + dataStore);
                }

                boolean publish = BaseGeoserverREST.manager.getPublisher().publishDBLayer(ws, dataStore,  pds, layerEncoder);
                System.out.println("publish : " + publish);
                if(publish){
                    layer = BaseGeoserverREST.reader.getLayer(ws, layerName);
                    RESTFeatureType featureType = BaseGeoserverREST.reader.getFeatureType(layer);
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
                    tableNames.setStyleId(Long.parseLong(styleId));
                    tableNamesService.updateTableNames(tableNames);
                    return ReturnFormat.retParam(0,tableNames);
                }
                return ReturnFormat.retParam(1000,null);
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
     * 变更图层
     * @param updateParams
     * @return
     */
    @PostMapping(value = "/updateLayer")
    @ApiOperation(value = "变更图层",notes = "变更图层")
    public Object updateLayer(@ApiParam(name = "updateParams",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"nameCn\": \"中文名称\",\n" +
            "  \"nameEn\":\"英文名称\"\n" +
            "  \"style\":\"样式\",\n" +
            "  \"styleId\":\"样式id\"\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> updateParams){
        String ws = updateParams.get("workspace");
        String style = updateParams.get("style");
        String styleId =  updateParams.get("styleId");
        String layerName = updateParams.get("nameEn");
        String layerNameCn = updateParams.get("nameCn");
//        List allList = new ArrayList();
        //判断图层是否已经存在，不存在则创建并发布

        try{
            RESTLayer layer = BaseGeoserverREST.manager.getReader().getLayer(ws, layerName);
            if(layer == null){
                System.out.println("图层不存在:" + layerName);
                return ReturnFormat.retParam(4003,null);
            }else {
                if(layerNameCn!=null){
                    TableNames t = tableNamesService.getByNameCn(layerNameCn);
                    if(t!=null){
                        return ReturnFormat.retParam(2032,t);
                    }
                }
                TableNames tableNames = tableNamesService.getByNameEn(layerName);
                GSLayerEncoder layerEncoder = new GSLayerEncoder();
                if(null != style){
                    layerEncoder.setDefaultStyle(style);
                    boolean publish = BaseGeoserverREST.manager.getPublisher().configureLayer(ws, layerName, layerEncoder);
                    if(styleId!=null){
                        tableNames.setStyleId(Long.parseLong(styleId));
                    }
                    System.out.println("publish : " + publish);
                }else{
                    boolean publish = BaseGeoserverREST.manager.getPublisher().configureLayer(ws, layerName, layerEncoder);
                    logger.info("===============================：  "+publish);
                }


                layer = BaseGeoserverREST.reader.getLayer(ws, layerName);
                RESTFeatureType featureType = BaseGeoserverREST.reader.getFeatureType(layer);

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
            return ReturnFormat.retParam(1000,null);
        }
    }


    /**
     * 发布图层MBTiles
     * @return
     */

    @RequestMapping(value = "/publishMBTiles", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ApiOperation(value = "发布MBTiles",notes = "发布MBTiles")
    public Object publishMBTiles(@ApiParam(name = "mbtileParams",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"datastore\":\"数据存储\",\n" +
            "  \"nameEn\":\"英文名称\",\n" +
            "  \"path\":\"路径\"\n}"
            ,required = true)@RequestBody(required = true) Map<String, String> mbtileParams){
        try{
            String workspace = mbtileParams.get("workspace");
            String store_name = mbtileParams.get("datastore");
            String layerName = mbtileParams.get("nameEn");
            String path = mbtileParams.get("path");
            File file = new File(extractFilePath+path);
            //判断图层是否已经存在，不存在则创建并发布
            RESTLayer layer = BaseGeoserverREST.manager.getReader().getLayer(workspace, layerName);
            if(layer == null){
                boolean publish = BaseGeoserverREST.publisher.publishGeoMBTILES(workspace, store_name,store_name, file);
                logger.info("mbtile发布"+publish);
                if(publish){
                    TableNames tableNames = tableNamesService.getByNameEn(layerName);
                    tableNames.setState(1l);
                    tableNames.setCreateTime(new Date());
                    tableNamesService.updateTableNames(tableNames);
                    return ReturnFormat.retParam(0,tableNames);
                }
                return ReturnFormat.retParam(1000,null);
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
    public Object publishTif(@ApiParam(name = "tifMap",value = "{\n" +
            "  \"workspace\": \"工作区\",\n" +
            "  \"datastore\":\"数据存储\",\n" +
            "  \"nameEn\":\"英文名称\",\n" +
            "  \"path\":\"路径\"\n}"+
            "}",required = true)@RequestBody(required = true) Map<String, String> tifMap){
        try{
            String workspace = tifMap.get("workspace");
            String store_name = tifMap.get("datastore");
            String layerName = tifMap.get("nameEn");
            String path = tifMap.get("path");
            File file = new File(extractFilePath+path);
            //判断图层是否已经存在，不存在则创建并发布
            RESTLayer layer = BaseGeoserverREST.manager.getReader().getLayer(workspace, layerName);
            if(layer == null){
                boolean publish = BaseGeoserverREST.publisher.publishGeoTIFF(workspace, store_name,store_name, file);
                logger.info("发布图层layerName---"+layerName+":"+publish);
                if(publish){
                    RESTCoverage coverage = BaseGeoserverREST.reader.getCoverage(workspace,store_name,layerName);
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
                }
                return ReturnFormat.retParam(1000,null);
            }else {
                logger.info("已经发布过图层:" + layerName);
                return ReturnFormat.retParam(4002,null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ReturnFormat.retParam(1000,null);
        }
    }


    @PostMapping(value="/findPage")
    @ApiOperation(value = "分页查询所有图层", notes = "分页查询")
    public Object findPage(@ApiParam(name = "pageQuery",value = "{\n" +
            "  \"pageNum\": 1,\n" +
            "  \"pageSize\": 20,\n" +
            "  \"order\": \"desc\",\n" +
            "  \"sort\":\"id\",\n" +
            "  \"params\":{\"state\":  -1不可用;0已上传未发布;1已发布可用,\"nameCn\": 图层名称}\n" +
            "}",required = true)@RequestBody PageRequest pageQuery) {
        return ReturnFormat.retParam(0,tableNamesService.findPage(pageQuery));
    }

    /*@GetMapping("/names")
    @ApiOperation(value = "查询数据库中表名", notes = "查询数据库中表名")
    public Object findTableName(){
        List<String> list = tableNamesService.findTableName();
        return ReturnFormat.retParam(0,list);
    }*/

/*    @PostMapping("/names")
    @ApiOperation(value = "查询单个表名",notes = "查询单个表名接口,查询条件待商榷")
    public Object getTableNames(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"nameCn\": \"中文名称\",\n" +
            "  \"nameEn\": \"英文名称\",\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> paramMap){
        String nameCn = paramMap.get("nameCn");
        String nameEn = paramMap.get("nameEn");
        TableNames tableNames = tableNamesService.getByName(nameCn,nameEn);
        return ReturnFormat.retParam(0,tableNames);
    }*/
/*
    @PostMapping("/nameCn")
    @ApiOperation(value = "查询中文名称是否存在",notes = "查询中文名称是否存在")
    public Object getByNameCn(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"nameCn\": \"中文名称\",\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> paramMap){
        String nameCn = paramMap.get("nameCn");
        TableNames tableNames = tableNamesService.getByNameCn(nameCn);
        return ReturnFormat.retParam(0,tableNames);
    }*/



    @DeleteMapping("/{ids}")
    @ApiOperation(value = "多表删除",notes = "多表删除接口")
    @Transactional
    public Object delete(@ApiParam(name = "ids",value = "ids",required = true)@PathVariable Long[] ids)  {
        List<TableNames> list = tableNamesService.getByIds(ids);
        boolean b = false;
        for (TableNames tableNames:list){
            //删除解压文件
            Object metadata = tableNames.getMetadata();
            JSONObject jsonObject = JSONObject.parseObject(metadata.toString());
            if(jsonObject.get("filename")!=null){
                String[] filenames = jsonObject.get("filename").toString().split("/");
                //删除解压路径文件夹
                FileUtils.delFolder(extractFilePath+filenames[0]);
            }

            //矢量数据删除数据库表
            if(tableNames.getFlag()==0){
                b = BaseGeoserverREST.publisher.removeLayer(tableNames.getWorkspace(), tableNames.getNameEn());
                System.out.println("删除矢量:"+tableNames.getNameEn()+","+b);
                if(b){
                    tableNamesService.delete(tableNames.getId());
                    tableNamesService.dropTable(tableNames.getNameEn());
                }else{
                    return ReturnFormat.retParam(2033,tableNames.getNameCn());
                }

            }else{
                b = BaseGeoserverREST.publisher.removeCoverageStore(tableNames.getWorkspace(), tableNames.getDatastore(),true);
                System.out.println("删除栅格:"+tableNames.getNameEn()+","+b);
                if(b){
                    tableNamesService.delete(tableNames.getId());
                }else{
                    return ReturnFormat.retParam(2033,tableNames.getNameCn());
                }
            }
        }
        return ReturnFormat.retParam(0,null);
    }


    @GetMapping("/extent/{taskId}")
    @ApiOperation(value = "根据TaskId查询bbox",notes = "根据TaskId查询bbox")
    public Object getExtent(@ApiParam(name = "taskId",value = "taskId",required = true)@PathVariable int taskId)  {
        String extent = tableNamesService.getExtent(taskId);
        return ReturnFormat.retParam(0,extent);
    }
}
