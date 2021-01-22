package com.siweidg.swgeoserver.comm.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * \* User: x
 * \* Date: 2020/8/20
 * \* Time: 12:21
 * \* Description:
 * \
 */
public class JsonUtils {

    /**
     * 格式化json
     * @param jsonString
     * @return
     */
    public static String jsonFormat(String jsonString) {
        JSONObject object= JSONObject.parseObject(jsonString);
        jsonString = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        return jsonString;
    }

    /**
     * 格式化json
     * @param jsonString json
     * @param str key
     * @return
     */
    public static JSONArray FormatToArray(String jsonString, String str) {
        JSONObject object= JSONObject.parseObject(jsonString);
        JSONArray obj = object.getJSONArray(str);
        return obj;
    }


    public static JSONObject FormatToJson(String jsonString) {
        JSONObject object= JSONObject.parseObject(jsonString);
        return object;
    }
}
