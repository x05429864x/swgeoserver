package it.geosolutions.swgeoserver.service.serviceImpl;

import it.geosolutions.swgeoserver.dao.TableNamesMapper;
import it.geosolutions.swgeoserver.entry.TableNames;
import it.geosolutions.swgeoserver.service.TableNamesService;
import org.apache.ibatis.annotations.Param;
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
    public List<String> findTableName() {
        return tableNamesMapper.findTableName();
    }

    @Override
    public List<TableNames> getByIds(Long[] ids) { return tableNamesMapper.getByIds(ids); }

    @Override
    public String getNameCn(String nameEn){
        List<TableNames> ts = tableNamesMapper.getNameCn(nameEn);
        if (ts.size()>0){
            return ts.get(0).getNameCn();
        }else {
            return "";
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
