package it.geosolutions.swgeoserver.controller;

import io.swagger.annotations.*;
import it.geosolutions.swgeoserver.comm.base.BaseGeoserverREST;
import it.geosolutions.swgeoserver.comm.utils.BooleanUtils;
import it.geosolutions.swgeoserver.comm.utils.PageRequest;
import it.geosolutions.swgeoserver.comm.utils.RandomUtils;
import it.geosolutions.swgeoserver.comm.utils.XmlUtil;
import it.geosolutions.swgeoserver.config.ApiJsonObject;
import it.geosolutions.swgeoserver.config.ApiJsonProperty;
import it.geosolutions.swgeoserver.controller.base.BaseController;
import it.geosolutions.swgeoserver.entry.Style;
import it.geosolutions.swgeoserver.entry.StyleType;
import it.geosolutions.swgeoserver.exception.ReturnFormat;
import it.geosolutions.swgeoserver.rest.HTTPUtils;
import it.geosolutions.swgeoserver.service.StyleService;
import it.geosolutions.swgeoserver.service.StyleTypeService;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.hibernate.result.Outputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/style")
@Api(tags = "样式接口",description = "样式接口")
public class StyleController extends BaseController {

    @Value("${geoserver_style_path}")
    private String GEOSERVER_STYLE_PATH;

    @Autowired
    private StyleService styleService;


    @PostMapping(value = "/uploadStyle")
    @ApiOperation(value = "上传样式",notes = "上传样式")
    public Object uploadStyle(@ApiParam(name = "uploadFile",value = "上传样式",required = true)
                                  @RequestPart( value="uploadFile", required = true) MultipartFile file,
                              Style style) {
        try{
            String nameEn = styleService.getMaxNumber();
            File dest = new File(new File(GEOSERVER_STYLE_PATH).getAbsolutePath() +"/"+ nameEn+".sld");
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            logger.info("上传成功");
            boolean flag = BaseGeoserverREST.reader.existsStyle(nameEn);
            if(!flag){
                Map map = new HashMap();
                map.put("nameCn",style.getStyleNameCn());
                Style byName = styleService.getByParams(map);
                if(byName==null){
                    boolean b = BaseGeoserverREST.publisher.publishStyle(dest, nameEn);
                    logger.info("*************************************发布style:"+b);
                    if(b){
                        style.setStyleNameEn(nameEn);
                        style.setStyleCreateTime(new Date());
                        styleService.insertSelective(style);
                        return ReturnFormat.retParam(0,null);
                    }
                }
                return ReturnFormat.retParam(4004,null);
            }else {
                return ReturnFormat.retParam(4004,null);
            }
        }catch(Exception e){
            e.printStackTrace();
            return ReturnFormat.retParam(4004,null);
        }
    }

    @ApiOperation(value = "下载sld模板",notes = "下载sld模板")
    @PostMapping("/downloadTemp")
    public void downloadTemp(HttpServletResponse response) throws IOException {
        /** 获取静态文件的路径 .*/
        ClassPathResource resource = new ClassPathResource("/style/sld.zip");
        InputStream inputStream = resource.getInputStream();
        /** 将文件名称进行编码 */
        response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode("sld.zip","UTF-8"));
        response.setContentType("content-type:octet-stream");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(buffer)) != -1){ /** 将流中内容写出去 .*/
            outputStream.write(buffer ,0 , len);
        }
        bufferedInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改样式名称",notes = "修改样式名称")
    public Object update(@RequestBody @ApiParam(name="样式对象",value="传入json格式",required=true)Style style) {
        Map map = new HashMap();
        map.put("nameCn",style.getStyleNameCn());
        Style byName = styleService.getByParams(map);
        if(byName==null){
            styleService.updateByPrimaryKeySelective(style);
            return ReturnFormat.retParam(0,null);
        }else{
            return ReturnFormat.retParam(4004,null);
        }
    }


    @PostMapping(value="/findPage")
    @ApiOperation(value = "分页查询所有图层", notes = "分页查询")
    public Object findPage(@ApiParam(name = "pageQuery",value = "{\n" +
            "  \"pageNum\": 1,\n" +
            "  \"pageSize\": 20,\n" +
            "  \"order\": \"desc\",\n" +
            "  \"sort\":\"id\",\n" +
            "  \"params\":{\"nameCn\": \"样式名称\"}\n" +
            "}",required = true)@RequestBody PageRequest pageQuery) {
        return ReturnFormat.retParam(0,styleService.findPage(pageQuery));
    }

    /*@GetMapping(value="/styleImg")
    @ApiOperation(value = "img", notes = "img")
    public Object styleImg(@RequestParam(required = true) Map<String, String> params,HttpServletResponse response) throws IOException {
        String nameEn = params.get("nameEn");
        String res = reader.imgStyle(nameEn);
        InputStream inputStream = new ByteArrayInputStream(res.getBytes());
        return ImageIO.read(inputStream);
    }*/
//        response.setHeader("content-disposition","inline; filename=geoserver-GetLegendGraphic.image");
//        response.setContentType("image/png");
//        String nameEn = params.get("nameEn");
//
//        String res = reader.imgStyle(nameEn);
//        InputStream inputStream = new ByteArrayInputStream(res.getBytes());
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//        OutputStream outputStream = response.getOutputStream();
//        byte[] buffer = new byte[1024];
//        int len;
//        while ((len = bufferedInputStream.read(buffer)) != -1){ /** 将流中内容写出去 .*/
//            outputStream.write(buffer ,0 , len);
//        }
//        bufferedInputStream.close();
//        inputStream.close();
//        outputStream.close();
//        return response;


}
