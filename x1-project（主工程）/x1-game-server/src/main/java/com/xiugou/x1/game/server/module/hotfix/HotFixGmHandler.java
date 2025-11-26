/**
 * 
 */
package com.xiugou.x1.game.server.module.hotfix;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.gaming.fakecmd.annotation.PlayerGmCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.hotfix.api.FixCodeTestA;

/**
 * @author YY
 *
 */
@Controller
public class HotFixGmHandler {

	private static Logger logger = LoggerFactory.getLogger(HotFixGmHandler.class);
	
	@Autowired
	private FixCodeTestA fixCodeTestA;
	
	@PlayerGmCmd(command = "HOT_FIX")
	public void test1(PlayerContext playerContext, String[] params) {
		
		String path = "E:\\Workspace\\Git\\x1-server\\x1-project\\x1-game-server\\uploadfiles\\FC123456";
		try {
			RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
			String name = runtime.getName();
			System.out.println("当前进程的标识为：" + name);
			int index = name.indexOf("@");
			
			String pid = name.substring(0, index);
			System.out.println("当前进程的PID为：" + pid);
			
//			Agent.hotfix(pid, path);
	        fixCodeTestA.test();
		} catch (Exception e) {
			logger.error("热更异常", e);
		}
	}
	
	@PlayerGmCmd(command = "HOT_FIX_TEST")
	public void test2(PlayerContext playerContext, String[] params) {
		fixCodeTestA.test();
	}
}
