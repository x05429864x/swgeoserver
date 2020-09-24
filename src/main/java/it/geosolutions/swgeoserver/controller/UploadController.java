package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.*;
import it.geosolutions.swgeoserver.comm.utils.CompressFileUtils;
import it.geosolutions.swgeoserver.comm.utils.FileUtils;
import it.geosolutions.swgeoserver.comm.utils.Geotools;
import it.geosolutions.swgeoserver.comm.utils.PGDatastore;
import it.geosolutions.swgeoserver.config.ApiJsonObject;
import it.geosolutions.swgeoserver.config.ApiJsonProperty;
import it.geosolutions.swgeoserver.controller.base.BaseController;
import it.geosolutions.swgeoserver.dao.UploadFileMapper;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.entry.TablesEntity;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.service.TableNamesService;
import it.geosolutions.swgeoserver.service.UploadFileService;
import net.lingala.zip4j.core.ZipFile;
import org.geotools.data.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
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

//    ,
//    @ApiParam(name = "tableName",value = "表名",required = true) @PathVariable (required = true) String tableName,
//    @ApiParam(name = "flag",value = "0:新增,1:追加",required = true) @PathVariable (required = true) String flag

    @ApiOperation(value = "上传zip导入数据库", notes = "upload zipShp to the database", response = String.class)
    @RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
//    @ResponseBody
    public Object uploadZip(@ApiParam(name = "uploadFile",value = "上传文件",required = true) @RequestPart ( value="uploadFile", required = true) MultipartFile zipFile,
                            @ApiJsonObject(name = "paramMap", value = {
                                    @ApiJsonProperty(key = "workspace", example = "workspace", description = "workspace"),
                                    @ApiJsonProperty(key = "tableName", example = "tableName", description = "tableName"),
                                    @ApiJsonProperty(key = "flag", example = "1", description = "flag")
                            })
                            @RequestParam(required = true) Map<String, String> paramMap) throws Exception {
        String workspace = paramMap.get("workspace");
        String tableName = paramMap.get("tableName");
        String flag = paramMap.get("flag");
        uploadFileName = paramMap.get("fileName");// 上传文件
        String lastModifyTime = paramMap.get("lastModifyTime");// 最后修改时间
        String fileSize = paramMap.get("fileSize");
        RandomAccessFile randomAccessfile = null;
        String MBTilesName = "";
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
            ZipFile zFile = new ZipFile(newFile);
            zFile.setFileNameCharset("UTF-8");

            long extractStart= System.currentTimeMillis();
            //解压文件
            zFile.extractAll(extractFilePath+name);
            long extractEnd = System.currentTimeMillis();
            System.out.println("解压运行时间：" + (extractEnd - extractStart) + "ms");
//            if ("ZIP".equals(suffix)) {
//                CompressFileUtils.unZipFiles(uploadFilePath + uploadFileName, filePath);
//            } else if ("RAR".equals(suffix)) {
//                CompressFileUtils.unRarFile(uploadFilePath + uploadFileName, filePath);
//            } else {
//                return ReturnFormat.retParam(2019, null);
//            }
        }

        TableNames tableNames = new TableNames();
        tableNames.setState(0L);
        tableNames.setIsPublish(0L);
        tableNames.setWorkspace(workspace);
        tableNames.setNameCn(tableName);
        list = FileUtils.readMBTiles(extractFilePath + name, false);
        if(list.size()>0){
            MBTilesName = list.get(0);
            filePath = extractFilePath + name+"/"+MBTilesName;
        }else{
            return ReturnFormat.retParam(2017, tableNames);
        }
        tableNames.setExtractPath(filePath);
        if("auxiliary".equals(workspace)||"tuban".equals(workspace)){
            tableNames.setNameEn("shp_"+name);
            tableNames.setFlag(0);
            tableNames.setDatastore("gis");
            DataStore datastore = PGDatastore.getDefeaultDatastore();
            Geotools geotools = new Geotools(datastore);
            try {
                long shp2pgStart = System.currentTimeMillis();
                boolean checkTable = checkTable("shp_"+name);

                //读取shp并保存到list
                List<Map<String, Object>> shpList = uploadFileService.readShp2List(extractFilePath+name+"/",tableName);
                if("0".equals(flag)){
                    TableNames t = tableNamesService.getByName(tableName, "shp_" + name);
                    if(!checkTable){
                        if(t!=null){
                            return ReturnFormat.retParam(2030, null);
                        }
                        tableNamesService.insertTableNames(tableNames);
                        //创建表
                        geotools.createTable(extractFilePath+name+"/"+name+".shp",name,"");
                        uploadFileService.saveData(shpList,"shp_"+name);
                    }else{
                        return ReturnFormat.retParam(2030, null);
                    }
                }else{
                    if(checkTable){
                        uploadFileService.saveData(shpList,"shp_"+name);
                    }else{
                        return ReturnFormat.retParam(2032,null);
                    }
                }
                long shp2pgEnd = System.currentTimeMillis();
                System.out.println("写入数据库运行时间：" + (shp2pgEnd - shp2pgStart) + "ms");
            }finally {
                datastore.dispose();
            }
        }else{
            String newName = FileUtils.getFileNameNoEx(MBTilesName);
            TableNames t = tableNamesService.getByName(tableName, newName );
            if(t!=null){
                return ReturnFormat.retParam(2030, null);
            }
            tableNames.setNameEn(newName);
            tableNames.setFlag(1);
            tableNames.setDatastore(newName);
            tableNamesService.insertTableNames(tableNames);
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

