package it.geosolutions.swgeoserver.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.comm.utils.PageResult;
import it.geosolutions.swgeoserver.comm.utils.PageUtils;
import it.geosolutions.swgeoserver.comm.utils.RandomUtils;
import it.geosolutions.swgeoserver.dao.StyleMapper;
import it.geosolutions.swgeoserver.entry.Style;
import it.geosolutions.swgeoserver.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StyleServiceImpl implements StyleService {
    @Autowired
    private StyleMapper styleMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return styleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Style record) {
        return styleMapper.insert(record);
    }

    @Override
    public int insertSelective(Style record) {
        return styleMapper.insertSelective(record);
    }

    @Override
    public Style selectByPrimaryKey(Long id) {
        return styleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Style record) {
        return styleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Style record) {
        return styleMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest,getPageInfo(pageRequest));
    }

    @Override
    public Style getByName(String nameCn,String nameEn) {
        return styleMapper.getByName(nameCn,nameEn);
    }

    @Override
    public String getNameEn() {

        String nameEn = RandomUtils.verifyUserName(6, 4);
        Map<String,Object> map = new HashMap<>();
        map.put("nameCn","ceshi");
        map.put("nameEn",nameEn);
        Style byParams = styleMapper.getByParams(map);
        if(byParams!=null){
            getNameEn();
        }
        return nameEn;
    }

    @Override
    public String getMaxNumber() {
        String nameEn = "style";
        DecimalFormat df = new DecimalFormat("0000");
        String style = styleMapper.getMaxNumber();
        if(null==style){
            nameEn += "0001";
        } else {
            nameEn += df.format(Integer.parseInt(style)+1);
        }
        return nameEn;
    }

    @Override
    public Style getByParams(Map map) {
        return styleMapper.getByParams(map);
    }


    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<Style> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        String orderBy = pageRequest.getOrder();
        String sort = pageRequest.getSort();
        PageHelper.startPage(pageNum, pageSize,sort +" "+orderBy);
        List<Style> styles = styleMapper.findAll(pageRequest.getParams());
        return new PageInfo<Style>(styles);
    }
}
