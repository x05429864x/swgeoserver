package com.siweidg.swgeoserver.comm.utils;

/**
 * 数据结果封装
 *
 * @author lzy
 *
 * @param <T>
 */
public class ResultDataUtil<T> {

	/**
	 * 返回的数据，默认为空
	 */
	private T data;

	/**
	 * INT 返回的标识
	 */
	private int code = 200;

	/**
	 * 提示信息
	 */
	private String msg;

	/**
	 * STRING 返回的标识
	 */
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(!"0".equals(status)){
			success = false;
		}
		this.status = status;
	}

	private Boolean success = true;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		if (200 != code) {
			success = false;
		}
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

}
