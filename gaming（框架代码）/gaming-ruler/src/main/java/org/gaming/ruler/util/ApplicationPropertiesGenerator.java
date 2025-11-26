/**
 * 
 */
package org.gaming.ruler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author YY
 * 配置属性类生成工具
 */
public class ApplicationPropertiesGenerator {

	public static void readPropertiesFile(String path) {
		File file = new File(path);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			StringBuilder builder = new StringBuilder();
			
			String line = br.readLine();
			while(line != null) {
				try {
					line = line.trim();
					if(line.startsWith("#")) {
						builder.append("\n");
						builder.append("//").append(line);
					} else if("".equals(line)) {
						
					} else {
//						String[] keyValue = line.split("=");
						int sepIndex = line.indexOf("=");
						String key = line.substring(0, sepIndex);
						String value = line.substring(sepIndex + 1);
						
						builder.append("\n");
						builder.append("@Value(\"${" + key + ":" + value + "}\")");
						builder.append("\n");
						builder.append("protected ");
						try {
							//尝试进行int转换
							Integer.parseInt(value);
							builder.append("int ");
						} catch (Exception e1) {
							if("true".equals(value) || "false".equals(value)) {
								builder.append("boolean ");
							} else {
								builder.append("String ");
							}
						}
						builder.append(formatName(key)).append(";");
					}
				} catch (Exception e) {
					System.out.println("解释行" + line + "时异常");
				}
				line = br.readLine();
			}
			br.close();
			fr.close();
			
			System.out.println(builder.toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String formatName(String keyString) {
		keyString = keyString.replaceAll("-", "#").replaceAll("\\.", "#");
		
		StringBuilder builder = new StringBuilder();
		String[] keys = keyString.split("#");
		boolean isFirst = true;
		for(String key : keys) {
			if(isFirst) {
				builder.append(key);
			} else {
				//首字母大写
				builder.append(key.substring(0, 1).toUpperCase() + key.substring(1));
			}
			isFirst = false;
		}
		return builder.toString();
	}
}
