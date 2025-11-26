/**
 * 
 */
package org.gaming.db.mysql.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gaming.tool.GsonUtil;

/**
 * @author YY
 *
 */
public class DBConfig {
	/**
	 * 服务器ID
	 */
	private int id;
	/**
	 * 数据源的别名
	 */
	private String alias = "game";
	/**
	 * 数据库的连接地址
	 */
	private String ipPort = "127.0.0.1:3306";
	/**
	 * 数据库的名字
	 */
	private String dbName = "game1";
	/**
	 * 数据库用户
	 */
	private String user = "root";
	/**
	 * 数据库用户的密码
	 */
	private String password = "123456";
	/**
	 * 数据库最小空闲连接数
	 */
	private int minIdle = 1;
	/**
	 * 数据库最大连接数
	 */
	private int maxActive = 5;
	/**
	 * 数据库最大等待时间
	 */
	private long maxWaitMillis = 60000;
	
	/**
	 * 加载配置列表
	 * @param filePath
	 * @return
	 */
	public static List<DBConfig> loadConfigs(String filePath) {
		List<DBConfig> configs = new ArrayList<>();
		try {
			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String config = reader.readLine();
			while(config != null) {
				DBConfig dbConfig = GsonUtil.parseJson(config, DBConfig.class);
				if(dbConfig != null) {
					configs.add(dbConfig);
				}
				//读取下一行的配置信息
				config = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configs;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getIpPort() {
		return ipPort;
	}
	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	@Override
	public String toString() {
		return "DBConfig [id=" + id + ", alias=" + alias + ", ipPort=" + ipPort
				+ ", dbName=" + dbName + ", user=" + user + ", password="
				+ password + ", minIdle=" + minIdle + ", maxActive="
				+ maxActive + ", maxWaitMillis=" + maxWaitMillis + "]";
	}
}
