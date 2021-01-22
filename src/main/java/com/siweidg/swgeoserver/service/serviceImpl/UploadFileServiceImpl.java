package com.siweidg.swgeoserver.service.serviceImpl;

import com.siweidg.swgeoserver.comm.utils.Logger;
import com.siweidg.swgeoserver.comm.utils.ShpReadUtils;
import com.siweidg.swgeoserver.dao.UploadFileMapper;
import com.siweidg.swgeoserver.entry.TablesEntity;
import com.siweidg.swgeoserver.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional()
public class UploadFileServiceImpl implements UploadFileService {

    private static final Logger logger = Logger.getLogger(UploadFileServiceImpl.class);

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Override
    public String uploadZip(String zipFileName, String zipFilePath, String suffix) {
        return null;
    }

    @Override
    public int checkTableName(String tableName) {
        return uploadFileMapper.checkTableName(tableName);
    }

    @Override
    public List<Map<Object, Object>> readShp2List(String shpPath, String tableName) {
        List<Map<Object, Object>> list = new ArrayList<Map<Object,Object>>();
        File file = new File(shpPath);
        File[] files = file.listFiles();
        String code = "GBK";
        for(File f : files){
            if(f.isDirectory()) {
                readShp2List(shpPath, tableName);
            } else {
                if(f.getName().endsWith(".shp")) {
                    String fName = f.getName().substring(0,f.getName().length()-3);
                    fName+="cpg";
                    File f1 = new File(file+"/"+fName);
                    code = ShpReadUtils.readFileByChars(f1);
                    logger.info("-----------------------读取.shp完成,code为："+code);
                    list =  ShpReadUtils.readSHP(f.getAbsolutePath(),code);

                }
            }
        }
//        file.delete();
        return list;
    }

    @Override
    public int saveData(List<Map<Object, Object>> list,String tableName) {
        List<Object> valueList = new ArrayList<Object>();
        // 拼接列名
        List<String> key = new ArrayList<String>();
        for (Map<Object, Object> att : list) {
            for (Map.Entry<Object, Object> entry : att.entrySet()) {
                key.add(String.valueOf(entry.getKey()).toLowerCase());
            }
            break;
        }
        for (Map<Object, Object> att : list) {
            // 客户端传递的参数的VALUE
            StringBuffer value = new StringBuffer();

            for (Map.Entry<Object, Object> entry : att.entrySet()) {
                if(entry.getValue()==null){
                    value.append((char[]) null+ ",");
                }else {
                    value.append("'" + String.valueOf(entry.getValue()) + "',");
                }


            }
            valueList.add("("+value.substring(0,value.length() -1)+")");
//            break;
        }
        // 表实体类
        TablesEntity tablesEntity = new TablesEntity();
        // 设置表名
        tablesEntity.setTableName(tableName);
        // 设置列
        tablesEntity.setColumnList(key);
        // 设置保存的数据
        tablesEntity.setValueList(valueList);
        int flag = uploadFileMapper.saveData(tablesEntity);
        return flag;
    }

    @Override
    public int truncateTable(String tableName) {
        return uploadFileMapper.truncateTable(tableName);
    }

}
