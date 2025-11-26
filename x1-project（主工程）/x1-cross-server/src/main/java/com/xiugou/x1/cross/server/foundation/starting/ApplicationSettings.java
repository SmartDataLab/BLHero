/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.starting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @author YY
 *
 */
@Configuration
public class ApplicationSettings {



	//#跨服服务器ID
	@Value("${cross.server.id:1}")
	protected int crossServerId;
	//#平台编号
	@Value("${cross.server.platformid:1001}")
	protected int crossServerPlatformid;
	//#跨服服务器的tcp端口
	@Value("${corss.server.port:41001}")
	protected int corssServerPort;
	//#跨服服务器的tomcat端口
	@Value("${server.port:42001}")
	protected int serverPort;
	//#项目配置表格式
	@Value("${csv.file.path:E:/Workspace/Svn/x1doc/config/export/server-csv}")
	protected String csvFilePath;
	//#服务器的语言环境
	@Value("${cross.language:en}")
	protected String crossLanguage;
	//#服务器是否调试模式
	@Value("${cross.debug.mode:true}")
	protected boolean crossDebugMode;
	//#热更配置文件夹
	@Value("${cross.fix.design.folder:/uploadfiles/design/}")
	protected String crossFixDesignFolder;
	//#热更代码文件夹
	@Value("${cross.fix.clazzs.folder:/uploadfiles/clazzs/}")
	protected String crossFixClazzsFolder;
	//#后台加密秘钥
	@Value("${backstage.key:xiugouyouxi2023}")
	protected String backstageKey;
	//#后台地址
	@Value("${backstage.url:http://192.168.1.33:9001}")
	protected String backstageUrl;
	//#数据库配置
	@Value("${database.config.file:file:./config/database.db}")
	protected String databaseConfigFile;
	//#日志输出格式配置
	@Value("${logging.config:file:./config/log4j2.xml}")
	protected String loggingConfig;
	//#===================redis基础配置===================
	@Value("${spring.redis.database:1}")
	protected int springRedisDatabase;
	@Value("${spring.redis.host:127.0.0.1}")
	protected String springRedisHost;
	@Value("${spring.redis.password:yy2024}")
	protected String springRedisPassword;
	@Value("${spring.redis.port:6379}")
	protected int springRedisPort;
	//# 连接超时时间 单位 ms（毫秒）
	@Value("${spring.redis.timeout:3000}")
	protected int springRedisTimeout;


	public int getCrossServerId() {
		return crossServerId;
	}
	public int getCorssServerPort() {
		return corssServerPort;
	}
	public int getServerPort() {
		return serverPort;
	}
	public String getCsvFilePath() {
		return csvFilePath;
	}
	public boolean isCrossDebugMode() {
		return crossDebugMode;
	}
	public String getCrossFixDesignFolder() {
		return crossFixDesignFolder;
	}
	public String getCrossFixClazzsFolder() {
		return crossFixClazzsFolder;
	}
	public String getBackstageKey() {
		return backstageKey;
	}
	public String getBackstageUrl() {
		return backstageUrl;
	}
	public String getLoggingConfig() {
		return loggingConfig;
	}
	public int getCrossServerPlatformid() {
		return crossServerPlatformid;
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
	public String getCrossLanguage() {
		return crossLanguage;
	}
	public String getDatabaseConfigFile() {
		return databaseConfigFile;
	}
}
