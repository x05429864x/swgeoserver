package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/workspace")
//@Api(tags = "工作区接口", description = "工作区")
public class WorkSpaceController extends BaseGeoserverREST {

    /**************************工作区***********************/
    @GetMapping(value = "/")
    @ApiOperation(value = "查询工作区",notes = "工作区列表")
    public Object getWorkspaces(){
        List workspaceList = reader.getWorkspaceNames();
        return ReturnFormat.retParam(0,workspaceList);
    }

    @PostMapping(value = "/{workspace}")
    @ApiOperation(value = "添加工作区",notes = "添加工作区")
    public Object createWorkspace(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace){
        if(!reader.getWorkspaceNames().contains(workspace)) {
            publisher.createWorkspace(workspace);
            return ReturnFormat.retParam(0,null);
        } else {
            return ReturnFormat.retParam(4000,null);
        }
    }

//    @PutMapping(value = "/workspaces/{workspace},{workspaceNew}")
//    @ApiOperation(value = "编辑工作区",notes = "编辑工作区")
//    public Object updateWorkspace(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
//                                  @ApiParam(name = "workspaceNew",value = "新名称",required = true) @PathVariable String workspaceNew){
//        if(reader.getWorkspaceNames().contains(workspace)) {
//            publisher.updateWorkspace(workspace,workspaceNew);
//            return ReturnFormat.retParam(0,null);
//        } else {
//            return ReturnFormat.retParam(4003,null);
//        }
//    }

//    @PutMapping(value = "/namespace/{workspace}")
//    @ApiOperation(value = "编辑namespace",notes = "编辑namespace")
//    public Object updateNamespace(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace){
//        if(reader.getWorkspaceNames().contains(workspace)) {
//            // Prepare the JSON String instructing the Task about the SRS to use
//            // Performing the Task update
//            publisher.updateNamespace(workspace, URI.create("http://b.example.com"));
//            return ReturnFormat.retParam(0,null);
//        } else {
//            return ReturnFormat.retParam(4003,null);
//        }
//    }

    @DeleteMapping(value = "/{workspace},{flag}")
    @ApiOperation(value = "删除工作区",notes = "删除工作区")
    public Object deleteWorkspace(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
                                  @ApiParam(name = "flag",value = "删除工作区内容,",defaultValue = "true",required = true) @PathVariable boolean flag){
        if(reader.getWorkspaceNames().contains(workspace)) {
            publisher.removeWorkspace(workspace,flag);
            return ReturnFormat.retParam(0,null);
        } else {
            return ReturnFormat.retParam(4000,null);
        }
    }
    /**************************工作区***********************/
}
