package it.geosolutions.swgeoserver.entry;

import java.io.Serializable;
import java.util.List;

public class TablesEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4244019469184509158L;

	/**
	 * 表名
	 */
	public String tableName;

	/**
	 * 属性表的表名
	 */
	public String attTableName;

	/**
	 * 列集合
	 */
	public List<String> columnList;

	/**
	 * 值集合
	 */
	public List<String> valueList;

	/**
	 * 更新时用到的 格式是: KEY = VALUE
	 */
	public String sqlStr = "";

	/**
	 * 关键字段 例如主键
	 */
	public String keyfieldname = "";

	/**
	 * 主键值
	 */
	public String keyfieldvalue = "";

	/**
	 * WHERE条件
	 */
	public String whereStr = "";

	/**
	 * 当前层级
	 */
	public int currentLevel;
	/**
	 * 需显示的属性字段名
	 */
	public String showfieldname = "";

	/**
	 * 是否需要显示属性
	 */
	public String oldshowfieldname = "";

	/**
	 * 标识字段
	 */
	public String flag = "";
	
	int startindex;
	
	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	int pagesize;

	/**
	 * 多媒体信息表名
	 */
	public String multimediaTableName = "";

	public String tabId = "";

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getAttTableName() {
		return attTableName;
	}

	public void setAttTableName(String attTableName) {
		this.attTableName = attTableName;
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public List<String> getValueList() {
		return valueList;
	}

	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}

	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}

	public String getKeyfieldname() {
		return keyfieldname;
	}

	public void setKeyfieldname(String keyfieldname) {
		this.keyfieldname = keyfieldname;
	}

	public String getKeyfieldvalue() {
		return keyfieldvalue;
	}

	public void setKeyfieldvalue(String keyfieldvalue) {
		this.keyfieldvalue = keyfieldvalue;
	}

	public String getWhereStr() {
		return whereStr;
	}

	public void setWhereStr(String whereStr) {
		this.whereStr = whereStr;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getShowfieldname() {
		return showfieldname;
	}

	public void setShowfieldname(String showfieldname) {
		this.showfieldname = showfieldname;
	}

	public String getMultimediaTableName() {
		return multimediaTableName;
	}

	public void setMultimediaTableName(String multimediaTableName) {
		this.multimediaTableName = multimediaTableName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOldshowfieldname() {
		return oldshowfieldname;
	}

	public void setOldshowfieldname(String oldshowfieldname) {
		this.oldshowfieldname = oldshowfieldname;
	}

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
}
