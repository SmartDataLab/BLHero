/**
 * 
 */
package org.gaming.design.loader;

import org.gaming.tool.GsonUtil;

/**
 * @author YY
 *
 */
public class JsonDesignParser implements DesignParser {

	@Override
	public Object parseList(String value, Class<?> clazz) {
		return GsonUtil.getList(value, clazz);
	}

	@Override
	public Object parseMap(String value, Class<?> keyClazz, Class<?> valueClazz) {
		return GsonUtil.getMap(value, keyClazz, valueClazz);
	}

	@Override
	public Object parseStruct(String value, Class<?> clazz) {
		return GsonUtil.parseJson(value, clazz);
	}

}
