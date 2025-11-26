/**
 * 
 */
package org.gaming.simulator.ui.script;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class PressUseLogReader {

	public static PressUselog readUselog() {
		
		String path = PressUseLogReader.class.getResource("/").getPath() + "pressuselog"; 
		
		List<String> values = ScriptReader.readScript(path);
		if(values.size() < 3) {
			return null;
		} else {
			return new PressUselog(values.get(0), values.get(1), values.get(2));
		}
	}
	
	public static void saveUselog(PressUselog uselog) {
		String path = PressUseLogReader.class.getResource("/").getPath() + "pressuselog"; 
		
		List<String> content = new ArrayList<>();
		content.add(uselog.getAreaId());
		content.add(uselog.getOpenIds());
		content.add(uselog.getHost());
		ScriptReader.saveScript(path, content);
	}
	
	public static class PressUselog {
		private String areaId;
		private String openIds;
		private String host;
		public PressUselog(String areaId, String openIds, String host) {
			this.areaId = areaId;
			this.openIds = openIds;
			this.host = host;
		}
		public String getAreaId() {
			return areaId;
		}
		public String getOpenIds() {
			return openIds;
		}
		public String getHost() {
			return host;
		}
		public void setAreaId(String areaId) {
			this.areaId = areaId;
		}
		public void setOpenIds(String openIds) {
			this.openIds = openIds;
		}
		public void setHost(String host) {
			this.host = host;
		}
	}
	
}
