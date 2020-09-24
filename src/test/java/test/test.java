package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

/**
 * \* User: x
 * \* Date: 2020/8/20
 * \* Time: 11:21
 * \* Description:
 * \
 */
public class test {
    //编写方法
    public static String jsonFormat(String jsonString) {
        JSONObject object= JSONObject.parseObject(jsonString);
        JSONArray roles = object.getJSONArray("roles");
        System.out.println(roles);
        jsonString = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return jsonString;
    }
    @Test
    public void form(){
        String a = "{\n\t\"roles\":[\n\t\t\"ADMIN\",\n\t\t\"GROUP_ADMIN\",\n\t\t\"test_r\"\n\t]\n}";
        String b = "{\"roles\":[\"ADMIN\",\"GROUP_ADMIN\",\"test_r\"]}";
        a = jsonFormat(a);
        b = jsonFormat(b);
        System.out.println(a);
        System.out.println(b);
    }
}
