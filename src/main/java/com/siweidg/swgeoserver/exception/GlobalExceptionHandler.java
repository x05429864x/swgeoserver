package com.siweidg.swgeoserver.exception;

import com.siweidg.swgeoserver.comm.utils.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

/**
 * 异常增强，以JSON的形式返回给客服端
 * 异常增强类型：NullPointerException,RunTimeException,ClassCastException,
 * NoSuchMethodException,IOException,IndexOutOfBoundsException
 * 以及springmvc自定义异常等，如下： SpringMVC自定义异常对应的status code
 * 
 * Exception HTTP Status Code ConversionNotSupportedException 500 (Internal
 * Server Error) HttpMessageNotWritableException 500 (Internal Server Error)
 * HttpMediaTypeNotSupportedException 415 (Unsupported Media Type)
 * HttpMediaTypeNotAcceptableException 406 (Not Acceptable)
 * HttpRequestMethodNotSupportedException 405 (Method Not Allowed)
 * NoSuchRequestHandlingMethodException 404 (Not Found) TypeMismatchException
 * 400 (Bad Request) HttpMessageNotReadableException 400 (Bad Request)
 * MissingServletRequestParameterException 400 (Bad Request)
 *
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
	static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	
//	@ExceptionHandler(RuntimeException.class)
//	@ResponseBody
//	public Object runtimeExceptionHandler(RuntimeException runtimeException) {
//		logger.error(runtimeException);
//		runtimeException.printStackTrace();
//		return ReturnFormat.retParam(1000, null);
//	}
	
	// 空指针异常
	@ExceptionHandler(NullPointerException.class)
	@ResponseBody
	public Object nullPointerExceptionHandler(NullPointerException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(1001, null);
	}

	// 类型转换异常
	@ExceptionHandler(ClassCastException.class)
	@ResponseBody
	public Object classCastExceptionHandler(ClassCastException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(1002, null);
	}

	// IO异常
	@ExceptionHandler(IOException.class)
	@ResponseBody
	public Object iOExceptionHandler(IOException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(1003, null);
	}

	// 未知方法异常
	@ExceptionHandler(NoSuchMethodException.class)
	@ResponseBody
	public Object noSuchMethodExceptionHandler(NoSuchMethodException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(1004, null);
	}

	// 数组越界异常
	@ExceptionHandler(IndexOutOfBoundsException.class)
	@ResponseBody
	public Object indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(1005, null);
	}

	// 400错误
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	@ResponseBody
	public Object requestNotReadable(HttpMessageNotReadableException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(400, null);
	}

	// 400错误
	@ExceptionHandler({ TypeMismatchException.class })
	@ResponseBody
	public Object requestTypeMismatch(TypeMismatchException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(400, null);
	}

	// 400错误
	@ExceptionHandler({ MissingServletRequestParameterException.class })
	@ResponseBody
	public Object requestMissingServletRequest(MissingServletRequestParameterException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(400, null);
	}

	// 405错误
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	@ResponseBody
	public Object request405(HttpRequestMethodNotSupportedException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(405, null);
	}

	// 406错误
	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
	@ResponseBody
	public Object request406(HttpMediaTypeNotAcceptableException ex) {
		logger.error(ex);
		return ReturnFormat.retParam(406, null);
	}

	// 500错误
	@ExceptionHandler({ ConversionNotSupportedException.class, HttpMessageNotWritableException.class })
	@ResponseBody
	public Object server500(RuntimeException runtimeException) {
		logger.error(runtimeException);
		return ReturnFormat.retParam(500, null);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public Object MaxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException runtimeException) {
		logger.error(runtimeException);
		return ReturnFormat.retParam(2031, null);
	}

	@ExceptionHandler(BadSqlGrammarException.class)
	@ResponseBody
	public Object badSqlGrammarExceptionHandler(BadSqlGrammarException badSqlGrammarException) {
		logger.error(badSqlGrammarException);
		return ReturnFormat.retParam(9999,null);
	}

}
