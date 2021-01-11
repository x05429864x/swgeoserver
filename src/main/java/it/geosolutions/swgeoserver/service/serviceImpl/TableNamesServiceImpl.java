package it.geosolutions.swgeoserver.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.comm.utils.FileUtils;
import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.comm.utils.PageResult;
import it.geosolutions.swgeoserver.comm.utils.PageUtils;
import it.geosolutions.swgeoserver.dao.TableNamesMapper;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TableNamesServiceImpl extends BaseGeoserverREST implements TableNamesService {

    @Autowired
    TableNamesMapper tableNamesMapper;

    @Override
    public List<TableNames> findTableNames(Map<String ,Object> paramMap) {
        return tableNamesMapper.findTableNames(paramMap);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest,getPageInfo(pageRequest));
    }

    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<TableNames> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        String orderBy = pageRequest.getOrder();
        String sort = "t.id";
        PageHelper.startPage(pageNum, pageSize,sort +" "+orderBy);
        List<TableNames> tableNames = tableNamesMapper.findTableNames(pageRequest.getParams());
        return new PageInfo<TableNames>(tableNames);
    }


    @Override
    public List<TableNames> getByIds(Long[] ids) { return tableNamesMapper.getByIds(ids); }

    @Override
    public TableNames getByNameEn(String nameEn){
        List<TableNames> ts = tableNamesMapper.getTableNameByNameEn(nameEn);
        if (ts.size()>0){
            return ts.get(0);
        }else {
            return null;
        }
    }

    @Override
    public TableNames getByName(String nameCn,String nameEn) {
        return tableNamesMapper.getByName(nameCn,nameEn);
    }

    @Override
    public TableNames getByNameCn(String nameCn) {
        return tableNamesMapper.getByNameCn(nameCn);
    }

    @Override
    public int insertTableNames(TableNames tableNames) {
        return tableNamesMapper.insertTableNames(tableNames);
    }


    @Override
    public int delete(Long id) { return tableNamesMapper.delete(id); }

    @Override
    public int delete(Long[] ids) {
        List<TableNames> list = tableNamesMapper.getByIds(ids);
        boolean b = false;
        for (TableNames tableNames:list){
            //删除解压文件
            Object metadata = tableNames.getMetadata();
            JSONObject jsonObject = JSONObject.parseObject(metadata.toString());
            if(jsonObject.get("filename")!=null){
                String[] filenames = jsonObject.get("filename").toString().split("/");
                //删除解压路径文件夹
//                FileUtils.delFolder(extractFilePath+filenames[0]);
            }

            //矢量数据删除数据库表
            if(tableNames.getFlag()==0){
                b = publisher.removeLayer(tableNames.getWorkspace(), tableNames.getNameEn());
                System.out.println("删除矢量:"+tableNames.getNameEn()+","+b);
                if(b){
                    tableNamesMapper.delete(tableNames.getId());
                    tableNamesMapper.dropTable(tableNames.getNameEn());
                }else{
                    return 2033;
                }

            }else{
                b = publisher.removeCoverageStore(tableNames.getWorkspace(), tableNames.getDatastore(),true);
                System.out.println("删除栅格:"+tableNames.getNameEn()+","+b);
                if(b){
                    tableNamesMapper.delete(tableNames.getId());
                }else{
                    return 2033;
                }
            }
        }
        return 0;
    }

    @Override
    public int updateTableNames(TableNames tableNames) {
        return tableNamesMapper.updateTableNames(tableNames);
    }

    @Override
    public String getExtent(int taskId) {
        return tableNamesMapper.getExtent(taskId);
    }

    @Override
    public int dropTable(String names) { return tableNamesMapper.dropTable(names); }

    /*@Override
    public List<String> findTableName() {
        return tableNamesMapper.findTableName();
    }
    @Override
    public int deleteTableNames(Long[] ids) { return tableNamesMapper.deleteTableNames(ids); }
    @Override
    public int updateState(Long[] ids) { return tableNamesMapper.updateState(ids); }*/
}
