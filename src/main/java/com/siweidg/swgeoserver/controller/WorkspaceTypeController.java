package com.siweidg.swgeoserver.controller;

import com.siweidg.swgeoserver.comm.utils.PageRequest;
import com.siweidg.swgeoserver.controller.base.BaseController;
import com.siweidg.swgeoserver.entry.WorkspaceType;
import com.siweidg.swgeoserver.exception.ReturnFormat;
import com.siweidg.swgeoserver.service.WorkspaceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/workspacetype")
@Api(tags = "工作区对照表接口",description = "工作区对照表接口")
public class WorkspaceTypeController extends BaseController {

    @Autowired
    private WorkspaceTypeService workspaceTypeService;


    @PostMapping(value = "/insert")
    @ApiOperation(value = "新增名称",notes = "新增名称")
    public Object insert(@RequestBody WorkspaceType workspaceType) {
        Map map = new HashMap();
        map.put("enName",workspaceType.getEnName());
        WorkspaceType byName = workspaceTypeService.getByParams(map);
        if(byName==null){
            workspaceTypeService.insert(workspaceType);
            return ReturnFormat.retParam(0,null);
        }else{
            return ReturnFormat.retParam(4004,null);
        }
    }

//    @DeleteMapping(value = "/delete/{ids}")
//    @ApiOperation(value = "删除名称",notes = "删除名称")
//    public Object delete(@ApiParam(name = "ids",value = "ids",required = true)@PathVariable Long[] ids) {
//        workspaceTypeService.deleteByids(ids);
//        return ReturnFormat.retParam(0,null);
//    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改名称",notes = "修改名称")
    public Object update(@RequestBody WorkspaceType workspaceType) {
        Map map = new HashMap();
        map.put("enName",workspaceType.getEnName());
        WorkspaceType byName = workspaceTypeService.getByParams(map);
        if(byName==null){
            workspaceTypeService.updateByPrimaryKeySelective(workspaceType);
            return ReturnFormat.retParam(0,null);
        }else{
            return ReturnFormat.retParam(4004,null);
        }
    }


    @PostMapping(value="/findPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public Object findPage(@ApiParam(name = "pageQuery",value = "{\n" +
            "  \"pageNum\": 1,\n" +
            "  \"pageSize\": 20,\n" +
            "  \"order\": \"desc\",\n" +
            "  \"sort\":\"id\",\n" +
            "  \"params\":{\"cnName\": \"名称\"}\n" +
            "}",required = true)@RequestBody PageRequest pageQuery) {
        return ReturnFormat.retParam(0,workspaceTypeService.findPage(pageQuery));
    }


}
