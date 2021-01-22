package com.siweidg.swgeoserver.comm.init;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.servlet.ServletConfig;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置文件初始化
 * 
 *
 * @copyRight SIWEIDG 北京航天世景信息技术有限公司
 */
public final class Constants {

	public static final String SESSION_INFO = "sessionInfo";

	public static final String INITIAL_PASSWORD = "123456";
	
	public static final String TOKEN = "fe089800-b1af-11e9-a888-4303e3c9c481";
	public static final String BASE_ADDRESS = "http://172.20.22.10:8080/api/api/v1";

	public static final String COMPRESSED_FILES_ZIP = ".zip";
	public static final String COMPRESSED_FILES_RAR = ".rar";

	public static final String PG_HOST = "192.168.8.254";
	public static final String PG_USERNAME = "postgres";
	public static final String PG_PWD = "postgres";
	public static final String PG_DB = "postgres";
	public static final String SCHEMA = "public";
	public static final String DBTYPE = "postgis";
	public static final int PG_PROT = 5432;


	private static Configuration emailConfig;
	public final static Map<String, String> header = new HashMap<String,String>();
	private static Configuration config;
	public static Map<String, Object> cacheDB = new LinkedHashMap<String, Object>();
	static {
		try {
			config = new PropertiesConfiguration("siweidg.properties");
			emailConfig = new PropertiesConfiguration(
					"siweidg_email.properties");
			header.put("token",Constants.TOKEN);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	public static final String EMAIL_HOST_NAME = emailConfig
			.getString("host_name");
	public static final String EMAIL_USER_NAME = emailConfig
			.getString("user_name");
	public static final String EMAIL_PASS_WORD = emailConfig
			.getString("pass_word");
	public static final String EMAIL_FROM_EMAIL = emailConfig
			.getString("fromEmail");
	public static final String EMAIL_COMPANY_NAME = emailConfig
			.getString("company_name");
	
	public static final String apache_path = config.getString("apache_path");
	
	/**
	 * 项目端口控制 (apache和socket端口)
	 */
	public static final int APACHE_PORT = config.getInt("apache_port");
	public static final int ATTA_SOCKET_PORT = config.getInt("atta_socket_port");
	public static final int MAP_SOCKET_PORT = config.getInt("map_socket_port");
	
	public static enum LOG_TYPE{
		UPDATE("update"),INSERT("insert"),DELETE("delete"),SELECT("select");
		public String name;
		private LOG_TYPE(String name) {
			// TODO Auto-generated constructor stub
			this.name =  name;
		}
	}
	
	public Constants(ServletConfig servletConfig) {
	}
	
}
