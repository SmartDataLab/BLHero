/**
 * 
 */
package org.gaming.design.loader;

/**
 * @author YY
 *
 */
public interface DesignParser {
	
	Object parseList(String value, Class<?> clazz);
	Object parseMap(String value, Class<?> keyClazz, Class<?> valueClazz);
	Object parseStruct(String value, Class<?> clazz);
}
