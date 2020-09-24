package it.geosolutions.swgeoserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.entry.Entity;
import it.geosolutions.swgeoserver.entry.User;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * \* User: x
 * \* Date: 2020/8/20
 * \* Time: 9:25
 * \* Description:
 * \
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口", description = "用户接口")
public class UserController extends BaseGeoserverREST {

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
