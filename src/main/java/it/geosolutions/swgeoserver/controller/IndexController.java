package it.geosolutions.swgeoserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.geosolutions.swgeoserver.controller.base.BaseController;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

//    @GetMapping("/")
//    public String index() {
//        return "/build/index.html";
//    }

    @Test
    public StringBuilder geo2server() throws IOException {
        StringBuilder json = new StringBuilder();


        Map<String, Object> param = new HashMap<String,Object>();
//				JSONObject param = new JSONObject();
        param.put("BBOX", "113.493551,33.745379,124.001,38.844582");
        param.put("EXCEPTIONS", "application/vnd.ogc.se_xml");
        param.put("FEATURE_COUNT", "50");
        param.put("HEIGHT", "330");
        param.put("INFO_FORMAT", "text/html");
        param.put("Layers", "shandong:sandong_P");
        param.put("QUERY_LAYERS", "shandong:sandong_P");
        param.put("REQUEST", "GetFeatureInfo");
        param.put("SERVICE", "WMS");
        param.put("format", "image/png");
        param.put("WIDTH", "680");
        param.put("srs", "EPSG:4326");
        param.put("version", "1.1.1");
        param.put("y", "258");
        param.put("x", "248");
        ObjectMapper mapper = new ObjectMapper();
        String jsons = mapper.writeValueAsString(param);
        System.out.println(jsons);
        String url = "http://192.168.8.254:8080/geoserver/wfs?"+jsons;
        URL newUrl = new URL(url);
        HttpClient hc = new HttpClient();

        HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        String inputLine = null;
        while ( (inputLine = in.readLine()) != null) {
            json.append(inputLine);
        }
        in.close();
        /*params.add(new BasicNameValuePair("BBOX", "113.493551,33.745379,124.001,38.844582"));
        params.add(new BasicNameValuePair("EXCEPTIONS", "application/vnd.ogc.se_xml"));
        params.add(new BasicNameValuePair("FEATURE_COUNT", "50"));
        params.add(new BasicNameValuePair("HEIGHT", "330"));
        params.add(new BasicNameValuePair("INFO_FORMAT", "text/html"));
        params.add(new BasicNameValuePair("Layers", "shandong:sandong_P"));
        params.add(new BasicNameValuePair("QUERY_LAYERS", "shandong:sandong_P"));
        params.add(new BasicNameValuePair("REQUEST", "GetFeatureInfo"));
        params.add(new BasicNameValuePair("SERVICE", "WMS"));
        params.add(new BasicNameValuePair("format", "image/png"));
        params.add(new BasicNameValuePair("WIDTH", "680"));
        params.add(new BasicNameValuePair("srs", "EPSG:4326"));
        params.add(new BasicNameValuePair("version", "1.1.1"));
        params.add(new BasicNameValuePair("y", "258"));
        params.add(new BasicNameValuePair("x", "248"));*/


//        String response = hc.httpPost(Ip, port, "/geoserver/shandong/wfs", params);
        return json;
    }


    @GetMapping("/swagger")
    public String swaggerIndex() {
        System.out.println("swagger-ui.html");
        return "redirect:swagger-ui.html";
    }
}
