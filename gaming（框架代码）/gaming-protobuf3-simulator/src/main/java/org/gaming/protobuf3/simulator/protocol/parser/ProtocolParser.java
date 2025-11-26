/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 * 协议文件解释器
 */
public class ProtocolParser {
	
//	public static void main(String[] args) {
//		PropertiesUtil.loadConfig("config.properties");
//		parse(PropertiesUtil.getString("protoPath"));
//	}
	
	
	public static void parse(String protoDirPath) {
		File folder = new File(protoDirPath);
		
		List<ProtocolGroup> groupList = new ArrayList<>();
		
		for(File file : folder.listFiles()) {
			if(!file.getName().endsWith(".proto")) {
				continue;
			}
			
			ProtocolGroup group = new ProtocolGroup();
			group.setName(file.getName().replaceAll(".proto", ""));
			
			List<List<String>> protoList = getProtocolScopeList(file);
			
			for(List<String> scope : protoList) {
				ProtocolStruct struct = parseStruct(scope);
				group.getStructs().add(struct);
			}
			groupList.add(group);
		}
		
		
	}
	
	/**
	 * 解释出一个协议结构
	 * @param structList
	 * @return
	 */
	private static ProtocolStruct parseStruct(List<String> structList) {
		ProtocolStruct struct = new ProtocolStruct();
		for(String line : structList) {
			line = line.trim();
			if(line.startsWith("//")) {
				struct.setComment(line.replaceAll("//", "").trim());
			} else if(line.startsWith("message")) {
				struct.setName(line.replaceFirst("message", "").replaceAll("\\{", "").trim());
			} else if(line.startsWith("optional") || line.startsWith("repeated")) {
				ProtocolField field = new ProtocolField();
				
				String[] temps = line.split("//");
				if(temps.length > 1) {
					field.setComment(temps[1].replaceAll("//", "").trim());
				}
				String[] fieldStrs = temps[0].substring(0, temps[0].indexOf("=")).split(" ");
				
				field.setIdentifier(fieldStrs[0].trim());
				field.setType(fieldStrs[1].trim());
				field.setName(fieldStrs[2].trim());
				
				struct.getFields().add(field);
				struct.getFieldsMap().put(field.getName(), field);
			}
		}
		return struct;
	}
	
	/**
	 * 划分每个协议结构的字符串
	 * @param protocolFile
	 * @return
	 */
	private static List<List<String>> getProtocolScopeList(File protocolFile) {
		
		//协议结构列表
		List<List<String>> scopeList = new ArrayList<>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(protocolFile)));
			
			List<String> lineList = new ArrayList<>();
			
			//标记是否为协议结构
			boolean scoping = false;
			
			//单个协议结构的内容列表
			List<String> scopeBuilder = null;
			
			String line = reader.readLine();
			
			while(line != null) {
				//协议结构开始
				if(line.startsWith("message")) {
					scoping = true;
					scopeBuilder = new ArrayList<>();
					
					//结构的备注
					String comment = lineList.get(lineList.size() - 1);
					if(comment.startsWith("//")) {
						scopeBuilder.add(comment);
					}
				}
				//协议结构结束
				if(line.contains("}")) {
					scopeBuilder.add(line.substring(0, line.indexOf("}") + 1));
					scopeList.add(scopeBuilder);
					scoping = false;
				}
				
				if(scoping) {
					scopeBuilder.add(line);
				}
				
				lineList.add(line);
				line = reader.readLine();
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scopeList;
	}
}
