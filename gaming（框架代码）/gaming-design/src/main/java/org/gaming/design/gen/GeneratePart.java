/**
 * 
 */
package org.gaming.design.gen;

/**
 * @author YY
 *
 */
public interface GeneratePart {
	String methodPrefix();
	String nullHandle(String cacheName, String indexName, String fieldNames);
}
