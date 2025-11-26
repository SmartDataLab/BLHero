/**
 * 
 */
package org.gaming.db.repository;

import java.util.ArrayList;

import org.gaming.db.repository.QueryOptions.QueryOption;

/**
 * @author YY
 *
 */
@SuppressWarnings("serial")
public class QueryOptions extends ArrayList<QueryOption> {
	
	/**
	 * 
	 * @param fieldName 字段名
	 * @param value
	 * @return
	 */
	public QueryOptions put(String fieldName, Object value) {
		this.add(new QueryOption(fieldName, value));
		return this;
	}
	
	public class QueryOption {
		private final String fieldName;
		private final Object value;
		
		public QueryOption(String fieldName, Object value) {
			this.fieldName = fieldName;
			this.value = value;
		}
		
		public Object getValue() {
			return value;
		}

		public String getFieldName() {
			return fieldName;
		}
	}
	
	public Object[] getValues() {
		Object[] objs = new Object[this.size()];
		for(int i = 0; i < objs.length; i++) {
			objs[i] = this.get(i).getValue();
		}
		return objs;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean isFirst = true;
		for(QueryOption option : this) {
			if(isFirst) {
				builder.append(option.fieldName).append("=").append(option.value);
			} else {
				builder.append(",").append(option.fieldName).append("=").append(option.value);
			}
			isFirst = false;
		}
		return builder.toString();
	}
}
