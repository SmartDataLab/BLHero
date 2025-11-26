/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YY
 * 协议结构
 */
public class ProtocolStruct {
	/**
	 * 协议名称
	 */
	private String name;
	/**
	 * 备注信息
	 */
	private String comment;
	/**
	 * 字段属性
	 */
	private List<ProtocolField> fields = new ArrayList<>();
	private Map<String, ProtocolField> fieldsMap = new HashMap<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<ProtocolField> getFields() {
		return fields;
	}
	@Override
	public String toString() {
		return "\nProtocolStruct [name=" + name + ", comment=" + comment + ", fields=" + fields + "]";
	}
	public Map<String, ProtocolField> getFieldsMap() {
		return fieldsMap;
	}
}
