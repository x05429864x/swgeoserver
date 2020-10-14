package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.comm.utils.SNUtil;
import it.geosolutions.swgeoserver.controller.base.BaseController;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.rest.decoder.RESTBoundingBox;
import it.geosolutions.swgeoserver.rest.decoder.RESTFeatureType;
import it.geosolutions.swgeoserver.rest.decoder.RESTLayer;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//实现跨域注解
//origin="*"代表所有域名都可访问
//maxAge飞行前响应的缓存持续时间的最大年龄，简单来说就是Cookie的有效期 单位为秒
//若maxAge是负数,则代表为临时Cookie,不会被持久化,Cookie信息保存在浏览器内存中,浏览器关闭Cookie就消失
//@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/table")
@Api(tags = "表名接口", description = "表名")
public class TableNamesController extends BaseGeoserverREST {

    @Autowired
    private TableNamesService tableNamesService;

    @PostMapping("/findTableNames")
    @ApiOperation(value = "图层查询接口", notes = "图层查询接口")
    public Object findTableNames(@ApiParam(name = "paramMap",value = "{\n" +
                                             "  \"state\": \"状态: 0正常,1作废\",\n" +
                                             "  \"isPublish\":\"0:未发布,1:已发布\"\n" +
                                             "}")@RequestBody Map<String ,Object> paramMap){
        List<TableNames> list = tableNamesService.findTableNames(paramMap);
        return ReturnFormat.retParam(0,list);
    }

    @PostMapping(value="/findPage")
    @ApiOperation(value = "分页查询所有图层", notes = "分页查询")
    public Object findPage(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"pageNum\": 1,\n" +
            "  \"pageSize\": 20,\n" +
            "  \"state\": \"状态: 0正常,1作废\",\n" +
            "  \"isPublish\":\"0:未发布,1:已发布\"\n" +
            "}",required = true)@RequestBody(required = false) Map<String, Object> paramMap) {
//        PageInfo<> page = tableNamesService.
        return tableNamesService.findPage(paramMap);
    }

    @GetMapping("/names")
    @ApiOperation(value = "查询数据库中表名", notes = "查询数据库中表名")
    public Object findTableName(){
        List<String> list = tableNamesService.findTableName();
        return ReturnFormat.retParam(0,list);
    }

    @PostMapping("/names")
    @ApiOperation(value = "查询单个表名",notes = "查询单个表名接口,查询条件待商榷")
    public Object getTableNames(@ApiParam(name = "paramMap",value = "{\n" +
            "  \"nameCn\": \"中文名称\",\n" +
            "  \"nameEn\":\"英文名称\"\n" +
            "}",required = true)@RequestBody(required = true) Map<String, String> paramMap){
        String nameCn = paramMap.get("nameCn");
        String nameEn = paramMap.get("nameEn");
        TableNames tableNames = tableNamesService.getByName(nameCn,nameEn);
        return ReturnFormat.retParam(0,tableNames);
    }

    @PostMapping()
    @ApiOperation(value = "增加表名",notes = "新增表名接口")
    public Object insert(@ApiParam(name = "Tablenames",value = "{\n" +
            "  \"workspace\": \"tuban\",\n" +
            "  \"datastore\": gis,\n" +
            "  \"nameCn\": \"中国\",\n" +
            "  \"nameEn\": \"china\",\n" +
            "  \"remark\": \"remark\",\n" +
            "  \"flag\": \"栅格:1 矢量:0\",\n" +
            "  \"creater\": 0,\n" +
            "}",required = true)@RequestBody TableNames t) {
        TableNames tableNames = tableNamesService.getByName(t.getNameCn(),t.getNameEn());
        if(tableNames!=null){
            return ReturnFormat.retParam(2030,null);
        }
        t.setCreateTime(new Date());
        t.setState(0l);
        int i = tableNamesService.insertTableNames(t);
        return ReturnFormat.retParam(0,t);

    }

    @PutMapping()
    @ApiOperation(value = "修改表名",notes = "修改表名接口")
    @Transactional
    public Object update(@ApiParam(name = "Tablenames",value = "{\n" +
            "  \"id\": 0,\n" +
            "  \"nameCn\": \"中国\",\n" +
            "  \"nameEn\": \"china\",\n" +
            "  \"workspace\": \"tuban\",\n" +
            "  \"datastore\": gis,\n" +
            "  \"nameCn\": \"中国\",\n" +
            "  \"nameEn\": \"china\",\n" +
            "  \"remark\": \"remark\",\n" +
            "  \"flag\": \"栅格:1 矢量:0\",\n" +
            "  \"updater\": 0\n" +
            "}",required = true)@RequestBody TableNames t) {
        t.setUpdateTime(new Date());
        tableNamesService.updateTableNames(t);
        return ReturnFormat.retParam(0,null);
    }

    @PutMapping("/{ids}")
    @ApiOperation(value = "作废",notes = "表名作废接口")
    public Object updateState(@ApiParam(name = "ids",value = "ids",required = true)@PathVariable Long[] ids)  {
        tableNamesService.updateState(ids);
        return ReturnFormat.retParam(0,null);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除多个表名",notes = "删除多个表名接口")
    public Object delete(@ApiParam(name = "ids",value = "ids",required = true)@PathVariable Long[] ids)  {
        List<TableNames> list = tableNamesService.getByIds(ids);
        int i = tableNamesService.deleteTableNames(ids);
        if(i>1){
            String names ="";
            for(TableNames tableNames:list){
                names += tableNames.getNameEn()+",";
            }
            names = names.substring(0, names.length()-1);
            tableNamesService.dropTable(names);
        }
        return ReturnFormat.retParam(0,null);
    }
}
