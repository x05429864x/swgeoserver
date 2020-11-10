package it.geosolutions.swgeoserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import org.springframework.web.bind.annotation.*;


/**
 * \* User: x
 * \* Date: 2020/8/20
 * \* Time: 9:25
 * \* Description:
 * \
 */
//@RestController
//@RequestMapping("/user")
//@Api(tags = "用户接口", description = "用户接口")
public class UserController extends BaseGeoserverREST {

    /**
     * layerType :0非查看属性，1查看属性，2编辑wfs，3wmts服务,4快视图
     * @param workspace
     * @param type
     * @return
     * @throws Exception
     */
    /*@PostMapping(value = "/toList/{workspace},{type}")
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
                map.put("layerName_CN",workspace+":"+ PropUtil.getKey(nameLinkElem.getName()));
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

    /**
     *
     * @return
     */
    @GetMapping(value = "/users")
    @ApiOperation(value = "查询用户",notes = "查询用户")
    public Object getUsers(){
        JSONArray dtList = reader.getUsers();
        return ReturnFormat.retParam(0,dtList);
    }

    @PostMapping(value = "/")
    @ApiOperation(value = "添加用户",notes = "添加用户")
    public Object addUsers(@ApiParam(value = "{\n" +
            "  \"org.geoserver.rest.security.xml.JaxbUser\":{\n" +
            "     \"userName\": \"userName\",\n" +
            "     \"password\": \"password\",\n" +
            "     \"enabled\": true\n" +
            "  }\n" +
            "}",required = true)@RequestBody JSONObject jsonParam){

        String dtList = reader.addUser(jsonParam);
        return ReturnFormat.retParam(0,dtList);
    }

    @DeleteMapping(value = "/")
    @ApiOperation(value = "删除用户",notes = "删除用户")
    public Object addUsers(@PathVariable String user){
        reader.deleteUser(user);
        return ReturnFormat.retParam(0,null);
    }

    /**
     * 角色查询
     * @return
     */
    @GetMapping(value = "/roles")
    @ApiOperation(value = "查询角色",notes = "查询角色")
    public Object getRoles(){
        JSONArray dtList = reader.getRoles();
        return ReturnFormat.retParam(0,dtList);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping(value = "/roles/{role}")
    @ApiOperation(value = "添加角色",notes = "添加角色")
    public Object addRole(@ApiParam(name = "role",value = "角色名称",required = true) @PathVariable String role){
        String dtList = reader.addRole(role);
        return ReturnFormat.retParam(0,dtList);
    }

    /**
     * 删除角色
     * @param role
     * @return
     */
    @DeleteMapping(value = "/roles/{role}")
    @ApiOperation(value = "删除角色",notes = "删除角色")
    public Object deleteRole(@ApiParam(name = "role",value = "角色名称",required = true) @PathVariable String role){
        boolean b = reader.deleteRole(role);
        if(b){
            return ReturnFormat.retParam(0,null);
        }else {
            return ReturnFormat.retParam(1,null);
        }

    }
}
