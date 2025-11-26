/**
 *
 */
package com.xiugou.x1.game.server;

import java.util.Collections;

import org.gaming.fakecmd.side.game.GameCmdManager;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.schedule.ScheduleManager;
import org.gaming.ruler.spring.Spring;
import org.gaming.tool.ConsoleUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.xiugou.x1.game.server.foundation.starting.ConfigLifecycle;
import com.xiugou.x1.game.server.foundation.starting.ProjectBooter;

/**
 * @author YY
 *
 */
@ComponentScan(basePackages = {"org.gaming", "com.xiugou", "pojo.xiugou"})
@SpringBootApplication
public class X1GameServer {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication();
        //服务器启动引导
        springApplication.addListeners(new ProjectBooter());

        springApplication.addPrimarySources(Collections.singletonList(X1GameServer.class));
        springApplication.run(args);

        ConsoleUtil.addFunction("stop", () -> { System.exit(0); });
        ConsoleUtil.addFunction("printConsoleFunction", () -> { ConsoleUtil.printFunction();});
        ConsoleUtil.addFunction("printEventTrace", () -> {EventBus.printTrace();});
        ConsoleUtil.addFunction("printCmd", () -> { GameCmdManager.printCmd(); });
        ConsoleUtil.addFunction("printSchedule", () -> { ScheduleManager.print();});
        ConsoleUtil.addFunction("reload", () -> { Spring.getBean(ConfigLifecycle.class).reloadConfig();});
        ConsoleUtil.inputListening();
    }
}
