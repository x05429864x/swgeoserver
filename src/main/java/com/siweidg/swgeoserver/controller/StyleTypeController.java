package com.siweidg.swgeoserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.siweidg.swgeoserver.comm.base.BaseGeoserverREST;
import com.siweidg.swgeoserver.comm.utils.XmlUtil;
import com.siweidg.swgeoserver.controller.base.BaseController;
import com.siweidg.swgeoserver.entry.StyleType;
import com.siweidg.swgeoserver.exception.ReturnFormat;
import com.siweidg.swgeoserver.service.StyleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.File;


@RestController
@RequestMapping("/styleType")
@Api(tags = "工作数据样式接口",description = "工作数据样式接口")
public class StyleTypeController extends BaseController {

    @Value("${geoserver_style_path}")
    private String GEOSERVER_STYLE_PATH;

    @Autowired
    private StyleTypeService styleTypeService;

    /*[
        {
            "dtype": 0,
                "fillcolor": "#AAAAAA",
                "fillopacity": "0.3",
                "linecolor": "#678643",
                "lineopacity": "0.1",
                "linetype": "dot",
                "linewidth": "1.5"
        },
        {
            "dtype": 1,
                "fillcolor": "#BBBBBB",
                "fillopacity": "0.3",
                "linecolor": "#670000",
                "lineopacity": "0.1",
                "linetype": "dash",
                "linewidth": "1.5"
        }
]*/
    @PostMapping(value = "/publishWorkStyles")
    @ApiOperation(value = "发布样式",notes = "样式")
    public Object publishWorkStyles(@ApiParam(name = "StyleType",value = "{\n" +
            "  \"id\": 0,\n" +
            "  \"dtype\": \"数据类型（如：未核查0、合法1、违法2、实地伪变化3…）\",\n" +
            "  \"linetype\": \"线型\",\n" +
            "  \"lineopacity\": \"线透明度\",\n" +
            "  \"linewidth\": 线宽,\n" +
            "  \"linecolor\": \"线型颜色\",\n" +
            "  \"fillopacity\": \"填充透明度\",\n" +
            "  \"fillcolor\": \"填充颜色\"\n" +
            "}",required = true)@RequestBody(required = true) StyleType styleType){
        try {
            boolean flag = BaseGeoserverREST.reader.existsStyle("workStyle");
            if(!flag){
                File newfile = new ClassPathResource("style/workStyle.sld").getFile();
                newfile.renameTo(new File(GEOSERVER_STYLE_PATH + "wordStyle.sld"));
                boolean sldpublished = BaseGeoserverREST.publisher.publishStyle(newfile,"workStyle");
                System.out.println("发布样式成功"+sldpublished);
            }
//            System.out.println("workStyle样式已存在.");
            File file = XmlUtil.modifyStyle(GEOSERVER_STYLE_PATH, styleType);
            boolean updateStyle = BaseGeoserverREST.publisher.updateStyle(file,"workStyle");
            System.out.println("更新样式成功"+updateStyle);
            if(updateStyle){
                if (null == styleType.getId()){
                    styleTypeService.insert(styleType);
                }else {
                    styleTypeService.updateByPrimaryKey(styleType);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ReturnFormat.retParam(0,null);
    }

}
