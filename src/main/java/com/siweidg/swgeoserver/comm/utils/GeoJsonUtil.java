package com.siweidg.swgeoserver.comm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siweidg.swgeoserver.entry.Describe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 此类是spring mvc 默认的 json 处理里用的。这个项目中用到的是现在比较流行的阿里的 fastjson 做的处理， 默认的处理还是保留
 * 
 * @author lzy
 *
 */
public class GeoJsonUtil {

	static ObjectMapper objectMapper = null;

	public GeoJsonUtil() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
	}

	// public static String map2Json(Map<String, Object> kv) {
	// try {
	// return objectMapper.writeValueAsString(kv);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return "";
	// }
	// public static String List2Json(List<Object> kv) {
	// try {
	// return objectMapper.writeValueAsString(kv);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return "";
	// }
	/**
	 * Object 转 Json
	 * 
	 * @param o
	 * @return
	 */
	public String Obj2Json(Object o) {
		try {
			return objectMapper.writeValueAsString(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * String 转 Object
	 * 
	 * @param kv
	 * @param valueType
	 * @return
	 */
	public <T> T string2Obj(String kv, Class<T> valueType) {
		try {
			return objectMapper.readValue(kv, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public List<Describe> StrtoList(String str) {
		JavaType javaType = getCollectionType(ArrayList.class, Describe.class);
		List<Describe> lst = null;
		try {
			lst = (List<Describe>) objectMapper.readValue(str, javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lst;
	}

	public <T>  T string2Obj(String jsonarray, TypeReference<T> jsonTypeReference) throws Exception {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Map.class);
		return (T) objectMapper.readValue(jsonarray, javaType);
	}
}
