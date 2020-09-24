package it.geosolutions.swgeoserver.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.comm.utils.PageResult;
import it.geosolutions.swgeoserver.comm.utils.PageUtils;
import it.geosolutions.swgeoserver.dao.TableNamesMapper;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TableNamesServiceImpl implements TableNamesService {

    @Autowired
    TableNamesMapper tableNamesMapper;

    @Override
    public List<TableNames> getAll(Long state) {
        return tableNamesMapper.getAll(state);
    }

    @Override
    public PageResult findPage(Map<String, String> paramMap) {
        PageRequest pageRequest = new PageRequest();
        String order = paramMap.get("order");
        String sort = paramMap.get("sort");
        String pageNum = paramMap.get("pageNum");
        String pageSize = paramMap.get("pageSize");
        if(order!=null&&sort!=null){
            pageRequest.setOrder(order);
            pageRequest.setSort(sort);
        }
        if(pageNum!=null){
            pageRequest.setPageNum(Integer.parseInt(pageNum));
        }
        if(pageSize!=null){
            pageRequest.setPageSize(Integer.parseInt(pageSize));
        }
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
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
        String sort = pageRequest.getSort();
        PageHelper.startPage(pageNum, pageSize,sort +" "+orderBy);
        List<TableNames> tableNames = tableNamesMapper.getPage();
        return new PageInfo<TableNames>(tableNames);
    }

    @Override
    public List<String> findTableName() {
        return tableNamesMapper.findTableName();
    }

    @Override
    public List<TableNames> getByIds(Long[] ids) { return tableNamesMapper.getByIds(ids); }

    @Override
    public TableNames getTableNameByNameEn(String nameEn){
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
    public int insertTableNames(TableNames tableNames) {
        return tableNamesMapper.insertTableNames(tableNames);
    }

    @Override
    public int deleteTableNames(Long[] ids) { return tableNamesMapper.deleteTableNames(ids); }

    @Override
    public int updateTableNames(TableNames tableNames) {
        return tableNamesMapper.updateTableNames(tableNames);
    }

    @Override
    public int updateState(Long[] ids) { return tableNamesMapper.updateState(ids); }

    @Override
    public int dropTable(String names) { return tableNamesMapper.dropTable(names); }
}
