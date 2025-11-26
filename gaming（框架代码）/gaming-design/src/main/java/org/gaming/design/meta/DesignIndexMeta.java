/**
 * 
 */
package org.gaming.design.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author YY
 *
 */
public class DesignIndexMeta {
	private List<String> fields;
	
	private String name;
	
	/**
	 * 
	 * @param metaDefine As #index a b c...
	 */
	public DesignIndexMeta(String metaDefine) {
		String[] strs = metaDefine.split(" ");
		List<String> fields = new ArrayList<>();
		for (int i = 1; i < strs.length; i++) {
			fields.add(strs[i].trim());
		}
		this.fields = Collections.unmodifiableList(fields);
	}
	
	public String getName() {
		if(name == null) {
			name = "";
			boolean first = true;
			for(String field : fields) {
				if(first) {
					name += field;
				} else {
					name += field.substring(0, 1).toUpperCase() + field.substring(1);
				}
				first = false;
			}
			name += "Index";
		}
		return name;
	}
	
	public List<String> getFields() {
		return fields;
	}
}
