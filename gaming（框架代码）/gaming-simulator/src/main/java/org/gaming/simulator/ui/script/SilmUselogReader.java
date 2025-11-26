/**
 * 
 */
package org.gaming.simulator.ui.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.tool.GsonUtil;
import org.gaming.tool.SortUtil;

/**
 * @author YY
 *
 */
public class SilmUselogReader {
	
	
	private static String fullLogFile;
	
	private static Map<String, SilmUselog> useMap = new HashMap<>();
	
	public static List<SilmUselog> readUselog(String fullLogFile) {
		SilmUselogReader.fullLogFile = fullLogFile;
		List<String> values = ScriptReader.readScript(fullLogFile);
		
		for(String value : values) {
			if(value != null && !"".equals(value)) {
				SilmUselog silmUselog = GsonUtil.parseJson(value, SilmUselog.class);
				useMap.put(silmUselog.resume(), silmUselog);
			}
		}
		
		List<SilmUselog> list = new ArrayList<>(useMap.values());
		SortUtil.sortIntDesc(list, SilmUselog::getUseTime);
		return list; 
	}
	
	public static SilmUselog saveUselog(String serverId, String openId, String host) {
		String key = serverId + "-" + openId + "-" + host;
		SilmUselog log = useMap.get(key);
		if(log == null) {
			log = new SilmUselog();
			log.serverId = serverId;
			log.openId = openId;
			log.host = host;
			useMap.put(log.resume(), log);
		}
		log.useTime = (int)(System.currentTimeMillis() / 1000);
		
		while(useMap.size() >= 32) {
			List<SilmUselog> list = new ArrayList<>(useMap.values());
			SortUtil.sortIntDesc(list, SilmUselog::getUseTime);
			useMap.remove(list.get(list.size() - 1).resume());
		}
		
		SilmUselogReader.saveLog();
		return log;
	}
	
	public static void saveSearchText(String serverId, String openId, String host, int index, String searchText) {
		String key = serverId + "-" + openId + "-" + host;
		SilmUselog log = useMap.get(key);
		if(log == null) {
			return;
		}
		if(index < log.getSearch().length) {
			log.getSearch()[index] = searchText;
		}
		SilmUselogReader.saveLog();
	}
	
	public static void saveLog() {
		List<String> content = new ArrayList<>();
		for(SilmUselog temp : useMap.values()) {
			content.add(GsonUtil.toJson(temp));
		}
		ScriptReader.saveScript(SilmUselogReader.fullLogFile, content);
	}
	
	public static SilmUselog parse(String value) {
		String[] split = value.split("-");
		SilmUselog silmUselog = new SilmUselog();
		silmUselog.serverId = split[0];
		silmUselog.openId = split[1];
		silmUselog.host = split[2];
		return silmUselog;
	}
	
	public static class SilmUselog {
		private String serverId;
		private String openId;
		private String host;
		private String[] search = new String[3];
		private int useTime;
		
		public String getServerId() {
			return serverId;
		}
		public String getOpenId() {
			return openId;
		}
		public String getHost() {
			return host;
		}
		public String resume() {
			return serverId + "-" + openId + "-" + host;
		}
		public int getUseTime() {
			return useTime;
		}
		public String[] getSearch() {
			return search;
		}
	}
}
