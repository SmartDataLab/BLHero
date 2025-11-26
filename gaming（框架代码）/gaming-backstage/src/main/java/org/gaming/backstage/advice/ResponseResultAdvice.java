/**
 * 
 */
package org.gaming.backstage.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author YY
 *
 */
@RestController
@RestControllerAdvice
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

	private static Logger logger = LoggerFactory.getLogger(ResponseResultAdvice.class);

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		if(returnType.getParameterType() == ResponseResult.class) {
			return false;
		}
		if(returnType.getParameterType() == String.class) {
			return false;
		}
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		ResponseResult result = new ResponseResult();
		result.setCode(0);
		result.setMessage("SUCCESS");
		result.setData(body);
		return result;
	}

	@ResponseBody
	@ExceptionHandler(value = BusinessException.class)
	public ResponseResult exceptionHandler(BusinessException e) {
		ResponseResult result = new ResponseResult();
		result.setCode(e.getCode());
		result.setMessage(e.getMessage());
		result.setData("");
		logger.error(e.getMessage(), e.getParams());
		return result;
	}
}
