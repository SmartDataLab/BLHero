/**
 * 
 */
package org.gaming.db.entity;

import java.util.List;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.cahce.Struct;
import org.gaming.db.orm.AbstractEntity;

/**
 * @author YY
 *
 */
@JvmCache
@LogTable(name = "time_many_entity", comment = "", dbAlias = "log", byColumn = "time", asyncType = AsyncType.INSERT)
public class TimeManyEntity extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "")
	private long id;
	@Column(comment = "")
	private long rid;
	@Column(comment = "")
	private int type;
	@Column(comment = "")
	private String name;
	@Column(comment = "", length = 1000, formatter = "json")
	private List<Integer> intList;
	@Column(comment = "", length = 1000, formatter = "json")
	private Map<Integer, Long> intMap;
	@Column(comment = "", length = 1000, formatter = "json")
	private List<Struct> structList;
	@Column(comment = "", length = 1000, formatter = "json")
	private Map<Integer, Struct> structMap;
	@Column(comment = "")
	private long time;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getIntList() {
		return intList;
	}
	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}
	public Map<Integer, Long> getIntMap() {
		return intMap;
	}
	public void setIntMap(Map<Integer, Long> intMap) {
		this.intMap = intMap;
	}
	public List<Struct> getStructList() {
		return structList;
	}
	public void setStructList(List<Struct> structList) {
		this.structList = structList;
	}
	public Map<Integer, Struct> getStructMap() {
		return structMap;
	}
	public void setStructMap(Map<Integer, Struct> structMap) {
		this.structMap = structMap;
	}
}
