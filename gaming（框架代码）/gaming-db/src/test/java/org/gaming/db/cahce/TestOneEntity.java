/**
 * 
 */
package org.gaming.db.cahce;

import java.util.List;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;

/**
 * @author YY
 *
 */
@JvmCache
@Table(name = "test_one", comment = "", dbAlias = "game")
public class TestOneEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "")
	private long ownerId;
	@Column(comment = "", formatter = "json")
	private List<Struct> structs;
	@Column(comment = "", formatter = "json")
	private Map<Integer, Struct> structss;
	@Column(comment = "", formatter = "json")
	private Integer[] aaa;
	
	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public List<Struct> getStructs() {
		return structs;
	}

	public void setStructs(List<Struct> structs) {
		this.structs = structs;
	}

	public Map<Integer, Struct> getStructss() {
		return structss;
	}

	public void setStructss(Map<Integer, Struct> structss) {
		this.structss = structss;
	}

	public Integer[] getAaa() {
		return aaa;
	}

	public void setAaa(Integer[] aaa) {
		this.aaa = aaa;
	}
	
}
