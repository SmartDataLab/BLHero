/**
 * 
 */
package com.xiugou.x1.game.server.foundation.starting;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @author YY
 *
 */
@Configuration
public class ApplicationSettings {
	private Set<Integer> serverIds;
	public Set<Integer> getGameServerIds() {
		if(serverIds == null) {
			Set<Integer> tempServerIds = new HashSet<>();
			tempServerIds.add(gameServerId);
			//TODO 添加合服后的服务器
			
			serverIds = tempServerIds;
		}
		return serverIds;
	}
	
	private Set<Integer> subServerIds;
	//获取附属（被合服）的服务器ID
	public Set<Integer> getSubServerIds() {
		if(subServerIds == null) {
			Set<Integer> temp = new HashSet<>();
			String[] parts = gameServerMergeIds.split(",");
			for(String part : parts) {
				int serverId = Integer.parseInt(part);
				if(serverId <= 0) {
					continue;
				}
				temp.add(serverId);
			}
			subServerIds = temp;
		}
		return subServerIds;
	}




	//#游戏服务器ID
	@Value("${game.server.id:4}")
	protected int gameServerId;
	//#被合服的服务器ID
	@Value("${game.server.merge.ids:0,0,0}")
	protected String gameServerMergeIds;
	//#平台编号
	@Value("${game.server.platformid:1001}")
	protected int gameServerPlatformid;
	//#游戏服务器端口
	@Value("${game.server.port:10004}")
	protected int gameServerPort;
	//#游戏服务器的tomcat端口
	@Value("${server.port:20004}")
	protected int serverPort;
	//#项目配置表格式
	@Value("${csv.file.path:D:/Svn/x1-config/export/server-csv}")
	protected String csvFilePath;
	//#项目配置表格式
	@Value("${map.file.path:D:/Svn/x1-map-editor/server}")
	protected String mapFilePath;
	//#游戏渠道标识，如37、yile、wabo、其他
	@Value("${game.channel.identity:other}")
	protected String gameChannelIdentity;
	//#服务器的语言环境
	@Value("${game.language:cn}")
	protected String gameLanguage;
	//#服务器是不是提审服
	@Value("${game.arraign.type:false}")
	protected boolean gameArraignType;
	//#服务器开服方式，1启动服务器时间作为开服时间，2后台开服时间作为开服时间
	@Value("${game.open.type:1}")
	protected int gameOpenType;
	//#服务器是否调试模式
	@Value("${game.debug.mode:true}")
	protected boolean gameDebugMode;
	//#服务器netty空闲时间检测，0表示不检查空闲
	@Value("${game.netty.idle:60}")
	protected int gameNettyIdle;
	//#热更配置文件夹
	@Value("${game.fix.design.folder:/uploadfiles/design/}")
	protected String gameFixDesignFolder;
	//#热更代码文件夹
	@Value("${game.fix.clazzs.folder:/uploadfiles/clazzs/}")
	protected String gameFixClazzsFolder;
	//#跨服IP
	@Value("${cross.server.ip:192.168.1.47}")
	protected String crossServerIp;
	//#跨服端口
	@Value("${cross.server.port:41002}")
	protected int crossServerPort;
	//#后台加密秘钥
	@Value("${backstage.key:xiugouyouxi2023}")
	protected String backstageKey;
	//#后台地址
	@Value("${backstage.url:http://127.0.0.1:9001}")
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

	
	public Set<Integer> getServerIds() {
		return serverIds;
	}
	public int getGameServerId() {
		return gameServerId;
	}
	public String getGameServerMergeIds() {
		return gameServerMergeIds;
	}
	public int getGameServerPlatformid() {
		return gameServerPlatformid;
	}
	public int getGameServerPort() {
		return gameServerPort;
	}
	public int getServerPort() {
		return serverPort;
	}
	public String getCsvFilePath() {
		return csvFilePath;
	}
	public String getMapFilePath() {
		return mapFilePath;
	}
	public String getGameChannelIdentity() {
		return gameChannelIdentity;
	}
	public int getGameOpenType() {
		return gameOpenType;
	}
	public boolean isGameDebugMode() {
		return gameDebugMode;
	}
	public int getGameNettyIdle() {
		return gameNettyIdle;
	}
	public String getGameFixDesignFolder() {
		return gameFixDesignFolder;
	}
	public String getGameFixClazzsFolder() {
		return gameFixClazzsFolder;
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
	public String getCrossServerIp() {
		return crossServerIp;
	}
	public int getCrossServerPort() {
		return crossServerPort;
	}
	public boolean isGameArraignType() {
		return gameArraignType;
	}
	public String getGameLanguage() {
		return gameLanguage;
	}
	public String getDatabaseConfigFile() {
		return databaseConfigFile;
	}
	
	
}
