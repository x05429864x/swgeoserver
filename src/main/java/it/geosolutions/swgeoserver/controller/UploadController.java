package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.utils.FileUtils;
import it.geosolutions.swgeoserver.comm.utils.Geotools;
import it.geosolutions.swgeoserver.comm.utils.PGDatastore;
import it.geosolutions.swgeoserver.controller.base.BaseController;
import it.geosolutions.swgeoserver.dao.UploadFileMapper;
import it.geosolutions.swgeoserver.entry.TablesEntity;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.service.UploadFileService;
import net.lingala.zip4j.core.ZipFile;
import org.geotools.data.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-15 14:04
 */

@RestController
@RequestMapping("/file")
@Api(tags = "文件上传接口", description = "文件上传入库")
public class UploadController extends BaseController {

    @Value("${upload_file_path}")
    private String uploadFilePath;

    @Value("${upload_extract_path}")
    private String extractFilePath;


    static Map<String, Object> allMap = null;

    @Autowired
    private UploadFileService uploadFileService;

    //option的value的内容是这个method的描述，notes是详细描述，response是最终返回的json model。其他可以忽略
//    @ApiOperation(value = "上传zip导入数据库", notes = "upload zipShp to the database", response = String.class)
    //这里是显示你可能返回的http状态，以及原因。比如404 not found, 303 see other
    /*@ApiResponses(value = {
            @ApiResponse(code = 405, message = "Invalid input", response = String.class) })*/
//    @PostMapping("/upload/zip/")
//    @RequestMapping(value = "/upload/zip/{tableName}/{flag}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
//    public Object uploadFile(@ApiParam(name = "uploadFile",value = "上传文件",required = true) @RequestPart ( value="uploadFile", required = true) MultipartFile zipFile,
//                             @ApiParam(name = "tableName",value = "表名",required = true) @PathVariable (required = true) String tableName,
//                             @ApiParam(name = "flag",value = "0:新增,1:追加",required = true) @PathVariable (required = true) String flag,
//                             HttpServletResponse response, HttpServletRequest request) throws Exception{
//        String zipFileName = zipFile.getOriginalFilename();
//        String name = FileUtils.getFileNameNoEx(zipFileName);
////        String suffix = zipFileName.substring(zipFileName.lastIndexOf("."));
////        LOGGER.info("上传文件名为："+zipFileName+"后缀为："+suffix+"  name:"+tableName);
//        String shpPath = "";
//        String newPath = new File(uploadFilePath).getAbsolutePath() +"/" + zipFileName;
//        File file = new File(newPath);
//        //检测是否存在目录
//        if(!file.getParentFile().exists()){
//            file.getParentFile().mkdirs();
//        }
//        long startTime = System.currentTimeMillis();
//
//        zipFile.transferTo(file);
//        long endTime = System.currentTimeMillis();
//        System.out.println("文件上传运行时间：" + (endTime - startTime) + "ms");
//
//        //解压：创建ZipFile指向磁盘上的.zip文件
//        ZipFile zFile = new ZipFile(file);
//        zFile.setFileNameCharset("UTF-8");
//        File destDir = new File(extractFilePath+name);
//
//        long extractStart= System.currentTimeMillis();
//        //解压文件
//        zFile.extractAll(extractFilePath+name);
//        long extractEnd = System.currentTimeMillis();
//        System.out.println("解压运行时间：" + (extractEnd - extractStart) + "ms");
//
////        shpPath = uploadFileService.uploadZip(zipFileName,file.getAbsolutePath(),suffix);
//        boolean result = false;
//        DataStore datastore = PGDatastore.getDefeaultDatastore();
//        Geotools geotools = new Geotools(datastore);
//
//        long shp2pgStart = System.currentTimeMillis();
//        if("0".equals(flag)){
//            result = geotools.shp2pgtable(extractFilePath+name+"/"+name+".shp",tableName,0);
//        }else{
//            result = geotools.shp2pgtable(extractFilePath+name+"/"+name+".shp",tableName,1);
//        }
//        long shp2pgEnd = System.currentTimeMillis();
//        System.out.println("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
//        return ReturnFormat.retParam(0, null);
//    }


    @ApiOperation(value = "上传zip导入数据库", notes = "upload zipShp to the database", response = String.class)
    @RequestMapping(value = "/upload/{tableName}/{flag}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadZip(@ApiParam(name = "uploadFile",value = "上传文件",required = true) @RequestPart ( value="uploadFile", required = true) MultipartFile zipFile,
                             @ApiParam(name = "tableName",value = "表名",required = true) @PathVariable (required = true) String tableName,
                             @ApiParam(name = "flag",value = "0:新增,1:追加",required = true) @PathVariable (required = true) String flag) throws Exception {

        String zipFileName = zipFile.getOriginalFilename();
        String name = FileUtils.getFileNameNoEx(zipFileName);
        String newPath = new File(uploadFilePath).getAbsolutePath() +"/" + zipFileName;
        File file = new File(newPath);
        //检测是否存在目录
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        long startTime = System.currentTimeMillis();

        zipFile.transferTo(file);
        long endTime = System.currentTimeMillis();
        System.out.println("文件上传运行时间：" + (endTime - startTime) + "ms");

        //解压：创建ZipFile指向磁盘上的.zip文件
        ZipFile zFile = new ZipFile(file);
        zFile.setFileNameCharset("UTF-8");

        long extractStart= System.currentTimeMillis();
        //解压文件
        zFile.extractAll(extractFilePath+name);
        long extractEnd = System.currentTimeMillis();
        System.out.println("解压运行时间：" + (extractEnd - extractStart) + "ms");


        DataStore datastore = PGDatastore.getDefeaultDatastore();
        Geotools geotools = new Geotools(datastore);
        long shp2pgStart = System.currentTimeMillis();
        boolean checkTable = checkTable(tableName);
        //读取shp并保存到list
        List<Map<String, Object>> list = uploadFileService.readShp2List(extractFilePath+name+"/",tableName);
        if("0".equals(flag)){
            if(!checkTable){
                //创建表
                geotools.createTable(extractFilePath+name+"/"+name+".shp",tableName,"");
                uploadFileService.saveData(list,tableName);
            }else{
                return ReturnFormat.retParam(2030, null);
            }
        }else{
            if(checkTable){
                uploadFileService.saveData(list,tableName);
            }else{
                return ReturnFormat.retParam(2032,null);
            }
        }
        long shp2pgEnd = System.currentTimeMillis();
        System.out.println("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
        return ReturnFormat.retParam(0, null);
    }

    /**
     * 验证数据库中表是否存在
     * @param tableName
     * @return
     * @throws Exception
     */
    public boolean checkTable(String tableName) throws Exception {
        int flag = uploadFileService.checkTableName(tableName);
        if (flag > 0)
            return true;
        return false;
    }
}

