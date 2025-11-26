/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.starting;

import org.gaming.ruler.util.ApplicationPropertiesGenerator;

/**
 * @author YY
 *
 */
public class ApplicationSettingsTool {
	
	public static void main(String[] args) {
		String projectDir = System.getProperty("user.dir");
		String game = projectDir + "/config/application.properties";
		ApplicationPropertiesGenerator.readPropertiesFile(game);
	}
}
