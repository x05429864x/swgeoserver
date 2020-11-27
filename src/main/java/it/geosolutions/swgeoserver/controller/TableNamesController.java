package it.geosolutions.swgeoserver.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.comm.utils.FileUtils;
import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//实现跨域注解
//origin="*"代表所有域名都可访问
//maxAge飞行前响应的缓存持续时间的最大年龄，简单来说就是Cookie的有效期 单位为秒
//若maxAge是负数,则代表为临时Cookie,不会被持久化,Cookie信息保存在浏览器内存中,浏览器关闭Cookie就消失
//@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/table")
@Api(tags = "表名接口", description = "表名")
public class TableNamesController extends BaseGeoserverREST {

    @Value("${upload_file_path}")
    private String uploadFilePath;

    @Value("${upload_extract_path}")
    private String extractFilePath;
    @Autowired
    private TableNamesService tableNamesService;

    /*@PostMapping("/findTableNames")
    @ApiOperation(value = "图层查询接口", notes = "图层查询接口")
    public Object findTableNames(@ApiParam(name = "paramMap",value = "{\n" +
                                             "  \"state\": \"状态: 0正常,1作废\",\n" +
                                             "  \"isPublish\":\"0:未发布,1:已发布\"\n" +
                                             "}")@RequestBody Map<String ,Object> paramMap){
        List<TableNames> list = tableNamesService.findTableNames(paramMap);
        return ReturnFormat.retParam(0,list);
    }*/


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
                b = publisher.removeLayer(tableNames.getWorkspace(), tableNames.getNameEn());
                System.out.println("删除矢量:"+tableNames.getNameEn()+","+b);
                if(b){
                    tableNamesService.delete(tableNames.getId());
                    tableNamesService.dropTable(tableNames.getNameEn());
                }else{
                    return ReturnFormat.retParam(2033,tableNames.getNameCn());
                }

            }else{
                b = publisher.removeCoverageStore(tableNames.getWorkspace(), tableNames.getDatastore(),true);
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
}
