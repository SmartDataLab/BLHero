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
public class DesignCollectorMeta {
	private List<String> fields;
	
	/**
	 * 
	 * @param metaDefine As #collector a b c...
	 */
	public DesignCollectorMeta(String metaDefine) {
		String[] strs = metaDefine.split(" ");
		List<String> fields = new ArrayList<>();
		for (int i = 1; i < strs.length; i++) {
			fields.add(strs[i].trim());
		}
		this.fields = Collections.unmodifiableList(fields);
	}
	
	private String name;
	
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
			name += "Collector";
		}
		return name;
	}
	public List<String> getFields() {
		return fields;
	}
}
