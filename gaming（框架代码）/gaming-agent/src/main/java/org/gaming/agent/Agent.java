package org.gaming.agent;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.attach.VirtualMachine;

/**
 * 热更的实现方式
 * 1、如果工程是maven工程，则需要在pom.xml文件中加入依赖
 * 		<dependency>
 *			<groupId>jdk.tools</groupId>
 *			<artifactId>jdk.tools</artifactId>
 *			<version>1.8</version>
 *			<scope>system</scope>
 *			<systemPath>${JAVA_HOME}/lib/tools.jar</systemPath>
 *		</dependency>
 *   并在打包配置中加入
 *   	<build>
 *			<plugins>
 *				<plugin>
 *					<groupId>org.apache.maven.plugins</groupId>
 *					<artifactId>maven-jar-plugin</artifactId>
 *					<version>3.1.0</version>
 *					<configuration>
 *						<archive>
 *							<manifestEntries>
 *								<Premain-Class>org.gaming.agent.Agent</Premain-Class>
 *								<Agent-Class>org.gaming.agent.Agent</Agent-Class>
 *								<Can-Retransform-Classes>true</Can-Retransform-Classes>
 *								<Can-Redefine-Classes>true</Can-Redefine-Classes>
 *							</manifestEntries>
 *						</archive>
 *					</configuration>
 *				</plugin>
 *			</plugins>
 *		</build>
 *    其中Agent-Class、Premain-Class为Agent类的包含package路径的名称
 * 2、在Agent类中定义premain、agentmain函数，其中
 * 		premain函数需要被声明，但不一定需要实现
 * 		agentmain函数中可实现用于热更代码的逻辑
 * 3、gaming-agent需要被打包成jar
 * 4、热更需要知道当前应用的进程ID与将要热更的文件所在的目录路径
 * 5、在项目中遇到过服务器运行一段时间后无法获得进程ID的情况，所以尽量在服务器启动时获取进程ID，并保存在静态变量中
 * 6、在启动应用时需要在java命令中加入-javaagent:./jar/gaming-agent-1.0.0.jar参数
 * 		如java -javaagent:./jar/gaming-agent-1.0.0.jar -XX:+HeapDumpOnOutOfMemoryError -Xms1024M -Xmx2048M -classpath ./common-libs/*;./jar/*;./config/*; com.xiugou.x1.game.server.X1GameServer
 * 7、需要注意在windows环境跟在linux环境下，jdk的tools.jar是不一样的，
 * 		如果服务器代码最终运行环境是windows系统，则需要确保jdk/bin目录下存在attach.dll文件
 * 		如果jdk的bin目录下不存在attach.dll文件，则需要从jdk/jre/bin目录下复制过去
 * 		如果服务器代码最终运行环境是linux系统，则最好在linux环境下进行打包，
 * 		如果不能在linux环境下打包，则可以把linux系统中安装的jdk中的tools.jar手动拷贝到程序加载jar的目录下
 */
public class Agent {

	private static Logger logger = LoggerFactory.getLogger(Agent.class);

	/**
	 * jvm未启动前调用
	 *
	 * @param agentArgs
	 * @param inst
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		// 需要有此函数的现实
	}

	/**
	 * agent调用
	 *
	 * @param patchClassPath
	 * @param inst
	 */
	public static void agentmain(String patchClassPath, Instrumentation inst) {
		logger.info("将要热更{}下的代码文件", patchClassPath);
		Set<String> fileNames = listFilePath(patchClassPath);
		for (String fileName : fileNames) {
			try {
				logger.info("加载代码文件{}", fileName);
				File file = new File(fileName);
				byte[] data = readFile(file);
				String className = getClassName(fileName);
				Class<?> theClass = Class.forName(className);
				ClassDefinition classDefinition = new ClassDefinition(theClass, data);
				inst.redefineClasses(classDefinition);
				logger.info(String.format("%s热更新成功", className));
			} catch (Exception e) {
				logger.error(String.format("%s热更新失败", fileName), e);
			}
		}
	}

	/**
	 * 获取要加载 的class文件名
	 *
	 * @param name
	 *            String
	 * @return String
	 */
	private static String getClassName(String name) {
		int index = name.lastIndexOf(File.separator);
		if (index == -1) {
			return name;
		} else {
			return name.substring(index + 1, name.lastIndexOf('.'));
		}
	}

	/**
	 * 获取文件目录下所有的文件
	 * 
	 * @param fileDir
	 * @return
	 */
	public static Set<String> listFilePath(String fileDir) {
		Set<String> fileNames = new HashSet<>();
		File file = new File(fileDir);
		listFilePath(file, fileNames);
		return fileNames;
	}

	private static void listFilePath(File file, Set<String> fileNames) {
		if (file.isDirectory()) {
			for (File tempFile : file.listFiles()) {
				listFilePath(tempFile, fileNames);
			}
		} else {
			fileNames.add(file.getPath());
		}
	}

	/**
	 * 读取文件的字节
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] readFile(File file) {
		if (file.isDirectory()) {
			return new byte[0];
		}
		BufferedInputStream bis = null;
		byte[] data = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int c = 0;
		byte[] buffer = new byte[8 * 1024];
		try {
			while ((c = bis.read(buffer)) != -1) {
				baos.write(buffer, 0, c);
				baos.flush();
			}
			data = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 热更
	 * @param processId
	 * @param patchPath
	 */
	public static void hotfix(String processId, String patchPath) {
		try {
			VirtualMachine vm = VirtualMachine.attach(processId);
			vm.loadAgent(getAgentPath(), patchPath);
			vm.detach();
		} catch (Exception e) {
			logger.error("热更失败", e);
		}
	}
	
	/**
	 * 获取进程ID
	 * @return
	 */
	public static String getProcessId() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName();
		int index = name.indexOf("@");
		return name.substring(0, index);
	}

	/**
	 * Agent工程在被打包成jar后，通过此方式可以获得其所在jar的路径
	 * @return
	 */
	private static String getAgentPath() {
		try {
			File agentJar = new File(Agent.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			return agentJar.getAbsolutePath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
}
