/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 * 协议分组
 */
public class ProtocolGroup {
	/**
	 * 分组名称
	 */
	private String name;
	/**
	 * 备注信息
	 */
	private String comment;
	/**
	 * 协议结构
	 */
	private List<ProtocolStruct> structs = new ArrayList<>();
	
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
	public List<ProtocolStruct> getStructs() {
		return structs;
	}
	@Override
	public String toString() {
		return "ProtocolGroup [name=" + name + ", comment=" + comment + ", structs=" + structs
				+ "]";
	}
}
