package it.geosolutions.swgeoserver.exception;

import it.geosolutions.swgeoserver.comm.utils.ResultDataUtil;

import java.util.HashMap;
import java.util.Map;

//格式化返回客户端数据格式（json）
public class ReturnFormat {
	private static Map<String, String> messageMap = new HashMap<String, String>();
	// 初始化状态码与文字说明
	static {
		messageMap.put("0", "成功");
		messageMap.put("1", "失败");

		messageMap.put("400", "Bad Request!");
		messageMap.put("401", "NotAuthorization");
		messageMap.put("405", "Method Not Allowed");
		messageMap.put("406", "Not Acceptable");
		messageMap.put("500", "Internal Server Error");

		messageMap.put("1000", "[服务器]运行时异常");
		messageMap.put("1001", "[服务器]空值异常");
		messageMap.put("1002", "[服务器]数据类型转换异常");
		messageMap.put("1003", "[服务器]IO异常");
		messageMap.put("1004", "[服务器]未知方法异常");
		messageMap.put("1005", "[服务器]数组越界异常");
		messageMap.put("1006", "[服务器]网络异常");

		messageMap.put("2010", "缺少参数或值为空");
		messageMap.put("2011", "创建表失败");
		messageMap.put("2012", "确认以下内容是否都有 startlevel,endlevel,simplify,classtype,centerx,centery");
		messageMap.put("2013", "上传文件重复");
		messageMap.put("2014", "保存图形信息失败");
		messageMap.put("2015", "保存图形属性失败");
		messageMap.put("2016", "保存多媒体信息失败");
		messageMap.put("2017", "上传文件错误");
		messageMap.put("2018", "任务保存失败");

		messageMap.put("2019", "上传文件格式错误(zip/rar)");
		messageMap.put("2020", "无效的Token");
		messageMap.put("2021", "无操作权限");
		messageMap.put("2023", "请重新登录");
		messageMap.put("2024", "历史信息保存失败");
		messageMap.put("2029", "参数不合法");

		messageMap.put("2030", "数据已存在");
		messageMap.put("2031", "上传文件过大");
		messageMap.put("2032", "中文名称已存在");
		messageMap.put("2033", "删除图层失败");

		messageMap.put("3001", "有数据");
		messageMap.put("3002", "没有数据");
		messageMap.put("3003", "权限名已经存在");
		messageMap.put("3004", "该组不为空，请确认后在删除");
		messageMap.put("3005", "没有资源数据");
		messageMap.put("3006", "只有<待执行>和<重执行>的任务可以删除!");

		messageMap.put("4000", "工作区已存在!");
		messageMap.put("4001", "数据存储已存在!");
		messageMap.put("4002", "图层已存在!");
		messageMap.put("4003", "图层不存在!");
		messageMap.put("4004", "样式已存在!");

		messageMap.put("9000", "用户名或是密码错误");
		messageMap.put("9001", "不能同时执行多个任务");
		messageMap.put("9999", "数据库错误");
		
		messageMap.put("10000", "续传");
	}

	public static Object retParam(int status, Object data) {
		ResultDataUtil<Object> json = new ResultDataUtil<Object>();
		json.setStatus(String.valueOf(status));
		json.setMsg(messageMap.get(String.valueOf(status)));
		json.setData(data);
		return json;
	}

	public static Object retParam(int status, Object data, Map<String, String> map) {
		ResultDataUtil<Object> json = new ResultDataUtil<Object>();
		json.setStatus(String.valueOf(status));
		json.setMsg(messageMap.get(String.valueOf(status)));
		json.setData(data);
		return json;
	}

	public static Object retParam(int status, Object data,String msg) {
		ResultDataUtil<Object> json = new ResultDataUtil<Object>();
		json.setStatus(String.valueOf(status));
		json.setMsg(msg);
		json.setData(data);
		return json;
	}
}
