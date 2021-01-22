package com.siweidg.swgeoserver.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.siweidg.swgeoserver.comm.base.BaseGeoserverREST;
import com.siweidg.swgeoserver.comm.init.Constants;
import com.siweidg.swgeoserver.exception.ReturnFormat;
import com.siweidg.swgeoserver.rest.GeoServerRESTPublisher;
import com.siweidg.swgeoserver.rest.decoder.RESTDataStore;
import com.siweidg.swgeoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//@RestController
//@RequestMapping("/datastore")
//@Api(tags = "数据存储接口", description = "数据存储")
public class DataStoreController  extends BaseGeoserverREST {

    /**************************数据存储***********************/
    @GetMapping(value = "/{workspace}")
    @ApiOperation(value = "查询工作区下所有数据存储",notes = "单一工作区下数据存储列表")
    public Object getDatastores(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace){
        List dtList = reader.getDatastoreNames(workspace);
        return ReturnFormat.retParam(0,dtList);
    }

//    @GetMapping(value = "/{workspace},{datastore}")
//    @ApiOperation(value = "数据存储详情",notes = "数据存储详情")
//    public Object getDatastore(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
//                               @ApiParam(name = "datastore",value = "数据存储名称",required = true) @PathVariable String datastore){
//        RESTDataStore dataStore = reader.getDatastore(workspace, datastore);
//        dataStore.getStoreType();
//        return ReturnFormat.retParam(0,dataStore);
//    }


    @PostMapping(value = "/{workspace},{datastore}")
    @ApiOperation(value = "添加数据存储",notes = "添加数据存储")
    public Object createDatastore(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
                                  @ApiParam(name = "datastore",value = "数据存储名称",required = true) @PathVariable String datastore){
        RESTDataStore restStore = manager.getReader().getDatastore(workspace, datastore);
        if(restStore==null){
            GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(datastore);
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

            boolean createStore = manager.getStoreManager().create(workspace, store);
            System.out.println("create store : " + createStore);
            return ReturnFormat.retParam(0,null);
        } else {
            System.out.println("数据存储已经存在了,store:" + datastore);
            return ReturnFormat.retParam(4001,null);
        }
    }

    @DeleteMapping(value = "/{workspace},{datastore}")
    @ApiOperation(value = "删除数据存储",notes = "删除数据存储")
    public Object deleteDatastore(@ApiParam(name = "workspace",value = "工作区名称",required = true) @PathVariable String workspace,
                                  @ApiParam(name = "datastore",value = "数据存储名称",required = true) @PathVariable String datastore){
        RESTDataStore restStore = manager.getReader().getDatastore(workspace, datastore);
        if(restStore!=null){
            publisher.removeDatastore(workspace, datastore, false, GeoServerRESTPublisher.Purge.METADATA);
            return ReturnFormat.retParam(0,null);
        } else {
            System.out.println("数据存储已经存在了,store:" + datastore);
            return ReturnFormat.retParam(4001,null);
        }
    }
    /**************************数据存储***********************/
}
