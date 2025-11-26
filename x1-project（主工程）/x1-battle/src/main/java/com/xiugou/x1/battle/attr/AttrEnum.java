/**
 * 
 */
package com.xiugou.x1.battle.attr;

import java.util.HashMap;
import java.util.Map;

import com.xiugou.x1.battle.sprite.Sprite;

/**
 * @author YY
 *
 */
public abstract class AttrEnum {
	public static Map<String, AttrEnum> map = new HashMap<>();
	
	private final int attrId;
	private final String attrName;

	protected AttrEnum(int attrId, String attrName) {
		this.attrId = attrId;
		this.attrName = attrName;
		map.put(attrName, this);
	}

	public String getAttrName() {
		return attrName;
	}

	public abstract long getAttrValue(Sprite sprite);

//	public abstract long getBaseValue(Sprite sprite);

	public static AttrEnum valueOf(String attrName) {
		return map.get(attrName);
	}

	public int getAttrId() {
		return attrId;
	}
}
