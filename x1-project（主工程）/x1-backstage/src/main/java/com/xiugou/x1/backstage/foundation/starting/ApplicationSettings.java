/**
 * 
 */
package com.xiugou.x1.backstage.foundation.starting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author YY
 *
 */
@Configuration
public class ApplicationSettings {


	//#后台服务器的tomcat端口
	@Value("${server.port:9001}")
	protected int serverPort;
	//#服务器是否调试模式
	@Value("${server.debug.mode:true}")
	protected boolean serverDebugMode;
	//#是否为后台主服务器
	@Value("${server.backstage.main:true}")
	protected boolean serverBackstageMain;
	//#热更配置文件夹
	@Value("${fix.upload.design.folder:/uploadfiles/design/}")
	protected String fixUploadDesignFolder;
	//#热更代码文件夹
	@Value("${fix.upload.clazzs.folder:/uploadfiles/clazzs/}")
	protected String fixUploadClazzsFolder;
	//#后台加密秘钥
	@Value("${backstage.key:xiugouyouxi2023}")
	protected String backstageKey;
	//#主后台的地址
	@Value("${backstage.main.url:http://127.0.0.1:9001}")
	protected String backstageMainUrl;
	//#数据库配置
	@Value("${database.config.file:file:./config/database.db}")
	protected String databaseConfigFile;
	//#日志输出格式配置
	@Value("${logging.config:file:./config/log4j2.xml}")
	protected String loggingConfig;
	//#===================redis基础配置===================
	@Value("${spring.redis.database:3}")
	protected int springRedisDatabase;
	@Value("${spring.redis.host:192.168.1.47}")
	protected String springRedisHost;
	@Value("${spring.redis.password:xiugou2023}")
	protected String springRedisPassword;
	@Value("${spring.redis.port:6379}")
	protected int springRedisPort;
	//# 连接超时时间 单位 ms（毫秒）
	@Value("${spring.redis.timeout:3000}")
	protected int springRedisTimeout;


	public int getServerPort() {
		return serverPort;
	}
	public boolean isServerDebugMode() {
		return serverDebugMode;
	}
	public boolean isServerBackstageMain() {
		return serverBackstageMain;
	}
	public String getDatabaseConfigFile() {
		return databaseConfigFile;
	}
	public String getLoggingConfig() {
		return loggingConfig;
	}
	public int getSpringRedisDatabase() {
		return springRedisDatabase;
	}
	public String getSpringRedisHost() {
		return springRedisHost;
	}
	public String getSpringRedisPassword() {
		return springRedisPassword;
	}
	public int getSpringRedisPort() {
		return springRedisPort;
	}
	public int getSpringRedisTimeout() {
		return springRedisTimeout;
	}
	public String getFixUploadDesignFolder() {
		return fixUploadDesignFolder;
	}
	public String getFixUploadClazzsFolder() {
		return fixUploadClazzsFolder;
	}
	public String getBackstageKey() {
		return backstageKey;
	}
	public String getBackstageMainUrl() {
		return backstageMainUrl;
	}
}
