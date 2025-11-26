/**
 * 
 */
package org.gaming.db.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.cahce.Struct;
import org.gaming.db.orm.AbstractEntity;

/**
 * @author YY
 *
 */
@JvmCache
@Table(name = "default_one_entity", comment = "", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class DefaultOneEntity extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "")
	private long id;
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
	private Date createTime;
	@Column(comment = "")
	private TestEnum number = TestEnum.THREE;
	
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public TestEnum getNumber() {
		return number;
	}
	public void setNumber(TestEnum number) {
		this.number = number;
	}
}
