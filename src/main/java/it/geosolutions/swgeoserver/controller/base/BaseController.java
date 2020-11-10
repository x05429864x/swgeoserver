package it.geosolutions.swgeoserver.controller.base;

import it.geosolutions.swgeoserver.comm.utils.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	protected ResultDataUtil<Object> resultDataUtil = new ResultDataUtil<Object>();
	protected GeoJsonUtil geoJsonUtil = new GeoJsonUtil();



	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return request;
	}

	/**
	 * 得到32位的uuid
	 *
	 * @return
	 */
	public String get32UUID() {

		return UuidUtil.get32UUID();
	}

}
