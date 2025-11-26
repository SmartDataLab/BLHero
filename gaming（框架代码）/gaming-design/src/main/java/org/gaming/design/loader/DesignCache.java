/**
 * 
 */
package org.gaming.design.loader;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.design.exception.DesignCacheLoadingException;
import org.gaming.design.exception.DesignNotFoundException;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public abstract class DesignCache<T extends IDesignData> {

	protected static Logger logger = LoggerFactory.getLogger(DesignCache.class);
	
	private List<T> dataList;
	private Map<Integer, T> dataMap;
	
	private Class<T> clazz;
	private boolean hasError = false;
	
	private DesignParser designParser;
	
	@SuppressWarnings("unchecked")
	public DesignCache() {
		this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		DesignCacheManager.register(this);
		this.designParser = new JsonDesignParser();
	}
	
	public abstract String fileName();
	
	protected void loadAutoGenerate() {}
	protected void loadAfterReady() {}
	protected void loadAfterAllReady() {}
	protected boolean check() {
		return true;
	}
	
	public final void load(List<DesignField> fields, List<String[]> bodyDatas) throws DesignCacheLoadingException {
		List<T> dataList = new ArrayList<>();
		Map<String, Field> fieldMap = getDeclaredFields();
		for(int i = 0; i < bodyDatas.size(); i++) {
			String[] data = bodyDatas.get(i);
			T obj = formObject(i, fieldMap, fields, data);
			dataList.add(obj);
		}
		load(dataList);
	}
	
	public final void load(List<T> dataList) throws DesignCacheLoadingException {
		Map<Integer, T> dataMap = new HashMap<>();
		
		hasError = false;
		for(T obj : dataList) {
			if(dataMap.containsKey(obj.id())) {
				logger.error("文件【{}】在解释时发现的重复ID【{}】的数据", fileName(), obj.id());
				hasError = true;
				continue;
			}
			dataMap.put(obj.id(), obj);
		}
		this.dataList = dataList;
		this.dataMap = dataMap;
		
		loadAutoGenerate();
		loadAfterReady();
		
		if(hasError) {
			throw new DesignCacheLoadingException(fileName());
		}
	}
	
	public final List<T> all() {
		return this.dataList;
	}
	
	public final T getOrNull(int key) {
		return dataMap.get(key);
	}
	
	public final T getOrThrow(int key) {
		T t = dataMap.get(key);
		if(t == null) {
			throw new DesignNotFoundException(this.getClass().getSimpleName() + ".getOrThrow", key);
		}
		return t;
	}
	
	private Map<String, Field> getDeclaredFields() {
		Map<String, Field> fieldMap = new HashMap<>();
		for(Field field : clazz.getDeclaredFields()) {
			fieldMap.put(field.getName(), field);
		}
		//超类的字段
		Class<?> superClass = clazz.getSuperclass();
		while(superClass != null) {
			for(Field field : superClass.getDeclaredFields()) {
				fieldMap.put(field.getName(), field);
			}
			superClass = superClass.getSuperclass();
		}
		return fieldMap;
	}
	
	/**
     * 创建对象，并进行自动赋值
     *
     * @param rowNum  行号
     * @param rowData
     * @return
     */
    private final T formObject(int rowIndex, Map<String, Field> fieldMap, List<DesignField> fields, String[] bodyData) {
    	Map<String, String> headToBody = new HashMap<>();
    	for (int i = 0; i < fields.size() && i < bodyData.length; i++) {
    		headToBody.put(fields.get(i).getName(), bodyData[i]);
    	}
		T obj = newInstance();
    	for(Entry<String, String> entry : headToBody.entrySet()) {
    		Field field = fieldMap.get(entry.getKey());
    		if(field == null) {
    			continue;
    		}
    		String bodyValue = entry.getValue();
    		try {
				Object value = formValue(field, bodyValue);
				if (value == null) {
					continue;
				}
				field.setAccessible(true);
				field.set(obj, value);
				field.setAccessible(false);
			} catch (Exception e) {
				logger.error("文件【{}】在解释第【{}】行字段【{}】的数据时发现错误，无法解释的数据【{}】", fileName(), rowIndex, field.getName(), bodyValue);
				logger.error(e.getMessage());
				hasError = true;
			}
    	}
        return obj;
    }
    
    private T newInstance() {
    	try {
			T obj = clazz.newInstance();
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	return null;
    }
	
    
    private Object formValue(Field field, String value) {
    	Object valueObj = null;
		
		if(int.class.equals(field.getType()) || Integer.class.equals(field.getType())) {
			
			valueObj = Integer.parseInt(value);
			
		} else if(long.class.equals(field.getType()) || Long.class.equals(field.getType())) {
			
			valueObj = Long.parseLong(value);
			
		} else if(float.class.equals(field.getType()) || Float.class.equals(field.getType())) {
			
			valueObj = Float.parseFloat(value);
			
		} else if(String.class.equals(field.getType())) {
			
			valueObj = value;
			
		} else if(List.class.isAssignableFrom(field.getType())) {
			
			Class<?> paramClass = (Class<?>) ((ParameterizedType) field.getGenericType())
					.getActualTypeArguments()[0];
			valueObj = designParser.parseList(value, paramClass);
			
		} else if(Map.class.isAssignableFrom(field.getType())) {
			
			Class<?> keyClazz = (Class<?>) ((ParameterizedType) field.getGenericType())
					.getActualTypeArguments()[0];
			Class<?> valueClazz = (Class<?>) ((ParameterizedType) field.getGenericType())
					.getActualTypeArguments()[0];
			
			valueObj = designParser.parseMap(value, keyClazz, valueClazz);
			
		} else {
			valueObj = designParser.parseStruct(value, field.getType());
			
		}
		return valueObj;
    }
    
    protected final void loadConst(Map<String, String> constMap) {
    	for(Field field : this.getClass().getSuperclass().getDeclaredFields()) {
    		String constValue = constMap.get(field.getName());
    		if(constValue == null) {
    			logger.error("{}缺少索引名为{}的常量配置", fileName(), field.getName());
    			continue;
    		}
    		Object value = formValue(field, constValue);
			if (value == null) {
				continue;
			}
			try {
				field.setAccessible(true);
				field.set(this, value);
				field.setAccessible(false);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * 打印当前缓存中的数据
     * @return
     */
    public String printCacheData() {
    	StringBuilder builder = new StringBuilder();
    	List<Field> fieldList = new ArrayList<>();
    	for(Field field : clazz.getDeclaredFields()) {
    		builder.append(field.getName()).append("\t");
    		field.setAccessible(true);
    		fieldList.add(field);
    	}
    	try {
	    	for(T t : this.dataList) {
	    		builder.append("\n");
	    		for(Field field : fieldList) {
					builder.append(GsonUtil.toJson(field.get(t))).append("\t");
	    		}
	    	}
	    	System.out.println(builder.toString());
    	} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	return builder.toString();
    }

	public DesignParser getDesignParser() {
		return designParser;
	}

	public void setDesignParser(DesignParser designParser) {
		this.designParser = designParser;
	}
}
