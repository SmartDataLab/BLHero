/**
 *
 */
package com.xiugou.x1.backstage;

import java.util.Collections;

import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.schedule.ScheduleManager;
import org.gaming.tool.ConsoleUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.xiugou.x1.backstage.foundation.starting.ProjectBooter;

/**
 * @author YY
 */
@ComponentScan(basePackages = { "com.xiugou", "org.gaming" })
@SpringBootApplication
public class X1BackStage {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication();
		// 服务器启动引导
		springApplication.addListeners(new ProjectBooter());

		springApplication.addPrimarySources(Collections.singletonList(X1BackStage.class));
		springApplication.run(args);

		ConsoleUtil.addFunction("stop", () -> { System.exit(0); });
		ConsoleUtil.addFunction("printConsoleFunction", ConsoleUtil::printFunction);
		ConsoleUtil.addFunction("printEventTrace", EventBus::printTrace);
		ConsoleUtil.addFunction("printSchedule", () -> { ScheduleManager.print();});
		ConsoleUtil.inputListening();
	}
}
