/**
 * 
 */
package org.gaming.fakecmd.side.game;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.gaming.fakecmd.cmd.AbstractCmdRegister;
import org.gaming.fakecmd.side.common.StringKeyCmdInvoker;

/**
 * @author YY
 *
 */
public class PlayerGmCmdRegister extends AbstractCmdRegister<String, StringKeyCmdInvoker> {

	public static PlayerGmCmdRegister INS = new PlayerGmCmdRegister();
	
	private PlayerGmCmdRegister() {}
	
	@Override
	protected Class<? extends Annotation> annotationClass() {
		return PlayerGmCmd.class;
	}
	
	@Override
	protected Class<?>[] methodParamsType() {
		return new Class<?>[] { IPlayerContext.class, String[].class };
	}

	@Override
	protected StringKeyCmdInvoker registerMethod(Object bean, Method method) {
		try {
			PlayerGmCmd playerGmCmd = method.getAnnotation(PlayerGmCmd.class);
			return new StringKeyCmdInvoker(playerGmCmd.command(), bean, method);
		} catch (Exception e) {
			logger.error("类{}中的协议函数{}定义异常", bean.getClass().getSimpleName(), method.getName());
			throw new RuntimeException(e);
		}
	}
}
