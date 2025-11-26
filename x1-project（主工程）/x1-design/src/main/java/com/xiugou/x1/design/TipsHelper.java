/**
 * 
 */
package com.xiugou.x1.design;

import java.lang.reflect.InvocationTargetException;

import org.gaming.design.exception.DesignNotFoundException;
import org.gaming.prefab.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.LanguageTipsCache;

import pb.xiugou.x1.protobuf.tips.Tips.TipsMessage;

/**
 * @author YY
 *
 */
public class TipsHelper {

	public static String language = "cn";
	
	private static Logger logger = LoggerFactory.getLogger(TipsHelper.class);
	
	public static TipsMessage onException(int cmd, Message message, Exception e) {
		TipsMessage.Builder builder = TipsMessage.newBuilder();

		if (e instanceof BusinessException) {
			BusinessException exception = (BusinessException) e;
			String messagePrint = TextFormat.printer().shortDebugString(message);
			logger.error(String.format("处理请求【%s】时发生异常，请求参数【%s】，系统异常描述【%s】", cmd, messagePrint, exception.getMessage()), exception);

			String text = LanguageTipsCache.ins().getText(language, "" + exception.getExceptionCause().getCode());
			
			builder.setCode(exception.getExceptionCause().getCode());
			builder.setDevTips(text);
			for(Object param : exception.getParams()) {
				builder.addParams(param.toString());
			}
		} else if (e.getCause() instanceof BusinessException) {
			BusinessException exception = (BusinessException) e.getCause();
			String messagePrint = TextFormat.printer().shortDebugString(message);
			logger.error(String.format("处理请求【%s】时发生异常，请求参数【%s】，系统异常描述【%s】", cmd, messagePrint, exception.getMessage()), exception);

			String text = LanguageTipsCache.ins().getText(language, "" + exception.getExceptionCause().getCode());
			
			builder.setCode(exception.getExceptionCause().getCode());
			builder.setDevTips(text);
			for(Object param : exception.getParams()) {
				builder.addParams(param.toString());
			}
		} else if (e instanceof DesignNotFoundException) {
			DesignNotFoundException exception = (DesignNotFoundException) e;
			String messagePrint = TextFormat.printer().shortDebugString(message);
			logger.error(String.format("处理请求【%s】时发生异常，请求参数【%s】，系统异常描述【%s】", cmd, messagePrint, exception.getMessage()), exception);

			String text = LanguageTipsCache.ins().getText(language, "" + TipsCode.DESIGN_NOT_FOUND.getCode());
			
			builder.setCode(TipsCode.DESIGN_NOT_FOUND.getCode());
			builder.setDevTips(text);

		} else if (e.getCause() instanceof DesignNotFoundException) {
			DesignNotFoundException exception = (DesignNotFoundException) e.getCause();
			String messagePrint = TextFormat.printer().shortDebugString(message);
			logger.error(String.format("处理请求【%s】时发生异常，请求参数【%s】，系统异常描述【%s】", cmd, messagePrint, exception.getMessage()), exception);

			String text = LanguageTipsCache.ins().getText(language, "" + TipsCode.DESIGN_NOT_FOUND.getCode());
			
			builder.setCode(TipsCode.DESIGN_NOT_FOUND.getCode());
			builder.setDevTips(text);

		} else if (e instanceof InvocationTargetException) {
			Throwable throwable = e.getCause();
			String messagePrint = TextFormat.printer().shortDebugString(message);
			logger.error(String.format("处理请求【%s】时发生异常，请求参数【%s】，系统异常描述【%s】", cmd, messagePrint, throwable.getMessage()), throwable);
			
			String text = LanguageTipsCache.ins().getText(language, "" + TipsCode.REQUEST_FAILED.getCode());
			
			builder.setCode(TipsCode.REQUEST_FAILED.getCode());
			builder.setDevTips(text);
			
		} else {
			String messagePrint = TextFormat.printer().shortDebugString(message);
			logger.error(String.format("处理请求【%s】时发生异常，请求参数【%s】，系统异常描述【%s】", cmd, messagePrint, e.getMessage()), e);

			String text = LanguageTipsCache.ins().getText(language, "" + TipsCode.REQUEST_FAILED.getCode());
			
			builder.setCode(TipsCode.REQUEST_FAILED.getCode());
			builder.setDevTips(text);
			
		}
		return builder.build();
	}
	
	public static void onException(Exception e) {
		if (e instanceof BusinessException) {
			BusinessException exception = (BusinessException) e;
			logger.error(exception.getMessage(), exception);

		} else if (e.getCause() instanceof BusinessException) {
			BusinessException exception = (BusinessException) e.getCause();
			logger.error(exception.getMessage(), exception);
			
		} else if (e instanceof DesignNotFoundException) {
			DesignNotFoundException exception = (DesignNotFoundException) e;
			logger.error(exception.getMessage(), exception);

		} else if (e.getCause() instanceof DesignNotFoundException) {
			DesignNotFoundException exception = (DesignNotFoundException) e.getCause();
			logger.error(exception.getMessage(), exception);

		} else if (e instanceof InvocationTargetException) {
			Throwable throwable = e.getCause();
			logger.error(throwable.getMessage(), throwable);
		} else {
			logger.error(e.getMessage(), e);
		}
	}
}
