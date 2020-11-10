package it.geosolutions.swgeoserver.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.geosolutions.swgeoserver.comm.utils.*;
import it.geosolutions.swgeoserver.config.ApiJsonObject;
import it.geosolutions.swgeoserver.config.ApiJsonProperty;
import it.geosolutions.swgeoserver.controller.base.BaseController;
import it.geosolutions.swgeoserver.dao.SqliteDao;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.service.TableNamesService;
import it.geosolutions.swgeoserver.service.UploadFileService;
import org.geotools.data.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
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
//    @ResponseBody
    public Object uploadZip(@ApiParam(name = "uploadFile",value = "上传文件",required = true) @RequestPart ( value="uploadFile", required = true) MultipartFile zipFile,
                            @ApiJsonObject(name = "paramMap", value = {
                                    @ApiJsonProperty(key = "workspace", example = "workspace", description = "workspace"),
                                    @ApiJsonProperty(key = "tableName", example = "tableName", description = "tableName")
                            })
                            @RequestParam(required = true) Map<String, String> paramMap) throws Exception {
        String workspace = paramMap.get("workspace");
        String tableName = paramMap.get("tableName");
//        String flag = paramMap.get("flag");
        uploadFileName = paramMap.get("fileName");// 上传文件
        String lastModifyTime = paramMap.get("lastModifyTime");// 最后修改时间
        String fileSize = paramMap.get("fileSize");
        RandomAccessFile randomAccessfile = null;
        String filename = "";
        String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
        /**
         * 记录当前文件大小，用于判断文件是否上传完成
         */
        long currentFileLength = 0;
        long startTime = System.currentTimeMillis();
        String name = FileUtils.getFileNameNoEx(uploadFileName);
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
            long endTime = System.currentTimeMillis();
            System.out.println("文件上传运行时间：" + (endTime - startTime) + "ms");

//            String suffix = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1).toUpperCase();
//            String newFileName = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
            //=============解压============：创建ZipFile指向磁盘上的.zip文件
//            ZipFile zFile = new ZipFile(newFile);
//            zFile.setFileNameCharset("UTF-8");

            long extractStart= System.currentTimeMillis();
            //解压文件
//            zFile.extractAll(extractFilePath+name);


            if ("ZIP".equalsIgnoreCase(suffix)) {
                UnZipAnRar.unZip(newFile, extractFilePath+name);
//                CompressFileUtils.unZipFiles(uploadFilePath + uploadFileName, filePath);
            } else if ("RAR".equalsIgnoreCase(suffix)) {
                UnZipAnRar.unRar(newFile, extractFilePath+name);
//                CompressFileUtils.unRarFile(uploadFilePath + uploadFileName, filePath);
            } else {
                return ReturnFormat.retParam(2019, null);
            }
            long extractEnd = System.currentTimeMillis();
            System.out.println("解压运行时间：" + (extractEnd - extractStart) + "ms");
        }
        /*******上传解压结束*******/


        /*****图层对照表信息*****/
        TableNames tableNames = new TableNames();
        tableNames.setState(0L);
//        tableNames.setIsPublish(0L);
        tableNames.setWorkspace(workspace);
        tableNames.setNameCn(tableName);

        //读取mbtiles文件
        list = FileUtils.readMBTiles(extractFilePath + name, false);
        if(list.size()>0){
            filename = list.get(0);
//            filePath = extractFilePath + name+"/"+filename;
            //上传文件解压路径
            filePath = name+"/"+filename;
        }else{
            return ReturnFormat.retParam(2017, null);
        }
//        tableNames.setExtractPath(filePath);

//        tableNames.setMd5(md5);
        Map map = new HashMap();
        if("vector_sup".equals(workspace)){
            String shpFileName = FileUtils.getFileNameNoEx(filename);
            DataStore datastore = PGDatastore.getDefeaultDatastore();
            Geotools geotools = new Geotools(datastore);
            try {
                long shp2pgStart = System.currentTimeMillis();
                boolean checkTable = checkTable("shp_"+shpFileName);
//                if("0".equals(flag)){
                    //查看对照表是否存在
                    TableNames t = tableNamesService.getByName(tableName, "shp_" + shpFileName);
                    if(!checkTable){
                        if(t!=null){
                            return ReturnFormat.retParam(2030, null);
                        }
                        map.put("filename",filePath);
                        String json = JSON.toJSONString(map);
                        tableNames.setNameEn("shp_"+shpFileName);
                        tableNames.setFlag(0);
                        tableNames.setMetadata(json);
                        tableNames.setDatastore("gis");
                        tableNamesService.insertTableNames(tableNames);
                        //读取shp并保存到list
                        List<Map<String, Object>> shpList = uploadFileService.readShp2List(extractFilePath+name+"/",tableName);
                        //创建数据库表
                        geotools.createTable(extractFilePath+name+"/"+filename,shpFileName,"");
                        logger.info("*********创建数据表成功");
                        uploadFileService.saveData(shpList,"shp_"+shpFileName);
                        logger.info("*********保存数据表成功");
                    }else{
                        return ReturnFormat.retParam(2030, null);
                    }
                /*}else{
                    if(checkTable){
                        uploadFileService.saveData(shpList,"shp_"+name);
                    }else{
                        return ReturnFormat.retParam(2032,null);
                    }
                }*/
                long shp2pgEnd = System.currentTimeMillis();
                System.out.println("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
            }catch(Exception e){

            }finally {
                datastore.dispose();
            }
        }else if("image_sup".equals(workspace)){
            String newName = FileUtils.getFileNameNoEx(filename);
            try{
                TableNames t = tableNamesService.getByName(tableName, newName );
                if(t!=null){
                    return ReturnFormat.retParam(2030, null);
                }
                map.put("filename",filePath);
                String json = JSON.toJSONString(map);
                tableNames.setNameCn(tableName);
                tableNames.setNameEn(newName);
                tableNames.setFlag(1);
                tableNames.setDatastore(newName);
                tableNames.setMetadata(json);
                tableNamesService.insertTableNames(tableNames);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }else if("mbtiles".equals(workspace)){
            String newName = FileUtils.getFileNameNoEx(filename);
            String metadata_sql = "select * from metadata";
            try{
                map =  SqliteDao.executeQuery(extractFilePath+filePath, metadata_sql);
                if (map == null || map.size() < 1) {
//                    throw new RuntimeException("没有在数据库中查到有关结果");
                    return ReturnFormat.retParam(2017, null);
                } else {
                    TableNames t = tableNamesService.getByName(tableName, newName );
                    if(t!=null){
                        return ReturnFormat.retParam(2030, null);
                    }
                    String md5 = MD5Utils.getFileMD5String(new File(extractFilePath+filePath));
                    map.put("MD5",md5);
                    map.put("filename",filePath);
                    String json = JSON.toJSONString(map);
                    tableNames.setNameCn(map.get("name").toString());
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

