package com.siweidg.swgeoserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.siweidg.swgeoserver.comm.utils.*;
import com.siweidg.swgeoserver.config.ApiJsonObject;
import com.siweidg.swgeoserver.config.ApiJsonProperty;
import com.siweidg.swgeoserver.controller.base.BaseController;
import com.siweidg.swgeoserver.dao.SqliteDao;
import com.siweidg.swgeoserver.entry.TableNames;
import com.siweidg.swgeoserver.exception.ReturnFormat;
import com.siweidg.swgeoserver.service.TableNamesService;
import com.siweidg.swgeoserver.service.UploadFileService;
import org.geotools.jdbc.JDBCDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
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

    /**
     * 上传文件的文件名
     */
    public static String uploadFileName = "";

    public static String filePath = "";

    @Value("${upload_file_path}")
    private String uploadFilePath;

    @Value("${upload_extract_path}")
    private String extractFilePath;


    static Map<String, Object> allMap = null;

    @Autowired
    private TableNamesService tableNamesService;

    @Autowired
    private UploadFileService uploadFileService;



    @ApiOperation(value = "上传zip导入数据库", notes = "upload Zip to the database", response = String.class)
    @RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @Transactional
    public Object uploadZip(@ApiParam(name = "uploadFile",value = "上传文件",required = true) @RequestPart ( value="uploadFile", required = true) MultipartFile zipFile,
                            @ApiJsonObject(name = "paramMap", value = {
                                    @ApiJsonProperty(key = "workspace", example = "workspace", description = "workspace"),
                                    @ApiJsonProperty(key = "tableName", example = "tableName", description = "tableName")
                            })
                            @RequestParam(required = true) Map<String, String> paramMap) throws Exception {
        String workspace = paramMap.get("workspace");
        String tableNameCn = paramMap.get("tableName");
        String creater = paramMap.get("creater");
//        String flag = paramMap.get("flag");
        uploadFileName = paramMap.get("fileName");// 上传文件
        String lastModifyTime = paramMap.get("lastModifyTime");// 最后修改时间
        String fileSize = paramMap.get("fileSize");
        RandomAccessFile randomAccessfile = null;
        String fileName = "";
        String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);

        File uploadDir = new File(uploadFilePath);
        if  (!uploadDir.exists()  && !uploadDir.isDirectory()){
            uploadDir.mkdir();
        }

        File extractDir = new File(extractFilePath);
        if  (!extractDir.exists()  && !extractDir.isDirectory()){
            extractDir.mkdir();
        }

        /**
         * 记录当前文件大小，用于判断文件是否上传完成
         */
        long currentFileLength = 0;
        long startTime = System.currentTimeMillis();
        String pathName = FileUtils.getFileNameNoEx(uploadFileName);
        long totalSize = zipFile.getSize();
        if (null != fileSize && !fileSize.equals("")) {
            totalSize = Long.parseLong(fileSize);
        }

        System.out.println(zipFile.getBytes());
        File newfile = new File(uploadFilePath + uploadFileName + "." + lastModifyTime);
        // 存在文件
        if (newfile.exists()) {
            randomAccessfile = new RandomAccessFile(newfile, "rw");
        } else {
            // 不存在文件，根据文件标识创建文件
            randomAccessfile = new RandomAccessFile(uploadFilePath + uploadFileName + "." + lastModifyTime, "rw");
        }
        // 开始文件传输
        InputStream in = new ByteArrayInputStream(zipFile.getBytes());
        randomAccessfile.seek(randomAccessfile.length());
        byte b[] = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            randomAccessfile.write(b, 0, n);
        }
        currentFileLength = randomAccessfile.length();

        // 关闭文件
        closeRandomAccessFile(randomAccessfile);
        randomAccessfile = null;
        if (currentFileLength < totalSize) {
            return ReturnFormat.retParam(10000, null);
        }
        List<String> list = new ArrayList();
        if (currentFileLength == totalSize) {
            File oldFile = new File(uploadFilePath + uploadFileName + "." + lastModifyTime);
            File newFile = new File(uploadFilePath + uploadFileName);
            if (!oldFile.exists()) {// 文件重复返回提示
                return ReturnFormat.retParam(2013, null);
            }
            if (!oldFile.renameTo(newFile)) {
                oldFile.delete();
            }
            in.close();
            long endTime = System.currentTimeMillis();
            System.out.println("文件上传运行时间：" + (endTime - startTime) + "ms");

//            String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1).toUpperCase();
//            String newFileName = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
            //=============解压============：创建ZipFile指向磁盘上的.zip文件
//            ZipFile zFile = new ZipFile(newFile);
//            zFile.setFileNameCharset("UTF-8");

            long extractStart= System.currentTimeMillis();
            //解压文件
//            zFile.extractAll(extractFilePath+pathName);


            try {
                if ("ZIP".equalsIgnoreCase(suffix)) {
                    UnZipAnRar.unZip(newFile, extractFilePath+pathName);
//                CompressFileUtils.unZipFiles(uploadFilePath + uploadFileName, filePath);
                } else if ("RAR".equalsIgnoreCase(suffix)) {
                    UnZipAnRar.unRar(newFile, extractFilePath+pathName);
//                CompressFileUtils.unRarFile(uploadFilePath + uploadFileName, filePath);
                } else {
                    return ReturnFormat.retParam(2019, null);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            long extractEnd = System.currentTimeMillis();
            System.out.println("解压运行时间：" + (extractEnd - extractStart) + "ms");
        }
        /*******上传解压结束*******/


        //读取mbtiles文件
        list = FileUtils.readMBTiles(extractFilePath + pathName, false);
        if(list.size()>0){
            fileName = list.get(0);
            //上传文件解压路径
            filePath = pathName+"/"+fileName;
        }else{
            return ReturnFormat.retParam(2017, null);
        }
        /*****图层对照表信息*****/
        Object result = uploadF(workspace, fileName, tableNameCn, pathName,creater);
        return result;
    }

    public Object uploadF(String workspace,String fileName,String tablenameCn,String name,String creater) {
        TableNames tableNames = new TableNames();
        tableNames.setCreater(creater!=null?Long.parseLong(creater):null);
        tableNames.setCreateTime(new Date());
        JSONObject jsonObject = new JSONObject();
        if("work".equals(workspace)){
            try {
                long shp2pgStart = System.currentTimeMillis();
                List<Map<Object, Object>> shpList = uploadFileService.readShp2List(extractFilePath+name+"/",tablenameCn);
                uploadFileService.saveData(shpList,"workdata_base");
                jsonObject.put("filename",filePath);
                tableNames = tableNamesService.getByNameEn("workdata_base");
                tableNames.setMetadata(jsonObject);
                tableNames.setDatastore("work");
                tableNamesService.updateTableNames(tableNames);
                long shp2pgEnd = System.currentTimeMillis();
                logger.info("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
            }catch(Exception e){
                e.printStackTrace();
            }
        //矢量辅助数据
        }else if("xzqh".equals(workspace)){
            JDBCDataStore datastore = PGDatastore.getDefeaultDatastore();
            Geotools geotools = new Geotools(datastore);
            try {
                boolean checkTable = checkTable("sys_xzqh");
                //查看对照表是否存在
                TableNames t = tableNamesService.getByName(tablenameCn, "sys_xzqh");
                if(!checkTable) {
                    if (t != null) {
                        return ReturnFormat.retParam(2030, t);
                    }
                    //读取shp并保存到list
//                    List<Map<Object, Object>> shpList = uploadFileService.readShp2List(extractFilePath + name + "/", tablenameCn);
                    //创建数据库表
                    geotools.createTable(extractFilePath + name + "/", fileName, "sys_xzqh", "");
                    logger.info("*********创建数据表成功");
                }
                long shp2pgStart = System.currentTimeMillis();
                List<Map<Object, Object>> shpList = uploadFileService.readShp2List(extractFilePath+name+"/",tablenameCn);
                uploadFileService.truncateTable("sys_xzqh");
                logger.info("********truncateTable 成功!");
                uploadFileService.saveData(shpList,"sys_xzqh");
                jsonObject.put("filename",filePath);
                tableNames = tableNamesService.getByNameEn("sys_xzqh");
                tableNames.setNameCn(tablenameCn);
                tableNames.setMetadata(jsonObject);
                tableNames.setDatastore("xzqh");
                tableNamesService.updateTableNames(tableNames);
                long shp2pgEnd = System.currentTimeMillis();
                logger.info("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if("jdxz".equals(workspace)){
            try {
                long shp2pgStart = System.currentTimeMillis();
                List<Map<Object, Object>> shpList = uploadFileService.readShp2List(extractFilePath+name+"/",tablenameCn);
                uploadFileService.truncateTable("sys_jdxz");
                logger.info("********truncateTable 成功!");
                uploadFileService.saveData(shpList,"sys_jdxz");
                jsonObject.put("filename",filePath);
                tableNames = tableNamesService.getByNameEn("sys_jdxz");
                tableNames.setNameCn(tablenameCn);
                tableNames.setMetadata(jsonObject);
                tableNames.setDatastore("jdxz");
                tableNamesService.updateTableNames(tableNames);
                long shp2pgEnd = System.currentTimeMillis();
                logger.info("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if("fuzhu_shp".equals(workspace)){
            String shpFileName = FileUtils.getFileNameNoEx(fileName);
            JDBCDataStore datastore = PGDatastore.getDefeaultDatastore();
            Geotools geotools = new Geotools(datastore);
            try {
                long shp2pgStart = System.currentTimeMillis();
                boolean checkTable = checkTable("shp_"+shpFileName);
//                if("0".equals(flag)){
                //查看对照表是否存在
                TableNames t = tableNamesService.getByName(tablenameCn, "shp_" + shpFileName);
                if(!checkTable){
                    if(t!=null){
                        return ReturnFormat.retParam(2030, t);
                    }
                    //读取shp并保存到list
                    List<Map<Object, Object>> shpList = uploadFileService.readShp2List(extractFilePath+name+"/",tablenameCn);
                    //创建数据库表
                    geotools.createTable(extractFilePath+name+"/",fileName,shpFileName,"");
                    logger.info("*********创建数据表成功");
                    int i = uploadFileService.saveData(shpList, "shp_" + shpFileName);
                    logger.info("*********保存数据表成功");
                    jsonObject.put("filename",filePath);
                    tableNames.setState(0L);
                    tableNames.setWorkspace(workspace);
                    tableNames.setNameCn(tablenameCn);
                    tableNames.setNameEn("shp_"+shpFileName);
                    tableNames.setFlag(0);
                    tableNames.setMetadata(jsonObject);
                    tableNames.setDatastore("gis");
                    tableNamesService.insertTableNames(tableNames);
                }else{
                    return ReturnFormat.retParam(2030, t);
                }
                long shp2pgEnd = System.currentTimeMillis();
                System.out.println("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
            }catch(Exception e){
                e.printStackTrace();
            }
        //影像辅助数据
        }else if("fuzhu_img".equals(workspace)){
            String newName = FileUtils.getFileNameNoEx(fileName);
            try{
                TableNames t = tableNamesService.getByName(tablenameCn, newName );
                if(t!=null){
                    return ReturnFormat.retParam(2030, t);
                }
                jsonObject.put("filename",filePath);
                tableNames.setState(0L);
                tableNames.setWorkspace(workspace);
                tableNames.setNameCn(tablenameCn);
                tableNames.setNameEn(newName);
                tableNames.setFlag(1);
                tableNames.setDatastore(newName);
                tableNames.setMetadata(jsonObject);
                tableNamesService.insertTableNames(tableNames);
            }catch (Exception e) {
                e.printStackTrace();
            }
        //离线MBtiles
        }else if("mbtile".equals(workspace)){
            String newName = fileName.substring(0, fileName.indexOf("."));

            try{
                jsonObject =  SqliteDao.executeQuery(extractFilePath+filePath);
                File fileLength = new File(extractFilePath+filePath);
                if (jsonObject == null || jsonObject.size() < 1) {
                    return ReturnFormat.retParam(2017, null);
                } else {
                    TableNames t = tableNamesService.getByName(jsonObject.get("name").toString(), newName );
                    if(t!=null){
                        return ReturnFormat.retParam(4002, "图层"+jsonObject.get("name").toString()+"已存在!");
                    }
                    String md5 = MD5Utils.getFileMD5String(new File(extractFilePath+filePath));
                    jsonObject.put("MD5",md5);
                    jsonObject.put("filename",filePath);
                    jsonObject.put("size",fileLength.length());
                    String json = JSON.toJSONString(jsonObject);
                    tableNames.setState(0L);
                    tableNames.setWorkspace(workspace);
                    tableNames.setNameCn(jsonObject.get("name").toString());
                    tableNames.setNameEn(newName);
                    tableNames.setFlag(1);
                    tableNames.setDatastore(newName);
                    tableNames.setMetadata(json);
                    tableNamesService.insertTableNames(tableNames);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ReturnFormat.retParam(0, tableNames);
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

    /**
     * 获取已上传的文件大小
     *
     */
    @RequestMapping(value = "/getChunkedFileSize", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object getChunkedFileSize(@RequestParam String filename, @RequestParam String lastModifyTime)
            throws Exception {
        Object obj = new Object();
        uploadFileName = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
        // String lastModifyTime = request.getParameter("lastModifyTime");
        File file = new File(uploadFilePath + uploadFileName + "." + lastModifyTime);
        if (file.exists()) {
            obj = ReturnFormat.retParam(0, file.length());
        } else {
            obj = ReturnFormat.retParam(0, -1);
        }
        return obj;
    }

    /**
     * 关闭随机访问文件
     *
     */
    public static void closeRandomAccessFile(RandomAccessFile rfile) {
        if (null != rfile) {
            try {
                rfile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

